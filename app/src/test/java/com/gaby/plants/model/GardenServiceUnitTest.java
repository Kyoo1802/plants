package com.gaby.plants.model;


import com.google.common.truth.Truth;

import org.junit.Test;

import java.util.Collection;

public class GardenServiceUnitTest {

    static long PLANT_ID_1 = 123L;
    static long PLANT_ID_2 = 321L;

    @Test
    public void addPlant_Pass() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        gs.addPlant(createPlant(123L));

        Truth.assertThat(garden.getNumPlants()).isEqualTo(1);
    }

    @Test
    public void add2Plants_Pass() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        gs.addPlant(createPlant(123L));
        gs.addPlant(createPlant(321L));

        Truth.assertThat(garden.getNumPlants()).isEqualTo(2);
    }

    @Test
    public void addPlant_checkProperties_Pass() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        gs.addPlant(createPlant(123L));

        Truth.assertThat(garden.getPlants().get(123L).getPlantId())
                .isEqualTo(123L);
        Truth.assertThat(garden.getPlants().get(123L).getPlantState())
                .isEqualTo(PlantState.GROUND);
        Truth.assertThat(garden.getPlants().get(123L).isHasSunLight())
                .isEqualTo(false);
        Truth.assertThat(garden.getPlants().get(123L).isHasCompost())
                .isEqualTo(false);
        Truth.assertThat(garden.getPlants().get(123L).getDateOfBirth())
                .isEqualTo(123123);
        Truth.assertThat(garden.getPlants().get(123L).getLastTimeAbono())
                .isEqualTo(123123);
        Truth.assertThat(garden.getPlants().get(123L).getLastTimeWater())
                .isEqualTo(123123);
        Truth.assertThat(garden.getPlants().get(123L).getPlantType())
                .isEqualTo(PlantType.STRAWBERRY);
    }

    @Test
    public void adjustLightSun_Pass() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        gs.addPlant(createPlant(123L));
        gs.addPlant(createPlant(321L));

        gs.adjustLightSun(321L);

        Truth.assertThat(garden.getPlants().get(321L).isHasSunLight())
                .isEqualTo(true);
    }

    @Test
    public void setCompost_Pass() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        gs.addPlant(createPlant(123L));

        gs.setCompost(123L);

        Truth.assertThat(garden.getPlants().get(123L).isHasCompost())
                .isEqualTo(true);
    }


    @Test
    public void setCompost_NoUpdates() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        gs.addPlant(createPlant(123L));

        gs.setCompost(321L);

        Truth.assertThat(garden.getPlants().get(123L).isHasCompost())
                .isEqualTo(false);
    }

    @Test
    public void changePlantState_changeToSeed() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        long plantID1 = 123L;

        gs.addPlant(createPlant(plantID1));

        gs.changePlantState(plantID1, PlantState.SEED);
        Truth.assertThat(garden.getPlants().get(plantID1).getPlantState())
                .isEqualTo(PlantState.SEED);

        gs.changePlantState(plantID1, PlantState.PLANT);
        Truth.assertThat(garden.getPlants().get(plantID1).getPlantState())
                .isEqualTo(PlantState.PLANT);

        gs.changePlantState(plantID1, PlantState.FRUIT_PLANT);
        Truth.assertThat(garden.getPlants().get(plantID1).getPlantState())
                .isEqualTo(PlantState.FRUIT_PLANT);

        gs.changePlantState(plantID1, PlantState.WITHERED);
        Truth.assertThat(garden.getPlants().get(plantID1).getPlantState())
                .isEqualTo(PlantState.WITHERED);
    }


    @Test
    public void listPlants_Pass() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        gs.addPlant(createPlant(123L));
        gs.addPlant(createPlant(321L));

        Collection<Plant> plants = gs.listPlants();
        Truth.assertThat(plants.size()).isEqualTo(2);
    }

    @Test
    public void collectFruits_Pass() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        gs.addPlant(createPlant(PLANT_ID_1));
        gs.changePlantState(PLANT_ID_1, PlantState.FRUIT_PLANT);

        gs.collectFruits(PLANT_ID_1);

        Truth.assertThat(garden.getPlants().get(PLANT_ID_1).getPlantState())
                .isEqualTo(PlantState.PLANT);
    }


    @Test
    public void collectFruits_NoUpdate() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        gs.addPlant(createPlant(PLANT_ID_1));
        gs.changePlantState(PLANT_ID_1, PlantState.PLANT);

        gs.collectFruits(PLANT_ID_1);

        Truth.assertThat(garden.getPlants().get(PLANT_ID_1).getPlantState())
                .isEqualTo(PlantState.PLANT);
    }

    @Test
    public void getGardenInfo_Pass() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        gs.addPlant(createPlant(PLANT_ID_1));
        gs.changePlantState(PLANT_ID_1, PlantState.PLANT);

        gs.addPlant(createPlant(PLANT_ID_2));


        String info = gs.getGardenInformation();

        Truth.assertThat(info)
                .isEqualTo(
                        PLANT_ID_1 + " : " + PlantState.PLANT + "\n" +
                                PLANT_ID_2 + " : " + PlantState.GROUND + "\n");
    }

    @Test
    public void getPlantInfo_Pass() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        gs.addPlant(createPlant(PLANT_ID_1));
        gs.changePlantState(PLANT_ID_1, PlantState.GROUND);

        gs.addPlant(createPlant(PLANT_ID_2));


        String info = gs.getPlantInformation(PLANT_ID_1);

        Truth.assertThat(info)
                .isEqualTo(
                        PLANT_ID_1 + " : " + PlantState.GROUND + " " + PlantType.STRAWBERRY + "\n");
    }

    @Test
    public void removePlants_Pass() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        gs.addPlant(createPlant(123L));
        gs.addPlant(createPlant(321L));

        gs.removePlant(321L);

        Truth.assertThat(garden.getNumPlants()).isEqualTo(1);
    }

    private Plant createPlant(long id) {
        return Plant.builder()
                .plantId(id)
                .dateOfBirth(123123)
                .plantType(PlantType.STRAWBERRY)
                .build();
    }
}
