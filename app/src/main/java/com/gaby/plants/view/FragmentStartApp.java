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
        btnStart.setScaleX(0);
        btnStart.setScaleY(0);
        btnStart.setOnClickListener(v -> startPlants(v));

        ImageView title = this.getView().findViewById(R.id.titlePlants);
        title.setScaleX(0);
        title.setScaleY(0);

        animate();
    }

    private void animate() {
        // Scale logo plants Animation
        ImageView title = this.getView().findViewById(R.id.titlePlants);
        ObjectAnimator textPlantScaleX = ObjectAnimator.ofFloat(title, View.SCALE_X, 1f);
        textPlantScaleX.setInterpolator(new OvershootInterpolator());
        textPlantScaleX.setDuration(1500);

        ObjectAnimator textPlantScaleY = ObjectAnimator.ofFloat(title, View.SCALE_Y, 1f);
        textPlantScaleY.setInterpolator(new OvershootInterpolator());
        textPlantScaleY.setDuration(1500);

        // Button start Animation
        Button btnStart = this.getView().findViewById(R.id.btnStart);
        ObjectAnimator btnStartScaleX = ObjectAnimator.ofFloat(btnStart, View.SCALE_X, 1f);
        btnStartScaleX.setInterpolator(new OvershootInterpolator());
        btnStartScaleX.setDuration(1500);

        ObjectAnimator btnStartScaleY = ObjectAnimator.ofFloat(btnStart, btnStart.SCALE_Y, 1f);
        btnStartScaleY.setInterpolator(new OvershootInterpolator());
        btnStartScaleY.setDuration(1500);

        AnimatorSet animator = new AnimatorSet();
        animator.play(textPlantScaleX).after(500);
        animator.play(textPlantScaleX).with(textPlantScaleY);
        animator.play(btnStartScaleX).after(800);
        animator.play(btnStartScaleX).with(btnStartScaleY);

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
                        FragmentUtils.replaceFragment(myActivity, new FragmentMenuOpciones());
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
