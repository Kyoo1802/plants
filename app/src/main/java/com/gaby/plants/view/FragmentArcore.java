package com.gaby.plants.view;

import android.app.Activity;
import android.app.ActivityManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.gaby.plants.R;
import com.gaby.plants.model.Plant;
import com.gaby.plants.viewmodel.GardenViewModel;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentArcore extends Fragment {
    private static final String TAG = "MainActivity";
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ModelRenderable plantRenderable;
    private ViewRenderable selectedPlantControl;
    private ViewRenderable addAbonoControl;
    private ViewRenderable addWaterControl;
    private ViewRenderable progressBarView;
    private List<Node> plantControlNodes = new LinkedList<>();

    private boolean runningAction = false;

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_arcore, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this.getActivity())) {
            return;
        }

        // Cargar el Modelo en memoria
        ModelRenderable.builder()
                .setSource(this.getActivity(), R.raw.strawberry_two)
                .build()
                .thenAccept(renderable -> {
                    Log.i(TAG, "Archivo cargado.");
                    plantRenderable = renderable;
                })
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Unable to load Renderable.", throwable);
                            return null;
                        });

        // Renders the selected plant view
        ViewRenderable.builder()
                .setView(this.getActivity(), R.layout.fragment_selected_plant)
                .build()
                .thenAccept(renderable -> {
                    Log.i(TAG, "Archivo cargado.");
                    selectedPlantControl = renderable;
                })
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Unable to load Renderable.", throwable);
                            return null;
                        });

        ViewRenderable.builder()
                .setView(this.getActivity(), R.layout.fragment_add_abono)
                .build()
                .thenAccept(renderable -> {
                    Log.i(TAG, "Archivo cargado.");
                    addAbonoControl = renderable;
                })
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Unable to load Renderable.", throwable);
                            return null;
                        });

        ViewRenderable.builder()
                .setView(this.getActivity(), R.layout.fragment_add_water)
                .build()
                .thenAccept(renderable -> {
                    Log.i(TAG, "Archivo cargado.");
                    addWaterControl = renderable;
                })
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Unable to load Renderable.", throwable);
                            return null;
                        });

        ViewRenderable.builder()
                .setView(this.getActivity(), R.layout.fragment_progress_bar)
                .build()
                .thenAccept(renderable -> {
                    Log.i(TAG, "Archivo cargado.");
                    progressBarView = renderable;
                })
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Unable to load Renderable.", throwable);
                            return null;
                        });

        ArFragment arFragment = (ArFragment) this.getChildFragmentManager().findFragmentById(R.id.arFragment);
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (plantRenderable == null || selectedPlantControl == null || addAbonoControl == null) {
                        return;
                    }

                    GardenViewModel vm = ViewModelProviders.of(this.getActivity()).get(GardenViewModel.class);
                    Plant plant = vm.getNewPlant();
                    vm.changeToSeed(plant.getPlantId());

                    // Create the Anchor.
                    AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    Node plantNode = new Node();
                    plantNode.setParent(anchorNode);
                    plantNode.setRenderable(plantRenderable);

                    Node actionsViewNode = new Node();
                    actionsViewNode.setParent(plantNode);
                    actionsViewNode.setLocalPosition(new Vector3(0.0f, 0.35f, 0.0f));
                    actionsViewNode.setRenderable(selectedPlantControl);
                    actionsViewNode.setEnabled(false);
                    plantControlNodes.add(actionsViewNode);

                    Node abonoViewNode = new Node();
                    abonoViewNode.setParent(plantNode);
                    abonoViewNode.setLocalPosition(new Vector3(0.45f, 0.00f, 0.00f));
                    abonoViewNode.setRenderable(addAbonoControl);
                    abonoViewNode.setEnabled(false);
                    plantControlNodes.add(abonoViewNode);

                    Node waterViewNode = new Node();
                    waterViewNode.setParent(plantNode);
                    waterViewNode.setLocalPosition(new Vector3(0.20f, 0.00f, 0.00f));
                    waterViewNode.setRenderable(addWaterControl);
                    waterViewNode.setEnabled(false);
                    plantControlNodes.add(waterViewNode);

                    Node infoViewNode = new Node();
                    infoViewNode.setParent(plantNode);
                    infoViewNode.setLocalPosition(new Vector3(-0.45f, 0.00f, 0.00f));
                    infoViewNode.setRenderable(addAbonoControl);
                    infoViewNode.setEnabled(false);
                    plantControlNodes.add(infoViewNode);

                    Node progressViewNode = new Node();
                    progressViewNode.setParent(plantNode);
                    progressViewNode.setLocalPosition(new Vector3(-0.45f, 0.10f, 0.00f));
                    progressViewNode.setRenderable(progressBarView);
                    progressViewNode.setEnabled(false);
                    plantControlNodes.add(progressViewNode);

                    vm.updatedPlant().observe(this.getActivity(), plantUpdated -> {
                        if (plantUpdated.getPlantId() == plant.getPlantId()) {
//                            plantNode.setRenderable(addAbonoControl);
                            ProgressBar pBarWater = progressBarView.getView().findViewById(R.id.progressBarWater);
                            pBarWater.setProgress(plantUpdated.getWaterPercentage());
                            ProgressBar pBarAbono = progressBarView.getView().findViewById(R.id.progressBarAbono);
                            pBarAbono.setProgress(plantUpdated.getAbonoPercentage());
                        }
                    });

                    plantNode.setOnTapListener((hitTestResult, motionEvent1) -> {
                        boolean newEnabledState = !actionsViewNode.isEnabled();
                        hideAllControls();
                        actionsViewNode.setEnabled(newEnabledState);
                        progressViewNode.setEnabled(newEnabledState);
                        if (newEnabledState) {
                            ImageButton btnSun = selectedPlantControl.getView().findViewById(R.id.btnSun);
                            ImageButton btnAddAbono = selectedPlantControl.getView().findViewById(R.id.btnAddAbono);
                            ImageButton btnAddWater = selectedPlantControl.getView().findViewById(R.id.btnAddWater);
                            ImageButton btnInfo = selectedPlantControl.getView().findViewById(R.id.btnInfo);
                            ImageButton btnDelete = selectedPlantControl.getView().findViewById(R.id.btnDelete);
                            SeekBar seekBarSunAdjust = selectedPlantControl.getView().findViewById(R.id.seekBar);
                            seekBarSunAdjust.setVisibility(View.GONE);

                            // Button Add Abono configuration
                            btnAddAbono.setOnClickListener(view -> {
                                if (!abonoViewNode.isEnabled()) {
                                    vm.onTapBtnAddAbono(plant.getPlantId());
                                    abonoViewNode.setEnabled(true);
                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            // Sends a request to the UI Thread to trigger a method.
                                            new Handler(Looper.getMainLooper()).post(() -> abonoViewNode.setEnabled(false));
                                        }
                                    }, 1000);
                                }
                            });

                            // Button Add Water configuration
                            btnAddWater.setOnClickListener(view -> {
                                if (!waterViewNode.isEnabled()) {
                                    vm.onTapBtnAddWater(plant.getPlantId());
                                    waterViewNode.setEnabled(true);
                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            // Sends a request to the UI Thread to trigger a method.
                                            new Handler(Looper.getMainLooper()).post(() -> waterViewNode.setEnabled(false));
                                        }
                                    }, 1000);
                                }
                            });

                            // Button View Info configuration
                            btnInfo.setOnClickListener(view -> {
                                vm.getPlantInfo(plant.getPlantId());
                                infoViewNode.setEnabled(!infoViewNode.isEnabled());
                            });


                            // Button View Info configuration
                            btnDelete.setOnClickListener(view -> {
                                vm.onDeletePlant(plant.getPlantId());
                                hideAllControls();
                                anchorNode.removeChild(plantNode);
                            });

                            btnSun.setOnClickListener(view -> {
                                seekBarSunAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {
                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {
                                        if (seekBar.getProgress() >= plant.getCorrectSunAmount()) {
                                            vm.onCompleteAdjustLight(plant.getPlantId());
                                            seekBarSunAdjust.setVisibility(View.GONE);
                                            showPlantActionBtns(true);
                                        }
                                    }
                                });
                                seekBarSunAdjust.setVisibility(View.VISIBLE);
                                seekBarSunAdjust.setAlpha(0);
                                seekBarSunAdjust.animate()
                                        .alpha(1)
                                        .setDuration(1000);
                            });

                            showPlantActionBtns(plant.isHasSunLight());
                        }
                    });

                });

        Timer growTimer = new Timer();
        growTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Sends a request to the UI Thread to trigger a method.
                new Handler(Looper.getMainLooper()).post(() -> {
                });
            }
        }, 1000);
    }

    private void showPlantActionBtns(boolean show) {
        ImageButton btnAddAbono = selectedPlantControl.getView().findViewById(R.id.btnAddAbono);
        ImageButton btnAddWater = selectedPlantControl.getView().findViewById(R.id.btnAddWater);
        ImageButton btnSun = selectedPlantControl.getView().findViewById(R.id.btnSun);
        if (show) {
            btnSun.setVisibility(View.GONE);

            btnAddAbono.setAlpha(0f);
            btnAddAbono.setVisibility(View.VISIBLE);
            btnAddAbono.animate()
                    .alpha(1)
                    .setDuration(1000);

            btnAddWater.setAlpha(0f);
            btnAddWater.setVisibility(View.VISIBLE);
            btnAddWater.animate()
                    .alpha(1)
                    .setDuration(1000);
        } else {
            btnSun.setVisibility(View.VISIBLE);
            btnAddAbono.setVisibility(View.GONE);
            btnAddWater.setVisibility(View.GONE);
        }
    }

    private void hideAllControls() {
        for (Node n : plantControlNodes) {
            n.setEnabled(false);
        }
    }
}