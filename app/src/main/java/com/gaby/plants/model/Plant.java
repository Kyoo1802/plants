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
    private long dateOfBirth;
    private int waterPercentage;
    private long lastTimeWater;
    private int abonoPercentage;
    private long lastTimeAbono;

    public int getCorrectSunAmount() {
        switch (plantType) {
            case SUNFLOWER:
                return 20;
            case CORN:
                return 40;
            default:
                return 50;
        }
    }

    public PlantState getNextState() {
        switch (plantState){
            case PREP_GROUND:
                return PlantState.PREP_COMPOST;
            case PREP_COMPOST:
                return PlantState.SEED;
            case SEED:
                return PlantState.SPROUD;
            case SPROUD:
            case FRUIT_PLANT:
                return PlantState.PLANT;
            case PLANT:
                return PlantState.FRUIT_PLANT;
            default:
                return PlantState.UNSPECIFIED;
        }
    }

    public int getWaterIncrease() {
        switch (plantState){
            case SEED:
                return 25;
            case SPROUD:
                return 21;
            case PLANT:
                return 10;
            case FRUIT_PLANT:
                return 1;
            default:
                return 20;
        }
    }

    public int getAbonoIncrease() {
        switch (plantState){
            case SEED:
                return 25;
            case SPROUD:
                return 20;
            case PLANT:
                return 10;
            case FRUIT_PLANT:
                return 1;
            default:
                return 20;
        }
    }
}
