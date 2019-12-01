package com.gaby.plants.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.Collection;
import java.util.Optional;

public class GardenService {
    private Garden garden;

    // Canal de comunicacion con la vista.
    // (todo lo que quiero que la vista pueda ser notificado cuando hay cambios)
    private MutableLiveData<Collection<Plant>> plants;


    public GardenService(Garden garden, MutableLiveData<Collection<Plant>> plants) {
        this.garden = garden;
        this.plants = plants;
    }

    public LiveData<Collection<Plant>> getPlants() {
        return plants;
    }

    public void addPlant(Plant plant) {
        System.out.println("¡GardenService.addPlant() !");
        plant = plant.toBuilder()
                .plantState(PlantState.UNSPECIFIED)
                .lastTimeWater(plant.getDateOfBirth())
                .lastTimeAbono(plant.getDateOfBirth())
                .build();
        garden.getPlants().put(plant.getPlantId(), plant);

        System.out.println("¡Se manda la notificacion a los observers!");
        // Manda la notificacion a cualquier camara de los View
        plants.postValue(garden.getPlants().values());
    }

    public void adjustLightSun(long plantId) {
        if (garden.getPlants().containsKey(plantId)) {
            Plant plant = garden.getPlants().get(plantId);
            Plant updatedPlant = plant.toBuilder().hasSunLight(true).build();
            garden.getPlants().put(updatedPlant.getPlantId(), updatedPlant);
            plants.postValue(garden.getPlants().values());
        }
    }

    public void setCompost(long plantId) {
        if (garden.getPlants().containsKey(plantId)) {
            Plant plant = garden.getPlants().get(plantId);

            Plant compost = plant.toBuilder().hasCompost(true).build();
            garden.getPlants().put(compost.getPlantId(), compost);
            plants.postValue(garden.getPlants().values());
        }
    }

    public void removePlant(long plantId) {
        garden.getPlants().remove(plantId);

        // Manda la notificacion a cualquier camara de los View
        plants.postValue(garden.getPlants().values());
    }

    public Optional<Plant> addWater(long plantId) {
        if (garden.getPlants().containsKey(plantId)) {
            Plant plant = garden.getPlants().get(plantId);
            long horaActual = System.currentTimeMillis();
            int newPercentage = Math.min(100, plant.getAbonoPercentage() + 5);
            Plant water = plant.toBuilder().waterPercentage(newPercentage).lastTimeWater(horaActual).build();
            garden.getPlants().put(water.getPlantId(), water);
            return Optional.of(plant);
        }
        return Optional.empty();
    }

    public Optional<Plant> addAbono(long plantId) {
        if (garden.getPlants().containsKey(plantId)) {
            Plant plant = garden.getPlants().get(plantId);
            long horaActual = System.currentTimeMillis();
            int newPercentage = Math.min(100, plant.getAbonoPercentage() + 5);
            Plant abono = plant.toBuilder().abonoPercentage(newPercentage).lastTimeAbono(horaActual).build();
            garden.getPlants().put(abono.getPlantId(), abono);
            plants.postValue(garden.getPlants().values());
            return Optional.of(plant);
        }
        return Optional.empty();
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
