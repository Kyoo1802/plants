package com.gaby.plants;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gaby.plants.model.Plant;
import com.gaby.plants.utils.FragmentUtils;
import com.gaby.plants.view.FragmentStartApp;
import com.gaby.plants.viewmodel.GardenViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // VM Observer
        final GardenViewModel vm = ViewModelProviders.of(this).get(GardenViewModel.class);

        vm.listPlants().observe(this, plants -> {
            System.out.println("Notificacion recibida Plants has changed");
            for (Plant p : plants) {
                System.out.println("Plant:" + p.getPlantType() + " " + p.getDateOfBirth());
            }
        });

        FragmentUtils.replaceFragment(this, new FragmentStartApp());
    }
}
