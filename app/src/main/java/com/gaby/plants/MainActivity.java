package com.gaby.plants;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pone un layout en la pantalla
        setContentView(R.layout.main_activity);

        Button buttonABC = findViewById(R.id.buttonABC);

        buttonABC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(v);
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
