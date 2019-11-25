package com.gaby.plants;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentStartApp extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment...
        System.out.println("On onCreateView....");
        return inflater.inflate(R.layout.fragment_start_app, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        System.out.println("On onCreateView....");


        Button btnStart = this.getView().findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlants(v);
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    private void startPlants(View v) {
        System.out.println("Empezando plants!!!");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("On resume....");
    }


    @Override
    public void onPause() {
        super.onPause();
        System.out.println("On pause....");
    }
}
