package com.gaby.plants.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.gaby.plants.R;
import com.gaby.plants.model.Plant;
import com.gaby.plants.viewmodel.GardenViewModel;

public class FragmentBotonesPrincipales extends Fragment {
    private Plant plant = Plant.builder().build();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_botones_principales, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("On onCreateView....");


        ImageButton imageBtnAddWater = this.getView().findViewById(R.id.ImageBtnAddWater);
        ImageButton imageBtnAddAbono = this.getView().findViewById(R.id.imageBtnAddAbono);


        imageBtnAddWater.setOnClickListener(v -> addWater(v));

        imageBtnAddAbono.setOnClickListener(v -> addAbono(v));
    }

    private void addWater(View v) {
        System.out.println("¡Presionaste agua!");
        // VM Observer
        final GardenViewModel vm = ViewModelProviders.of(this.getActivity()).get(GardenViewModel.class);
        vm.onTapBtnAddWater(plant.getPlantId());
    }

    private void addAbono(View v) {
        System.out.println("¡Elegiste abono!");
        final GardenViewModel vm = ViewModelProviders.of(this.getActivity()).get(GardenViewModel.class);
        vm.onTapBtnAddAbono(plant.getPlantId());
    }

}