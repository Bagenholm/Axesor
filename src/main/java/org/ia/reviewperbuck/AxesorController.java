package org.ia.reviewperbuck;

import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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

    @GetMapping("/loadinfo")
    public void getReviewsAndTelevisions() {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);

        ResponseEntity<List<Review>> responseEntityReviews =
                restTemplate.exchange("http://review-app/reviews/",
                        HttpMethod.GET,
                        httpEntity,
                        new ParameterizedTypeReference<List<Review>>()
                        {});

        reviews = responseEntityReviews.getBody();

        System.out.println("Reviews loaded? " + reviews.toString());

        ResponseEntity<List<Television>> responseEntityTelevisions =
                restTemplate.exchange("http://specs-app/televisions/",
                        HttpMethod.GET,
                        httpEntity,
                        new ParameterizedTypeReference<List<Television>>() {
                        });

        televisions = responseEntityTelevisions.getBody();

        System.out.println("Televisions loaded? " + televisions.toString());

        combineLists();

    }

    private void combineLists() {
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

        System.out.println(storage.toString());
    }

    @GetMapping("/value")
    public List<Axesor> getValue() {
        storage.findAll().sort( (Axesor a1, Axesor a2 ) -> (int) a1.getPricePerRating() - (int) a2.getPricePerRating() );

        return storage.findAll();
    }

    @GetMapping("/axesors")
    public List<Axesor> getAll() {
        return storage.findAll();
    }
}
