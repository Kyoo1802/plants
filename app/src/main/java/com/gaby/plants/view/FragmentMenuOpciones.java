package com.gaby.plants.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaby.plants.R;
import com.gaby.plants.model.PlantType;
import com.gaby.plants.utils.FragmentUtils;
import com.gaby.plants.viewmodel.GardenViewModel;

import lombok.Getter;

public class FragmentMenuOpciones extends Fragment {
    private PlantCell[] plantCells = {
            new PlantCell(PlantType.CORN),
            new PlantCell(PlantType.SUNFLOWER),
            new PlantCell(PlantType.ALOEVERA),
            new PlantCell(PlantType.HELECHOS),
            new PlantCell(PlantType.MARGARITA),
            new PlantCell(PlantType.CALADIO),
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_opciones, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GridView gridPlants = this.getView().findViewById(R.id.gridPlants);
        PlantsAdapter plantsAdapter = new PlantsAdapter(this.getActivity(), plantCells);
        gridPlants.setAdapter(plantsAdapter);

        gridPlants.setOnItemClickListener((parent, view, position, id) -> {
            PlantCell plantCell = plantCells[position];
            GardenViewModel vm = ViewModelProviders.of(this.getActivity()).get(GardenViewModel.class);
            switch (plantCell.getPlantType()) {
                case UNSPECIFIED:
                    break;
                case SUNFLOWER:
                    vm.onTapAddSunflower();
                    break;
                case CORN:
                    vm.onTapAddCorn();
                    break;
                case CALADIO:
                    vm.onTapAddCaladio();
                    break;
                case HELECHOS:
                    vm.onTapAddHelechos();
                    break;
                case MARGARITA:
                    vm.onTapAddMargarita();
                    break;
                case ALOEVERA:
                    vm.onTapAddAloeVera();
                    break;
                default:
                    return;
            }
            switchFragment(vm);
        });
    }

    private void switchFragment(GardenViewModel vm) {
        if (vm.isHasShownGround() && vm.isHasShownCompost()) {
            vm.changePrepCompost(vm.getNewPlantId());
            FragmentUtils.showArcore(this.getActivity());
        } else if (!vm.isHasShownGround()) {
            vm.changePrepGround(vm.getNewPlantId());
            FragmentUtils.replaceFragment(this.getActivity(), new FragmentPrepGround());
        } else {
            FragmentUtils.replaceFragment(this.getActivity(), new FragmentPrepCompost());
        }
    }

    static class PlantsAdapter extends BaseAdapter {

        private final Context mContext;
        private final PlantCell[] plantCells;

        public PlantsAdapter(Context context, PlantCell[] plantCells) {
            this.mContext = context;
            this.plantCells = plantCells;
        }

        @Override
        public int getCount() {
            return plantCells.length;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final PlantCell plantCell = plantCells[position];

            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                convertView = layoutInflater.inflate(R.layout.view_plant_menu, null);
                convertView.setScaleX(0f);
                convertView.setScaleY(0f);

                ImageView imgPlant = convertView.findViewById(R.id.imgPlant2);
                TextView textPlant = convertView.findViewById(R.id.textPlant2);
                convertView.animate()
                        .setStartDelay(position * 100)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setInterpolator(new OvershootInterpolator())
                        .setDuration(1000);

                ViewHolder viewHolder = new ViewHolder(imgPlant, textPlant);
                convertView.setTag(viewHolder);
            }

            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.imgPlant.setImageResource(getResource(plantCell.getPlantType()));
            viewHolder.textPlant.setText(plantCell.getPlantType().toString());
            return convertView;
        }

        private int getResource(PlantType plantType) {
            switch (plantType) {
                case SUNFLOWER:
                    return R.drawable.girasol;
                case CORN:
                    return R.drawable.corn;
                case HELECHOS:
                    return R.drawable.helechos;
                case CALADIO:
                    return R.drawable.caladio;
                case MARGARITA:
                    return R.drawable.margarita;
                case ALOEVERA:
                    return R.drawable.aloevera2;
                default:
                    return -1;
            }
        }

        private class ViewHolder {
            private final ImageView imgPlant;
            private final TextView textPlant;

            public ViewHolder(ImageView imgPlant, TextView textPlant) {
                this.imgPlant = imgPlant;
                this.textPlant = textPlant;
            }
        }

    }

    static class PlantCell {
        @Getter
        private PlantType plantType;

        public PlantCell(PlantType plantType) {
            this.plantType = plantType;
        }
    }
}
