package com.gaby.plants.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gaby.plants.R;
import com.gaby.plants.viewmodel.GardenViewModel;

public class FragmentSelectPlant extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_opciones, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("On onCreateView....");


        Button btnStrawberry = this.getView().findViewById(R.id.btnStrawberry);
        Button btnTomato = this.getView().findViewById(R.id.btnTomato);

        btnStrawberry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStrawberry(v);
            }

        });

        btnTomato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTomato(v);
            }

        });
    }

    private void selectStrawberry(View v) {
        FragmentTransaction ft =this.getActivity().getSupportFragmentManager().beginTransaction();
        FragmentSelectedPlant fragmentStartApp = new FragmentSelectedPlant();
        ft.replace(R.id.frameFragment, fragmentStartApp);
        ft.commit();
    }

    private void selectTomato(View v) {
        System.out.println("¡Elegiste Tomate!");
        final GardenViewModel vm = ViewModelProviders.of(this.getActivity()).get(GardenViewModel.class);
        vm.onTapAddTomato();
    }

}
