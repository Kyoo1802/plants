package com.gaby.plants.model;

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

    public void selectPlant(long plantId) {
        throw new UnsupportedOperationException();
    }

    public String getPlantInformation(String plantId) {
        throw new UnsupportedOperationException();
    }

    public Garden getGardenerInformation() {
        throw new UnsupportedOperationException();
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
        //throw new UnsupportedOperationException();
    }

    public void addWater(long plantId) {
        if (garden.getPlants().containsKey(plantId)) {
            Plant plant = garden.getPlants().get(plantId);
            long horaActual = System.currentTimeMillis();
            Plant water = plant.toBuilder().lastTimeWater(horaActual).build();
            garden.getPlants().put(water.getPlantId(), water);

        }

        //throw new UnsupportedOperationException();
    }

    public void removePlant(long plantId) {
        throw new UnsupportedOperationException();
    }

    public void collectFruits(long plantId) {
        throw new UnsupportedOperationException();
    }

    public Plant listPlants() {
        throw new UnsupportedOperationException();
    }

    public void changePlantState(PlantState plantState) {
        throw new UnsupportedOperationException();
    }

}
