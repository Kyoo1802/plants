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
import com.gaby.plants.model.PlantState;
import com.gaby.plants.model.PlantType;
import com.gaby.plants.viewmodel.GardenViewModel;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

public class FragmentArcore extends Fragment {
    private static final String TAG = "MainActivity";
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ViewRenderable selectedPlantControl;
    private ViewRenderable addAbonoControl;
    private ViewRenderable addWaterControl;
    private ViewRenderable progressBarView;

    private boolean renderablesCompleted;

    Table<PlantType, PlantState, ModelRenderable> renderables = HashBasedTable.create();
    private List<Node> plantControlNodes = new LinkedList<>();

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

        CompletableFuture.allOf(
                // Cargar Modelos en memoria
                createModelRenderable(R.raw.strawberry_two, PlantType.SUNFLOWER, PlantState.SEED),
                createModelRenderable(R.raw.strawberry_two, PlantType.SUNFLOWER, PlantState.SPROUD),
                createModelRenderable(R.raw.strawberry_two, PlantType.SUNFLOWER, PlantState.PLANT),
                createModelRenderable(R.raw.strawberry_two, PlantType.SUNFLOWER, PlantState.FRUIT_PLANT),
                createModelRenderable(R.raw.strawberry_two, PlantType.CORN, PlantState.SEED),
                createModelRenderable(R.raw.strawberry_two, PlantType.CORN, PlantState.SPROUD),
                createModelRenderable(R.raw.strawberry_two, PlantType.CORN, PlantState.PLANT),
                createModelRenderable(R.raw.strawberry_two, PlantType.CORN, PlantState.FRUIT_PLANT),
                createModelRenderable(R.raw.strawberry_two, PlantType.BLUE, PlantState.SEED),
                createModelRenderable(R.raw.strawberry_two, PlantType.BLUE, PlantState.SPROUD),
                createModelRenderable(R.raw.strawberry_two, PlantType.BLUE, PlantState.PLANT),
                createModelRenderable(R.raw.strawberry_two, PlantType.BLUE, PlantState.FRUIT_PLANT),
                createModelRenderable(R.raw.strawberry_two, PlantType.GREEN, PlantState.SEED),
                createModelRenderable(R.raw.strawberry_two, PlantType.GREEN, PlantState.SPROUD),
                createModelRenderable(R.raw.strawberry_two, PlantType.GREEN, PlantState.PLANT),
                createModelRenderable(R.raw.strawberry_two, PlantType.GREEN, PlantState.FRUIT_PLANT),

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
                                }),
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
                                }),
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
                                }),
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
                                }))
                .handle(
                        (notUsed, throwable) -> {
                            renderablesCompleted = true;
                            return null;
                        });

        ArFragment arFragment = (ArFragment) this.getChildFragmentManager().findFragmentById(R.id.arFragment);
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (!renderablesCompleted) {
                        return;
                    }

                    GardenViewModel vm = ViewModelProviders.of(this.getActivity()).get(GardenViewModel.class);
                    Plant newPlant = vm.getNewPlant();
                    vm.changeToSeed(vm.getNewPlantId());

                    // Create the Anchor.
                    AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    Node plantNode = new Node();
                    plantNode.setParent(anchorNode);
                    plantNode.setRenderable(getRenderableFromPlant(newPlant.getPlantType(), newPlant.getPlantState()));

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

                    long newPlantId = newPlant.getPlantId();
                    vm.updatedPlant().observe(this.getActivity(), plantUpdated -> {
                        if (plantUpdated.getPlantId() == newPlantId) {
                            ProgressBar pBarWater = progressBarView.getView().findViewById(R.id.progressBarWater);
                            pBarWater.setProgress(plantUpdated.getWaterPercentage());

                            ProgressBar pBarAbono = progressBarView.getView().findViewById(R.id.progressBarAbono);
                            pBarAbono.setProgress(plantUpdated.getAbonoPercentage());

                            plantNode.setRenderable(getRenderableFromPlant(plantUpdated.getPlantType(), plantUpdated.getPlantState()));
                        }
                    });

                    plantNode.setOnTapListener((hitTestResult, motionEvent1) -> {
                        long selectedPlantId = newPlantId;
                        Plant selectedPlant = vm.getPlant(selectedPlantId);
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
                                    vm.onTapBtnAddAbono(selectedPlantId);
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
                                    vm.onTapBtnAddWater(selectedPlantId);
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
                                vm.getPlantInfo(selectedPlantId);
                                infoViewNode.setEnabled(!infoViewNode.isEnabled());
                            });


                            // Button View Info configuration
                            btnDelete.setOnClickListener(view -> {
                                vm.onDeletePlant(selectedPlantId);
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
                                        if (seekBar.getProgress() >= selectedPlant.getCorrectSunAmount()) {
                                            vm.onCompleteAdjustLight(selectedPlantId);
                                            seekBarSunAdjust.setVisibility(View.GONE);
                                            showPlantActionBtns(true, true);
                                        }
                                    }
                                });
                                seekBarSunAdjust.setVisibility(View.VISIBLE);
                                seekBarSunAdjust.setAlpha(0);
                                seekBarSunAdjust.animate()
                                        .alpha(1)
                                        .setDuration(1000);
                            });

                            showPlantActionBtns(selectedPlant.isHasSunLight(), false);
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

    private CompletableFuture<Void> createModelRenderable(int id, PlantType type, PlantState state) {
        return ModelRenderable.builder()
                .setSource(this.getActivity(), id)
                .build()
                .thenAccept(renderable -> {
                    Log.i(TAG, "Archivo cargado.");
                    renderables.put(type, state, renderable);
                })
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Unable to load Renderable.", throwable);
                            return null;
                        });
    }

    private Renderable getRenderableFromPlant(PlantType plantType, PlantState plantState) {
        return renderables.get(plantType, plantState);
    }

    private void showPlantActionBtns(boolean show, boolean animate) {
        ImageButton btnAddAbono = selectedPlantControl.getView().findViewById(R.id.btnAddAbono);
        ImageButton btnAddWater = selectedPlantControl.getView().findViewById(R.id.btnAddWater);
        ImageButton btnSun = selectedPlantControl.getView().findViewById(R.id.btnSun);
        if (show) {
            btnSun.setVisibility(View.GONE);
            btnAddAbono.setVisibility(View.VISIBLE);
            btnAddAbono.setAlpha(1f);
            btnAddWater.setVisibility(View.VISIBLE);
            btnAddWater.setAlpha(1f);
            if (animate) {
                btnAddAbono.setAlpha(0f);
                btnAddAbono.animate()
                        .alpha(1)
                        .setDuration(1000);
                btnAddWater.setAlpha(0f);
                btnAddWater.animate()
                        .alpha(1)
                        .setDuration(1000);
            }
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