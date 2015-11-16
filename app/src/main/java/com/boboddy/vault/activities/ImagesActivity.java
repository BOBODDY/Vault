package com.boboddy.vault.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.boboddy.vault.R;
import com.boboddy.vault.adapters.ImageAdapter;
import com.boboddy.vault.data.Picture;
import com.boboddy.vault.db.Database;

import java.util.List;

public class ImagesActivity extends Activity {
    
    RecyclerView imagesRV;
    RecyclerView.LayoutManager layoutManager;
    ImageAdapter layoutAdapter;
    private int spanCount = 3;
    
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        
        db = new Database(this);
        
        imagesRV = (RecyclerView) findViewById(R.id.imagesRV);
        
        layoutAdapter = new ImageAdapter();
        imagesRV.setAdapter(layoutAdapter);
        
        layoutManager = new GridLayoutManager(this, spanCount);
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
        }

        return super.onOptionsItemSelected(item);
    }
    
    private class LoadImagesTask extends AsyncTask<Void, Void, List<Picture>> {
        
        public List<Picture> doInBackground(Void... params) { return db.getPictures(); }
        
        public void onPostExecute(List<Picture> pictures) {
            layoutAdapter.addItems(pictures);
        }
    }
}
