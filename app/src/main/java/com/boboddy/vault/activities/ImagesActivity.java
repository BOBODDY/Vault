package com.boboddy.vault.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.boboddy.vault.R;
import com.boboddy.vault.adapters.ImageAdapter;
import com.boboddy.vault.data.Picture;
import com.boboddy.vault.db.Database;
import com.boboddy.vault.util.Util;

import java.util.List;

public class ImagesActivity extends Activity {
    
    RecyclerView imagesRV;
    RecyclerView.LayoutManager layoutManager;
    ImageAdapter layoutAdapter;
    private int spanCount = 3;
    
    final private static int TAKE_PICTURE = 23;
    final private static int CAMERA_PERMISSION = 2315;
    
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        
        db = new Database(this);
        
        imagesRV = (RecyclerView) findViewById(R.id.imagesRV);
        
        layoutAdapter = new ImageAdapter(getApplicationContext());
        imagesRV.setAdapter(layoutAdapter);
        
        layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        imagesRV.setLayoutManager(layoutManager);
        
        new LoadImagesTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_images, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_image) {
            Log.d("Vault", "taking picture");
            if(checkCameraPermission()) {
                Intent i = new Intent();
                i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, Util.createFilename(this));
                startActivityForResult(i, TAKE_PICTURE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
            }
        }

        return super.onOptionsItemSelected(item);
    }
    
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) {
            case CAMERA_PERMISSION: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted, accessing camera
                    Intent i = new Intent();
                    i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, Util.createFilename(this));
                    startActivityForResult(i, TAKE_PICTURE);
                } else {
                    Log.d("Vault", "Camera permission denied");
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    
    private boolean checkCameraPermission() {
        int permission = this.checkCallingPermission(Manifest.permission.CAMERA);
        return permission == PackageManager.PERMISSION_GRANTED;
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == TAKE_PICTURE) {
                Log.d("Vault", "picture taken");
            }
        }
    }
    
    private class LoadImagesTask extends AsyncTask<Void, Void, List<Picture>> {
        
        public List<Picture> doInBackground(Void... params) { return db.getPictures(); }
        
        public void onPostExecute(List<Picture> pictures) {
            layoutAdapter.addItems(pictures);
        }
    }
}
