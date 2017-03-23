package com.codyy.rxbinding.sample;

import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.codyy.rx.permissions.Permission;
import com.codyy.rx.permissions.RxPermissions;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.IOException;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RxPermissionsSample";

    private Camera camera;
    private SurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxPermissions rxPermissions = new RxPermissions(getSupportFragmentManager());
        rxPermissions.setLogging(true);

        setContentView(R.layout.act_main);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        RxView.clicks(findViewById(R.id.enableCamera))
                // Ask for permissions when button is clicked
                .compose(rxPermissions.ensureEach(Manifest.permission.CAMERA))
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(@NonNull Permission permission) throws Exception {
                        Log.i(TAG, "Permission result " + permission);
                        if (permission.granted) {
                            releaseCamera();
                            camera = Camera.open(0);
                            try {
                                camera.setPreviewDisplay(surfaceView.getHolder());
                                camera.startPreview();
                            } catch (IOException e) {
                                Log.e(TAG, "Error while trying to display the camera preview", e);
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // Denied permission without ask never again
                            Toast.makeText(MainActivity.this,
                                    "Denied permission without ask never again",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            Toast.makeText(MainActivity.this,
                                    "Permission denied, can't enable the camera",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG, "onError", throwable);
                    }
                })
        ;
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

}
