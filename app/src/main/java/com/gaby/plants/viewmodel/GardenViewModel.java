package com.gaby.plants.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.gaby.plants.model.Garden;
import com.gaby.plants.model.GardenService;
import com.gaby.plants.model.Plant;
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
    private SeleccionPlanta seleccion;

    public GardenViewModel(@NonNull Application application) {
        super(application);
        gardenService = new GardenService(new Garden(), new MutableLiveData<>());
        seleccion = SeleccionPlanta.UNKNOWN;
    }

    public void onTapAddStrawberry() {
        System.out.println("¡View Model onTapAddStrawberry!");

        // Obtener el tiempo actual del celular.
        long currentTime = Calendar.getInstance().getTime().getTime();

        Plant plantStrawBerry = Plant.builder()
                .plantId(id++)
                .dateOfBirth(currentTime)
                .plantType(PlantType.STRAWBERRY)
                .build();
        gardenService.addPlant(plantStrawBerry);
    }

    public void onTapAddTomato() {
        Date currentTime = Calendar.getInstance().getTime();
        Plant plantTomato = Plant.builder()
                .plantId(id++)
                .dateOfBirth(currentTime.getTime())
                .plantType(PlantType.TOMATE)
                .build();
        gardenService.addPlant(plantTomato);
    }

    public void onCompleteAdjustLight(long id) {
        gardenService.adjustLightSun(id);
    }

    public void onTapBtnAddAbono(long id) {
        gardenService.addAbono(id);
    }


    public void onTapBtnAddWater(long id) {
        gardenService.addWater(id);
    }

    public void onSelectRemovePlant(long id) {
        gardenService.removePlant(id);
    }

    public LiveData<Collection<Plant>> listPlants() {
        return gardenService.getPlants();
    }

    public String retrieveGardenInfo() {
        return gardenService.getGardenInformation();
    }
}
