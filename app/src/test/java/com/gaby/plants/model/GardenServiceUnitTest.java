package com.gaby.plants.model;


import com.google.common.truth.Truth;

import org.junit.Test;

public class GardenServiceUnitTest {

    @Test
    public void addPlant_Pass() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        Plant plant1 = Plant.builder()
                .plantId(123)
                .build();
        gs.addPlant(plant1);

        Truth.assertThat(garden.getNumPlants()).isEqualTo(1);
    }

    @Test
    public void add2Plants_Pass() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        Plant plant1 = Plant.builder()
                .plantId(123)
                .build();
        gs.addPlant(plant1);

        Plant plant2 = Plant.builder()
                .plantId(321)
                .build();
        gs.addPlant(plant2);

        Truth.assertThat(garden.getNumPlants()).isEqualTo(2);
    }

    @Test
    public void addPlant_checkProperties_Pass() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        Plant plant1 = Plant.builder()
                .plantId(123L)
                .dateOfBirth(123123)
                .plantType(PlantType.STRAWBERRY)
                .build();
        gs.addPlant(plant1);

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

        Plant plant1 = Plant.builder()
                .plantId(123)
                .dateOfBirth(123123)
                .plantType(PlantType.STRAWBERRY)
                .build();
        gs.addPlant(plant1);

        Plant plant2 = Plant.builder()
                .plantId(321)
                .dateOfBirth(123123)
                .plantType(PlantType.STRAWBERRY)
                .build();
        gs.addPlant(plant2);

        gs.adjustLightSun(321L);

        Truth.assertThat(garden.getPlants().get(321L).isHasSunLight())
                .isEqualTo(true);
    }

    @Test
    public void play_Pass() {
        Garden garden = new Garden();
        GardenService gs = new GardenService(garden);

        Plant plant1 = Plant.builder()
                .plantId(123)
                .dateOfBirth(123123)
                .plantType(PlantType.STRAWBERRY)
                .build();
        gs.addPlant(plant1);

        Plant plant2 = Plant.builder()
                .plantId(321)
                .dateOfBirth(123123)
                .plantType(PlantType.STRAWBERRY)
                .build();
        gs.addPlant(plant2);

        gs.adjustLightSun(321L);
        gs.setCompost(321L);


        Truth.assertThat(garden.getPlants().get(321L).getPlantId())
                .isEqualTo(321L);
        Truth.assertThat(garden.getPlants().get(321L).getPlantState())
                .isEqualTo(PlantState.GROUND);
        Truth.assertThat(garden.getPlants().get(321L).isHasSunLight())
                .isEqualTo(true);
        Truth.assertThat(garden.getPlants().get(321L).isHasCompost())
                .isEqualTo(true);
        Truth.assertThat(garden.getPlants().get(321L).getLastTimeWater())
                .isEqualTo(true);
        Truth.assertThat(garden.getPlants().get(321L).getLastTimeAbono())
                .isEqualTo(321L);
        Truth.assertThat(garden.getPlants().get(321L).getPlantType())
                .isEqualTo(PlantType.STRAWBERRY);

        //SEED
        Truth.assertThat(garden.getPlants().get(321L).getPlantId())
                .isEqualTo(321L);
        Truth.assertThat(garden.getPlants().get(321L).getPlantState())
                .isEqualTo(PlantState.SEED);
        Truth.assertThat(garden.getPlants().get(321L).getLastTimeWater())
                .isEqualTo(true);
        Truth.assertThat(garden.getPlants().get(321L).getLastTimeAbono())
                .isEqualTo(321L);

        //PLANT
        Truth.assertThat(garden.getPlants().get(321L).getPlantId())
                .isEqualTo(321L);
        Truth.assertThat(garden.getPlants().get(321L).getPlantState())
                .isEqualTo(PlantState.PLANT);
        Truth.assertThat(garden.getPlants().get(321L).getLastTimeWater())
                .isEqualTo(true);
        Truth.assertThat(garden.getPlants().get(321L).getLastTimeAbono())
                .isEqualTo(321L);

        //FRUIT PLANT
        Truth.assertThat(garden.getPlants().get(321L).getPlantId())
                .isEqualTo(321L);
        Truth.assertThat(garden.getPlants().get(321L).getPlantState())
                .isEqualTo(PlantState.FRUIT_PLANT);
        Truth.assertThat(garden.getPlants().get(321L).getLastTimeWater())
                .isEqualTo(true);
        Truth.assertThat(garden.getPlants().get(321L).getLastTimeAbono())
                .isEqualTo(321L);


    }
}
