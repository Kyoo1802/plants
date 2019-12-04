package com.gaby.plants.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gaby.plants.R;
import com.gaby.plants.utils.FragmentUtils;

public class FragmentBotonesPrincipales extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.arview_botones_principales, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button btnAddNewPlant = this.getView().findViewById(R.id.btnAddNewPlant);
        btnAddNewPlant.setOnClickListener(v -> {
            FragmentUtils.replaceFragment(this.getActivity(), new FragmentMenuOpciones());
        });
    }
}