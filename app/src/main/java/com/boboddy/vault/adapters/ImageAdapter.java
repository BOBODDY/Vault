package com.boboddy.vault.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boboddy.vault.R;
import com.boboddy.vault.data.Picture;
import com.boboddy.vault.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying secret images in a grid
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image);
        }
    }
    
    List<Picture> dataset;
    Context context;
    
    public ImageAdapter(Context ctx) {
        context = ctx;
        dataset = new ArrayList<>();
    }
    
    public ImageAdapter(Context ctx, List<Picture> newData) {
        context = ctx;
        dataset = newData;
    }
    
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup vg, int i) {
        View itemView = LayoutInflater.from(vg.getContext()).inflate(R.layout.image, vg, false);
        return new ViewHolder(itemView);

    }
    
    public void onBindViewHolder(ImageAdapter.ViewHolder vh, int position) {
        Picture pic = dataset.get(position);
        String path = pic.getPath();
        Log.d("Vault", "binding image " + path);
        vh.imageView.setImageBitmap(Util.getThumbnail(path));
    }
    
    public void addItems(List<Picture> newData) {
        dataset.addAll(newData);

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
