package org.ia.reviewperbuck;

import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AxesorController {

    private Storage storage;
    RestTemplate restTemplate;

    List<Television> televisions;
    List<Review> reviews;

    public AxesorController(RestTemplate restTemplate, Storage storage) {
        this.storage = storage;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/axesors")
    public List<Axesor> getAll() {
        return storage.findAll();
    }

    @GetMapping("/loadinfo")
    public List<Axesor> getReviewsAndTelevisions() {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);

        ResponseEntity<List<Review>> responseEntityReviews =
                restTemplate.exchange("http://review-app/reviews/",
                        HttpMethod.GET,
                        httpEntity,
                        new ParameterizedTypeReference<List<Review>>()
                        {});

        reviews = responseEntityReviews.getBody();

        ResponseEntity<List<Television>> responseEntityTelevisions =
                restTemplate.exchange("http://specs-app/televisions/",
                        HttpMethod.GET,
                        httpEntity,
                        new ParameterizedTypeReference<List<Television>>() {
                        });

        televisions = responseEntityTelevisions.getBody();

        return combineLists();

    }

    private List<Axesor> combineLists() {
        List<Axesor> axesors = reviews.stream()
                .map( review -> new Axesor(review.getModel(), review.getRating()))
                .collect(Collectors.toList());

        for ( Axesor a : axesors ) {
            for ( Television t : televisions) {
                if (a.getModel().equals(t.getModel())) {
                    a.setSpecs(t.getSpecs());
                    a.setPrice(t.getPrice());
                }
            }
        }

        storage.saveAll(axesors);
        return storage.findAll();
    }

    @GetMapping("/ratingprice")
    public List<Axesor> getValue() {

        return storage.findAll().stream().sorted( Comparator.comparing( Axesor::getPricePerRating ) ).collect(Collectors.toList());
    }

    @GetMapping("/ratingpriceinch")
    public List<Axesor> getRatingPriceInch() {

        return storage.findAll().stream().sorted( Comparator.comparing( Axesor::getPricePerRatingPerInch ) ).collect(Collectors.toList());
    }


    @GetMapping("/mockdata")
    public void mockData() {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);

        ResponseEntity<List<Review>> responseEntityReviews =
                restTemplate.exchange("http://review-app/manyreviews/",
                        HttpMethod.GET,
                        httpEntity,
                        new ParameterizedTypeReference<List<Review>>()
                        {});

        reviews = responseEntityReviews.getBody();

        ResponseEntity<List<Television>> responseEntityTelevisions =
                restTemplate.exchange("http://specs-app/manytelevisions/",
                        HttpMethod.GET,
                        httpEntity,
                        new ParameterizedTypeReference<List<Television>>() {
                        });

        televisions = responseEntityTelevisions.getBody();
    }
}
