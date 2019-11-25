package com.gaby.plants;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.gaby.plants.model.Plant;
import com.gaby.plants.viewmodel.GardenViewModel;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pone un layout en la pantalla
        setContentView(R.layout.main_activity);

        // VM Observer
        final GardenViewModel vm = ViewModelProviders.of(this).get(GardenViewModel.class);
        vm.listPlants().observe(this, new Observer<Collection<Plant>>() {
            @Override
            public void onChanged(@Nullable Collection<Plant> plants) {
                System.out.println("Plants has changed");
                for (Plant p : plants) {
                    System.out.println("Plant:" + p.getPlantType() + " " + p.getDateOfBirth());
                }
            }
        });

        // Button events
        Button buttonABC = findViewById(R.id.buttonABC);
        buttonABC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(v);
            }
        });


        Button buttonCDE = findViewById(R.id.buttonCDE);
        buttonCDE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.onTapAddStrawberry();
            }
        });
    }

    public void changeFragment(View view) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameFragment);

        if (currentFragment == null || currentFragment instanceof SelectPlantFragment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentStartApp ff2 = new FragmentStartApp();
            ft.replace(R.id.frameFragment, ff2);
            ft.commit();
        }

        if (currentFragment instanceof FragmentStartApp) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            SelectPlantFragment ff3 = new SelectPlantFragment();
            ft.replace(R.id.frameFragment, ff3);
            ft.commit();
        }
    }
}
