package com.gaby.plants.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;


@Getter
public class Garden {
    private long gardenId;
    private Map<Long, Plant> plants = new HashMap<>();

    public int getNumPlants() {
        return plants.size();
    }
}
