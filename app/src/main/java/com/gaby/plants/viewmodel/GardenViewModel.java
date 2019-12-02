package com.gaby.plants.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.gaby.plants.model.Garden;
import com.gaby.plants.model.GardenService;
import com.gaby.plants.model.Plant;
import com.gaby.plants.model.PlantState;
import com.gaby.plants.model.PlantType;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import lombok.Getter;


public class GardenViewModel extends AndroidViewModel {
    private GardenService gardenService;
    private int id = 0;

    @Getter
    private long newPlantId;
    @Getter
    private boolean hasShownGround;
    @Getter
    private boolean hasShownCompost;

    private MutableLiveData<Plant> updatedPlant;

    public GardenViewModel(@NonNull Application application) {
        super(application);
        gardenService = new GardenService(new Garden(), new MutableLiveData<>());
        updatedPlant = new MutableLiveData<>();
    }

    public void onTapAddStrawberry() {
        Plant newPlant = createPlant(PlantType.SUNFLOWER);
        newPlantId = newPlant.getPlantId();
        gardenService.addPlant(newPlant);
    }

    public void onTapAddTomato() {
        Plant newPlant = createPlant(PlantType.CORN);
        newPlantId = newPlant.getPlantId();
        gardenService.addPlant(newPlant);
    }

    public void onTapBtnAddAbono(long id) {
        Optional<Plant> plant = gardenService.addAbono(id);
        if (plant.isPresent()) {
            postUpdatedPlant(plant.get());
        }
    }

    public void onTapBtnAddWater(long id) {
        Optional<Plant> plant = gardenService.addWater(id);
        if (plant.isPresent()) {
            postUpdatedPlant(plant.get());
        }
    }

    public LiveData<Collection<Plant>> listPlants() {
        return gardenService.getPlants();
    }

    public void postUpdatedPlant(Plant plant) {
        updatedPlant.postValue(plant);
    }

    public LiveData<Plant> updatedPlant() {
        return updatedPlant;
    }

    public String retrieveGardenInfo() {
        return gardenService.getGardenInformation();
    }

    public void changePrepGround(long id) {
        hasShownGround=true;
        gardenService.changePlantState(id, PlantState.PREP_GROUND);
    }

    public void changePrepCompost(long id) {
        hasShownCompost=true;
        gardenService.changePlantState(id, PlantState.PREP_COMPOST);
    }

    public void changeToSeed(long id) {
        gardenService.changePlantState(id, PlantState.SEED);
    }

    public void onCompleteAdjustLight(long plantId) {
        gardenService.adjustLightSun(plantId);
    }

    public String getPlantInfo(long plantId) {
        return gardenService.getPlantInformation(plantId);
    }

    public void onDeletePlant(long plantId) {
        gardenService.removePlant(plantId);
    }

    private Plant createPlant(PlantType strawberry) {
        Date currentTime = Calendar.getInstance().getTime();
        return Plant.builder()
                .plantId(id++)
                .dateOfBirth(currentTime.getTime())
                .plantType(strawberry)
                .build();
    }

    public Plant getNewPlant() {
        return gardenService.getPlant(newPlantId);
    }

    public Plant getPlant(long selectedPlantId) {
        return gardenService.getPlant(selectedPlantId);
    }
}
