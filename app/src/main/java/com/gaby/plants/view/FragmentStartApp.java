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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaby.plants.R;
import com.gaby.plants.utils.FragmentUtils;
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

        TextView textPlants = this.getView().findViewById(R.id.textPlants);
        textPlants.setScaleX(0);
        textPlants.setScaleY(0);

        animate();
    }

    private void animate() {
        // Button start Animation
        Button btnStart = this.getView().findViewById(R.id.btnStart);
        ObjectAnimator btnAnimation = ObjectAnimator.ofFloat(btnStart, View.ALPHA, 1f);
        btnAnimation.setDuration(1500);

        // Scale logo plants Animation
        ImageView logoPlants = this.getView().findViewById(R.id.logoPlants);

        TextView textPlants = this.getView().findViewById(R.id.textPlants);

        ObjectAnimator logoPlantScaleX = ObjectAnimator.ofFloat(logoPlants, View.SCALE_X, 1f);
        logoPlantScaleX.setInterpolator(new OvershootInterpolator());
        logoPlantScaleX.setDuration(1500);

        ObjectAnimator logoPlantScaleY = ObjectAnimator.ofFloat(logoPlants, View.SCALE_Y, 1f);
        logoPlantScaleY.setInterpolator(new OvershootInterpolator());
        logoPlantScaleY.setDuration(1500);

        ObjectAnimator textPlantScaleX = ObjectAnimator.ofFloat(textPlants, View.SCALE_X, 1f);
        textPlantScaleX.setInterpolator(new OvershootInterpolator());
        textPlantScaleX.setDuration(1500);

        ObjectAnimator textPlantScaleY = ObjectAnimator.ofFloat(textPlants, View.SCALE_Y, 1f);
        textPlantScaleY.setInterpolator(new OvershootInterpolator());
        textPlantScaleY.setDuration(1500);

        AnimatorSet animator = new AnimatorSet();
        animator.play(textPlantScaleX).after(800);
        animator.play(textPlantScaleX).with(textPlantScaleY);
        animator.play(textPlantScaleX).with(logoPlantScaleX);
        animator.play(logoPlantScaleX).with(logoPlantScaleY);
        animator.play(logoPlantScaleX).before(btnAnimation);
        animator.play(btnAnimation).after(800);

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
        FragmentActivity myActivity = this.getActivity();
        btnStart.animate()
                .alpha(0)
                .setDuration(100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        FragmentUtils.replaceFragment(myActivity, new FragmentSelectPlant());
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
