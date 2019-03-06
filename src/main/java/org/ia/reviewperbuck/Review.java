package org.ia.reviewperbuck;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
public class Review {

    private Long id;
    private String model;
    private float rating;

}