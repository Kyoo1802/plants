package com.gaby.plants.model;

import java.util.Collection;

public class GardenService {
    private Garden garden;

    public GardenService(Garden garden) {
        this.garden = garden;
    }

    public void addPlant(Plant plant) {
        plant = plant.toBuilder()
                .plantState(PlantState.GROUND)
                .lastTimeWater(plant.getDateOfBirth())
                .lastTimeAbono(plant.getDateOfBirth())
                .build();
        garden.getPlants().put(plant.getPlantId(), plant);
    }

    public void adjustLightSun(long plantId) {
        if (garden.getPlants().containsKey(plantId)) {
            Plant plant = garden.getPlants().get(plantId);

            Plant updatedPlant = plant.toBuilder().hasSunLight(true).build();
            garden.getPlants().put(updatedPlant.getPlantId(), updatedPlant);
        }
    }

    public void setCompost(long plantId) {
        if (garden.getPlants().containsKey(plantId)) {
            Plant plant = garden.getPlants().get(plantId);

            Plant compost = plant.toBuilder().hasCompost(true).build();
            garden.getPlants().put(compost.getPlantId(), compost);
        }
    }

    public void removePlant(long plantId) {
        garden.getPlants().remove(plantId);
    }

    public void addWater(long plantId) {
        if (garden.getPlants().containsKey(plantId)) {
            Plant plant = garden.getPlants().get(plantId);
            long horaActual = System.currentTimeMillis();
            Plant water = plant.toBuilder().lastTimeWater(horaActual).build();
            garden.getPlants().put(water.getPlantId(), water);

        }
    }

    public void addAbono(long plantId) {
        if (garden.getPlants().containsKey(plantId)) {
            Plant plant = garden.getPlants().get(plantId);
            long horaActual = System.currentTimeMillis();
            Plant abono = plant.toBuilder().lastTimeAbono(horaActual).build();
            garden.getPlants().put(abono.getPlantId(), abono);

        }
    }

    public String getGardenInformation() {
        String info = "";

        for (Plant plant : garden.getPlants().values()) {
            info += plant.getPlantId() + " : " + plant.getPlantState() + "\n";
        }

        return info;
    }

    public String getPlantInformation(long plantId) {
        if (garden.getPlants().containsKey(plantId)) {
            Plant plant = garden.getPlants().get(plantId);
            return plant.getPlantId() + " : " + plant.getPlantState() + " " + plant.getPlantType() + "\n";
        }
        return "";
    }

    public void collectFruits(long plantId) {
        if (garden.getPlants().containsKey(plantId)) {
            Plant plant = garden.getPlants().get(plantId);
            if (plant.getPlantState() == PlantState.FRUIT_PLANT) {
                Plant collectFruits = plant.toBuilder().plantState(PlantState.PLANT).build();
                garden.getPlants().put(collectFruits.getPlantId(), collectFruits);
            }
        }
    }

    public Collection<Plant> listPlants() {
        return garden.getPlants().values();
    }

    public void changePlantState(long plantId, PlantState plantState) {
        if (garden.getPlants().containsKey(plantId)) {
            Plant plant = garden.getPlants().get(plantId);

            Plant compost = plant.toBuilder().plantState(plantState).build();
            garden.getPlants().put(compost.getPlantId(), compost);
        }
    }
}
