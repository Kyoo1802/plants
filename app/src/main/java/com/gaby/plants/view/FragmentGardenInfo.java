package com.gaby.plants.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaby.plants.R;
import com.gaby.plants.model.Plant;

import lombok.Getter;
import lombok.Setter;

public class FragmentGardenInfo extends Fragment {

    @Getter
    @Setter
    private boolean shown;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shown = false;
        return inflater.inflate(R.layout.fragment_garden_info, container, false);
    }

    public void setPlant(Plant selectedPlant) {
        ImageView imgPlant = this.getView().findViewById(R.id.imgPlant);
        TextView txtPlantName = this.getView().findViewById(R.id.txtPlantName);
        TextView txtPlantDesc = this.getView().findViewById(R.id.txtPlantDesc);
        switch (selectedPlant.getPlantType()) {
            case SUNFLOWER:
                txtPlantName.setText(R.string.girasol_title);
                txtPlantDesc.setText(R.string.girasol);
                imgPlant.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.i_girasoles));
                break;
            case CORN:
                txtPlantName.setText(R.string.maiz_title);
                txtPlantDesc.setText(R.string.maiz);
                imgPlant.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.i_elotes));
                break;
            case CALADIO:
                txtPlantName.setText(R.string.caladium_title);
                txtPlantDesc.setText(R.string.caladium);
                imgPlant.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.i_caladios));
                break;
            case HELECHOS:
                txtPlantName.setText(R.string.helechos_title);
                txtPlantDesc.setText(R.string.helechos);
                imgPlant.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.i_helechos));
                break;
            case MARGARITA:
                txtPlantName.setText(R.string.margaritas_title);
                txtPlantDesc.setText(R.string.margarita);
                imgPlant.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.i_margaritas));
                break;
            case ALOEVERA:
                txtPlantName.setText(R.string.aloevera_title);
                txtPlantDesc.setText(R.string.aloevera);
                imgPlant.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.i_aloeveras));
                break;
        }
    }
}
