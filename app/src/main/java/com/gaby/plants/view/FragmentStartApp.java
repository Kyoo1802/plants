package com.gaby.plants.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gaby.plants.R;
import com.gaby.plants.viewmodel.GardenViewModel;

public class FragmentStartApp extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_app, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button btnStart = this.getView().findViewById(R.id.btnStart);
        btnStart.setAlpha(0);
        btnStart.setOnClickListener(v -> startPlants(v));

        ImageView logoPlants = this.getView().findViewById(R.id.logoPlants);
        logoPlants.setScaleX(0);
        logoPlants.setScaleY(0);

        animate();
    }

    private void animate() {
        // Button start Animation
        Button btnStart = this.getView().findViewById(R.id.btnStart);
        ObjectAnimator btnAnimation = ObjectAnimator.ofFloat(btnStart, View.ALPHA, 1f);
        btnAnimation.setDuration(1000);

        // Scale logo plants Animation
        ImageView logoPlants = this.getView().findViewById(R.id.logoPlants);

        ObjectAnimator logoPlantScaleX = ObjectAnimator.ofFloat(logoPlants, View.SCALE_X, 1f);
        logoPlantScaleX.setInterpolator(new OvershootInterpolator());
        logoPlantScaleX.setDuration(1000);

        ObjectAnimator logoPlantScaleY = ObjectAnimator.ofFloat(logoPlants, View.SCALE_Y, 1f);
        logoPlantScaleY.setInterpolator(new OvershootInterpolator());
        logoPlantScaleY.setDuration(1000);

        AnimatorSet animator = new AnimatorSet();
        animator.play(logoPlantScaleX).after(100);
        animator.play(logoPlantScaleX).with(logoPlantScaleY);
        animator.play(logoPlantScaleX).before(btnAnimation);
        animator.play(btnAnimation).after(1000);

        final FragmentActivity myActivity = this.getActivity();
        final View myView = this.getView();
        animator.addListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        final GardenViewModel vm = ViewModelProviders.of(myActivity).get(GardenViewModel.class);
                        String gardenInfo = vm.retrieveGardenInfo();
                        if (!gardenInfo.isEmpty()) {
                            EditText gardenTextView = myView.findViewById(R.id.gardenInfoView);
                            gardenTextView.setText("Garden text: " + gardenInfo);
                            ImageView logoPlants = myView.findViewById(R.id.logoPlants);
                            logoPlants.setVisibility(View.GONE);
                            gardenTextView.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
        animator.start();
    }

    private void startPlants(View v) {
        Button btnStart = this.getView().findViewById(R.id.btnStart);

        final FragmentActivity myActivity = this.getActivity();
        btnStart.animate()
                .alpha(0)
                .setDuration(100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        FragmentTransaction ft = myActivity.getSupportFragmentManager().beginTransaction();
                        FragmentSelectPlant fragmentSelectPlant = new FragmentSelectPlant();
                        ft.replace(R.id.frameFragment, fragmentSelectPlant);
                        ft.commit();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }
}
