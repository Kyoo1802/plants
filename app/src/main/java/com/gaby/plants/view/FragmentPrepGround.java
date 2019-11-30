package com.gaby.plants.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaby.plants.R;
import com.gaby.plants.viewmodel.GardenViewModel;
import com.gaby.plants.viewmodel.SeleccionPlanta;

public class FragmentPrepGround extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preparando_tierra, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final GardenViewModel vm = ViewModelProviders.of(this.getActivity()).get(GardenViewModel.class);
        SeleccionPlanta seleccionPlanta = vm.getSeleccion();
    }
}
