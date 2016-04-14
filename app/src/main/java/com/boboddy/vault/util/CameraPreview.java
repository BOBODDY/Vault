package com.boboddy.vault.util;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by boboddy on 4/13/2016.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    
    private SurfaceHolder mHolder;
    private Camera mCamera;
    
    private Context context;
    
    private static String TAG = "Vault";
    
    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        this.context = context;
        
        //Install a SurfaceHolder.Callback so we get notified when the 
        //underlying surface is created and destroyed
        mHolder = getHolder();
        mHolder.addCallback(this);
        //deprecated, but required on Android 3.0-
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch(IOException ioe) {
            Log.e(TAG, "error setting camera preview", ioe);
        }
    }
    
    public void surfaceDestroyed(SurfaceHolder holder) {}
    
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if(mHolder.getSurface() == null) {
            return;
        }
        
        try {
            mCamera.stopPreview();
        } catch(Exception e) {
            Log.d(TAG, "tried to stop a non-existant preview");
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        Camera.Parameters parameters = mCamera.getParameters();
        Camera.Size size = getBestPreviewSize(w, h);

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        switch (display.getRotation()) {
            case Surface.ROTATION_0: // This is display orientation
                if (size.height > size.width) parameters.setPreviewSize(size.height, size.width);
                else parameters.setPreviewSize(size.width, size.height);
                mCamera.setDisplayOrientation(90);
                break;
            case Surface.ROTATION_90:
                if (size.height > size.width) parameters.setPreviewSize(size.height, size.width);
                else parameters.setPreviewSize(size.width, size.height);
                mCamera.setDisplayOrientation(0);
                break;
            case Surface.ROTATION_180:
                if (size.height > size.width) parameters.setPreviewSize(size.height, size.width);
                else parameters.setPreviewSize(size.width, size.height);
                mCamera.setDisplayOrientation(270);
                break;
            case Surface.ROTATION_270:
                if (size.height > size.width) parameters.setPreviewSize(size.height, size.width);
                else parameters.setPreviewSize(size.width, size.height);
                mCamera.setDisplayOrientation(180);
                break;
        }

        Camera.Size pictureSize = getBestPictureSize(mCamera.getParameters());
        parameters.setPictureSize(pictureSize.width, pictureSize.height);
        mCamera.setParameters(parameters);


        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    private Camera.Size getBestPictureSize(Camera.Parameters parameters) {
        Camera.Size result = null;

        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        for(int i = 0; i < sizes.size(); i++) {
            Camera.Size size = sizes.get(i);
            if(result == null) {
                result = size;
            } else {
                int resultArea = result.width * result.height;
                int newArea = size.width * size.height;

                if(newArea > resultArea) {
                    result = size;
                }
            }
        }

        return result;
    }

    private Camera.Size getBestPreviewSize(int width, int height) {
        Camera.Size result=null;
        Camera.Parameters p = mCamera.getParameters();
        for (Camera.Size size : p.getSupportedPreviewSizes()) {
            if (size.width<=width && size.height<=height) {
                if (result==null) {
                    result=size;
                } else {
                    int resultArea=result.width*result.height;
                    int newArea=size.width*size.height;

                    if (newArea>resultArea) {
                        result=size;
                    }
                }
            }
        }
        return result;
    }
}
