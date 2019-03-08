package org.ia.reviewperbuck;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashMap;

@Data
@Entity
public class Axesor {

    public Axesor() {    }

    public Axesor(String model, Float rating) {
        this.model = model;
        this.rating = rating;
    }


    private @Id @GeneratedValue long id;
    private String model;
    private float rating;
    private HashMap<String, String> specs;
    private float price;

    public float getPricePerRating() {
        return price / rating;
    }

    public float getPricePerRatingPerInch() {
        return getPricePerRating() / getSpecs().size();
    }
}
