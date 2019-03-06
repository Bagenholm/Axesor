package org.ia.reviewperbuck;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashMap;

@Data
public class Television {

    private Long id;
    private String model;
    private HashMap<String, String> specs;

}
