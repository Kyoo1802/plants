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

    public int[] getCorrectSunAmount() {
        switch (plantType) {
            case SUNFLOWER:
                return new int[]{20, 25};
            case CORN:
                return new int[]{20, 32};
            case CALADIO:
                return new int[]{20, 22};
            case HELECHOS:
                return new int[]{16, 21};
            case MARGARITA:
                return new int[]{15, 25};
            case ALOEVERA:
                return new int[]{10, 32};
            default:
                return new int[]{0, 100};
        }
    }

    public PlantState getNextState() {
        switch (plantState) {
            case PREP_GROUND:
                return PlantState.PREP_COMPOST;
            case PREP_COMPOST:
                return PlantState.SEED;
            case SEED:
                return PlantState.SPROUD;
            case SPROUD:
                return PlantState.PLANT;
            case FRUIT_PLANT:
            case PLANT:
                return PlantState.FRUIT_PLANT;
            default:
                return PlantState.UNSPECIFIED;
        }
    }

    public int getWaterIncrease() {
        return 25;
    }

    public int getAbonoIncrease() {
        return 25;
    }
}
