package org.ia.reviewperbuck;

import lombok.Data;

import javax.persistence.Entity;
import java.util.HashMap;

@Data
@Entity
public class Axesor {

    private String model;
    private float rating;
    private HashMap<String, String> specs;
    private float price;
}
