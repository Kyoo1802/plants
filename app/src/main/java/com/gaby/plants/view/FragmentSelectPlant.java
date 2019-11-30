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
import com.gaby.plants.viewmodel.SeleccionPlanta;

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

        Button btnStrawberry = this.getView().findViewById(R.id.btnStrawberry);
        Button btnTomato = this.getView().findViewById(R.id.btnTomato);

        btnStrawberry.setOnClickListener(v -> selectStrawberry(v));

        btnTomato.setOnClickListener(v -> selectTomato(v));
    }

    private void selectStrawberry(View v) {
        final GardenViewModel vm = ViewModelProviders.of(this.getActivity()).get(GardenViewModel.class);
        vm.setSeleccion(SeleccionPlanta.STRAWBERRY);


        FragmentTransaction ft = this.getActivity().getSupportFragmentManager().beginTransaction();
        FragmentPrepGround fragmentStartApp = new FragmentPrepGround();
        ft.replace(R.id.frameFragment, fragmentStartApp);
        ft.commit();
    }

    private void selectTomato(View v) {
        final GardenViewModel vm = ViewModelProviders.of(this.getActivity()).get(GardenViewModel.class);
        vm.setSeleccion(SeleccionPlanta.TOMATO);

        FragmentTransaction ft = this.getActivity().getSupportFragmentManager().beginTransaction();
        FragmentPrepGround fragmentStartApp = new FragmentPrepGround();
        ft.replace(R.id.frameFragment, fragmentStartApp);
        ft.commit();
    }

}
