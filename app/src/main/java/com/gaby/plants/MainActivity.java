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

        Button buttonChangeFragment = findViewById(R.id.buttonChangeFragment);

        buttonChangeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(v);
            }
        });
    }

    public void changeFragment(View view) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameFragment);

        if (currentFragment == null || currentFragment instanceof FragmentPrepGround) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentOpenApp ff2 = new FragmentOpenApp();
            ft.replace(R.id.frameFragment, ff2);
            ft.commit();
        }

        if (currentFragment instanceof FragmentOpenApp) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentStartApp ff3 = new FragmentStartApp();
            ft.replace(R.id.frameFragment, ff3);
            ft.commit();
        }

        if (currentFragment instanceof FragmentStartApp) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentSelectPlant ff4 = new FragmentSelectPlant();
            ft.replace(R.id.frameFragment, ff4);
            ft.commit();
        }

        if (currentFragment instanceof FragmentSelectPlant) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentPrepGround ff5 = new FragmentPrepGround();
            ft.replace(R.id.frameFragment, ff5);
            ft.commit();
        }
    }
}
