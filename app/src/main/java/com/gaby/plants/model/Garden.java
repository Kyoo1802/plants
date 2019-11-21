package com.gaby.plants.model;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;


@Getter
public class Garden {
    private long gardenId;
    private Map<Long, Plant> plants = new LinkedHashMap<>();

    public int getNumPlants() {
        return plants.size();
    }
}
