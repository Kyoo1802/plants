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
    private int waterPercentage;
    private long lastTimeWater;
    private int abonoPercentage;
    private long lastTimeAbono;

    public int getCorrectSunAmount() {
        switch (plantType) {
            case STRAWBERRY:
                return 20;
            case TOMATOE:
                return 40;
            default:
                return 50;
        }
    }
}
