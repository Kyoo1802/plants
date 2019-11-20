package com.gaby.plants.model;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class Plant {
    private long plantId;
    private PlantType plantType;
    private PlantState plantState;
    private boolean hasSunLight;
    private boolean hasCompost;
    private long dateOfBirth;
    private long lastTimeWater;
    private long lastTimeAbono;
}
