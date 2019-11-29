package com.gaby.plants.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.gaby.plants.R;
import com.gaby.plants.model.Plant;
import com.gaby.plants.viewmodel.GardenViewModel;

public class FragmentSelectedPlant extends Fragment {
    private Plant plant = Plant.builder().build();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_selected_plant, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("On onCreateView....");


        SeekBar seekBar = this.getView().findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                adjustLight(seekBar);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void adjustLight(SeekBar seekBar) {
        if (seekBar.getProgress() == 70) {
            final GardenViewModel vm = ViewModelProviders.of(this.getActivity()).get(GardenViewModel.class);
            vm.onCompleteAdjustLight(plant.getPlantId());
        }
    }
}