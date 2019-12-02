package com.gaby.plants.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gaby.plants.R;
import com.gaby.plants.utils.FragmentUtils;
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

        Button btnStrawberry = this.getView().findViewById(R.id.btnStrawberry);
        btnStrawberry.setOnClickListener(v -> selectStrawberry(v));

        Button btnTomato = this.getView().findViewById(R.id.btnTomato);
        btnTomato.setOnClickListener(v -> selectTomato(v));
    }

    private void selectStrawberry(View v) {
        GardenViewModel vm = ViewModelProviders.of(this.getActivity()).get(GardenViewModel.class);
        vm.onTapAddStrawberry();

        switchFragment(vm);
    }

    private void selectTomato(View v) {
        GardenViewModel vm = ViewModelProviders.of(this.getActivity()).get(GardenViewModel.class);
        vm.onTapAddTomato();
        switchFragment(vm);
    }

    private void switchFragment(GardenViewModel vm) {
        if(vm.isHasShownGround() && vm.isHasShownCompost()){
            vm.changePrepCompost(vm.getNewPlantId());
            FragmentUtils.showArcore(this.getActivity());
        } else if (!vm.isHasShownGround()){
            vm.changePrepGround(vm.getNewPlantId());
            FragmentUtils.replaceFragment(this.getActivity(), new FragmentPrepGround());
        } else {
            FragmentUtils.replaceFragment(this.getActivity(), new FragmentPrepCompost());
        }
    }
}
