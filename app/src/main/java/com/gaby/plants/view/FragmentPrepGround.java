package com.gaby.plants.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaby.plants.R;
import com.gaby.plants.model.Plant;
import com.gaby.plants.utils.FragmentUtils;
import com.gaby.plants.viewmodel.GardenViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class FragmentPrepGround extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preparando_tierra, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GardenViewModel vm = ViewModelProviders.of(this.getActivity()).get(GardenViewModel.class);
        Plant newPlant = vm.getNewPlant();
        vm.changePrepGround(newPlant.getPlantId());

        final FragmentActivity myActivity = this.getActivity();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Sends a request to the UI Thread to trigger a method.
                new Handler(Looper.getMainLooper()).post(() -> FragmentUtils.showArcore(myActivity));
            }
        }, 1000);
    }
}
