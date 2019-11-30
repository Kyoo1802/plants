package com.gaby.plants.view;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gaby.plants.R;
import com.gaby.plants.viewmodel.GardenViewModel;
import com.gaby.plants.viewmodel.SeleccionPlanta;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.List;
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

        final GardenViewModel vm = ViewModelProviders.of(this.getActivity()).get(GardenViewModel.class);
        SeleccionPlanta seleccionPlanta = vm.getSeleccion();

        Timer timer = new Timer();
        final FragmentActivity myActivity = this.getActivity();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> showArcore(myActivity));
            }
        }, 1000);
    }

    private static void showArcore(FragmentActivity myActivity) {
        FragmentTransaction ft = myActivity.getSupportFragmentManager().beginTransaction();
        List<Fragment> fragmentList = myActivity.getSupportFragmentManager().getFragments();

        boolean arFragmentExist = false;
        for(Fragment f : fragmentList){
            if(f instanceof FragmentArcore){
                ft.show(f);
                arFragmentExist = true;
            } else {
                ft.hide(f);
            }
        }
        if(!arFragmentExist) {
            FragmentArcore arFragment = new FragmentArcore();
            ft.add(R.id.frameFragment, arFragment);
        }
        ft.commit();
    }
}
