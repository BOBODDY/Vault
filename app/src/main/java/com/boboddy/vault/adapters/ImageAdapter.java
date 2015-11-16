package com.boboddy.vault.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boboddy.vault.data.Picture;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying secret images in a grid
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(ImageView iv) {
            super(iv);
            imageView = iv;
        }
    }
    
    List<Picture> dataset;
    
    public ImageAdapter() {
        dataset = new ArrayList<>();
    }
    
    public ImageAdapter(List<Picture> newData) {
        dataset = newData;
    }
    
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup vg, int i) {
        return null;
    }
    
    public void onBindViewHolder(ImageAdapter.ViewHolder vh, int position) {
        
    }
    
    public void addItems(List<Picture> newData) {
        dataset.addAll(newData);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
