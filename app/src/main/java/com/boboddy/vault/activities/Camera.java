package com.boboddy.vault.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import com.boboddy.vault.ImageReceiver;
import com.boboddy.vault.R;

import java.util.ArrayList;
import java.util.List;

public class Camera extends Activity {

    CameraManager cameraManager;
    CameraDevice cameraDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if(cameraPermission == PackageManager.PERMISSION_GRANTED) {
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

            try {
                cameraManager.openCamera("0", new CameraDevice.StateCallback() {
                    @Override
                    public void onOpened(CameraDevice camera) {
                        cameraDevice = camera;
                    }

                    @Override
                    public void onDisconnected(CameraDevice camera) {

                    }

                    @Override
                    public void onError(CameraDevice camera, int error) {

                    }
                }, null);

                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics("0");

                StreamConfigurationMap configs = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                
                Size[] sizes = configs.getOutputSizes(ImageFormat.JPEG);
                ImageReader imageReader = ImageReader.newInstance(sizes[0].getWidth(), 
                        sizes[0].getHeight(), ImageFormat.JPEG, 2);
                
                Surface jpegCaptureSurface = imageReader.getSurface();

                List<Surface> surfaces = new ArrayList<Surface>();
                surfaces.add(jpegCaptureSurface);
                
                cameraDevice.createCaptureSession(surfaces,
                        new CameraCaptureSession.StateCallback() {
                            @Override
                            public void onConfigured(CameraCaptureSession session) {
                                
                            }

                            @Override
                            public void onConfigureFailed(CameraCaptureSession session) {

                            }
                        }, null);
            }catch(CameraAccessException cae) {
                Log.e("Vault", "problem opening camera", cae);
            }
        }
    }
}
