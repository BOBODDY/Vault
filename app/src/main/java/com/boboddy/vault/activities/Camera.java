package com.boboddy.vault.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.boboddy.vault.R;
import com.boboddy.vault.util.CameraPreview;
import com.boboddy.vault.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera extends Activity {

    private android.hardware.Camera camera;
    private CameraPreview preview;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        camera = getCameraInstance();
        if(camera != null) {
            preview = new CameraPreview(this, camera);
            FrameLayout previewLayout = (FrameLayout) findViewById(R.id.camera_preview);
            previewLayout.addView(preview);
        }

        Button capture = (Button) findViewById(R.id.camera_capture);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, picture);
            }
        });
    }

    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if(camera != null) {
            camera.release();
            camera = null;
        }
    }

    private android.hardware.Camera.PictureCallback picture = new android.hardware.Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
            Log.v("Vault", "in PictureCallback");

            String s = Util.createFilename(getApplicationContext());
            File tmpF = new File(s);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, options);

            options.inSampleSize = calculateInSampleSize(options, 200, 200);

            options.inJustDecodeBounds = false;

            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);

            int displayRotation = getDisplayRotation();
            bmp = rotateBitmap(bmp, displayRotation);

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            bmp.compress(Bitmap.CompressFormat.PNG, 100, byteStream);

            try {
                FileOutputStream fos = new FileOutputStream(tmpF);
                fos.write(byteStream.toByteArray(), 0, byteStream.size());
                fos.close();
            } catch(FileNotFoundException fnfe) {
                Log.e("Vault", "file not found", fnfe);
            } catch(IOException ioe) {
                Log.e("Vault", "error", ioe);
            }
            finish();
        }
    };

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap rotateBitmap(Bitmap in, int angle) {
        Matrix mat = new Matrix();
        mat.postRotate(angle);
        return Bitmap.createBitmap(in, 0, 0, in.getWidth(), in.getHeight(), mat, true);
    }

    public static android.hardware.Camera getCameraInstance(){
        android.hardware.Camera c = null;
        try {
            if(android.hardware.Camera.getNumberOfCameras() > 1) {
                c = android.hardware.Camera.open(android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK);
            }
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.e("Vault", "Camera is not available (in use or does not exist)", e);
        }
        return c; // returns null if camera is unavailable
    }

    private int getDisplayRotation() {
        Display display = getWindowManager().getDefaultDisplay();
        int rotation = 0;
        switch (display.getRotation()) {
            case Surface.ROTATION_0: // This is display orientation
                rotation = 270;
                break;
            case Surface.ROTATION_90:
                rotation = 0;
                break;
            case Surface.ROTATION_180:
                rotation = 90;
                break;
            case Surface.ROTATION_270:
                rotation = 180;
                break;
        }
        return rotation;
    }
}
