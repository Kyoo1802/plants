package com.gaby.plants;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pone un layout en la pantalla
        setContentView(R.layout.selected_plant_activity);
    }
}
