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

import lombok.Getter;
import lombok.Setter;

public class GardenViewModel extends AndroidViewModel {
    private GardenService gardenService;
    private int id = 0;

    @Getter
    @Setter
    private Plant newPlant;

    private LiveData<Plant> updatedPlant;

    public GardenViewModel(@NonNull Application application) {
        super(application);
        gardenService = new GardenService(new Garden(), new MutableLiveData<>());
        updatedPlant = new MutableLiveData<>();
    }

    public void onTapAddStrawberry() {
        newPlant = createPlant(PlantType.STRAWBERRY);
        gardenService.addPlant(newPlant);
    }

    public void onTapAddTomato() {
        newPlant = createPlant(PlantType.TOMATOE);
        gardenService.addPlant(newPlant);
    }

    public void onTapBtnAddAbono(long id) {
        gardenService.addAbono(id);
    }

    public void onTapBtnAddWater(long id) {
        gardenService.addWater(id);
    }

    public LiveData<Collection<Plant>> listPlants() {
        return gardenService.getPlants();
    }
    public LiveData<Plant> updatedPlant() {
        return updatedPlant;
    }

    public String retrieveGardenInfo() {
        return gardenService.getGardenInformation();
    }

    public void changeToSeed(long id) {
        gardenService.changePlantState(id, PlantState.SEED);
    }

    public void changePrepGround(long id) {
        gardenService.changePlantState(id, PlantState.PREP_GROUND);
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
}
