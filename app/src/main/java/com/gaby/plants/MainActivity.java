package com.gaby.plants;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gaby.plants.model.Plant;
import com.gaby.plants.view.FragmentAddCompost;
import com.gaby.plants.view.FragmentBotonesPrincipales;
import com.gaby.plants.view.FragmentPrepGround;
import com.gaby.plants.view.FragmentSelectPlant;
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

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentStartApp fragmentStartApp = new FragmentStartApp();
        ft.replace(R.id.frameFragment, fragmentStartApp);
        ft.commit();
    }

    public void changeFragment(View view) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameFragment);

        if (currentFragment == null || currentFragment instanceof FragmentBotonesPrincipales) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentStartApp fragmentStartApp = new FragmentStartApp();
            ft.replace(R.id.frameFragment, fragmentStartApp);
            ft.commit();
        }


        if (currentFragment instanceof FragmentStartApp) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentSelectPlant fragmentSelectPlant = new FragmentSelectPlant();
            ft.replace(R.id.frameFragment, fragmentSelectPlant);
            ft.commit();
        }

        if (currentFragment instanceof FragmentSelectPlant) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentPrepGround fragmentPrepGround = new FragmentPrepGround();
            ft.replace(R.id.frameFragment, fragmentPrepGround);
            ft.commit();
        }

        if (currentFragment instanceof FragmentPrepGround) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentAddCompost fragmentAddCompost = new FragmentAddCompost();
            ft.replace(R.id.frameFragment, fragmentAddCompost);
            ft.commit();
        }

        if (currentFragment instanceof FragmentAddCompost) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentBotonesPrincipales fragmentBotonesPrincipales = new FragmentBotonesPrincipales();
            ft.replace(R.id.frameFragment, fragmentBotonesPrincipales);
            ft.commit();
        }
    }
}
