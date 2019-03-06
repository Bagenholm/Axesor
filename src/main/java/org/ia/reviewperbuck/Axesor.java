package org.ia.reviewperbuck;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashMap;

@Data
@Entity
public class Axesor {


    private @Id @GeneratedValue long id;
    private String model;
    private float rating;
    private HashMap<String, String> specs;
    private float price;
}
