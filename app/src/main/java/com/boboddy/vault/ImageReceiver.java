package com.boboddy.vault;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.boboddy.vault.data.Picture;
import com.boboddy.vault.db.Database;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Activity for receiving pictures from other apps for safe-keeping
 */
public class ImageReceiver extends Activity {
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        
        if(Intent.ACTION_SEND.equals(action) && type != null) {
            if(type.startsWith("image/")) {
                handleSendImage(intent);
            }
        } else if(Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        }
    }
    
    public void handleSendImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if(imageUri != null) {
            // Add image to database, copy to private file directory

            String path = getFilesDir() + File.separator + getImageFileName();
            File f = new File(path);
            Picture sentPic = new Picture(path);
            try {
                FileOutputStream fos = new FileOutputStream(f);
                Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                
                fos.write(outputStream.toByteArray());
                fos.close();
            } catch(FileNotFoundException fnfe) {
                Log.e("Vault", "file not found", fnfe);
            } catch(IOException ioe) {
                Log.e("Vault", "ioe", ioe);
            }

            Database db = new Database(this);
            db.addPicture(sentPic);

            Toast.makeText(this, "Locked away", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    public void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if(imageUris != null) {
            // Add images to database, copy to private file directory
        }
    }

    private String getImageFileName() {
        String filename = "";

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        filename = timeStamp + ".png";

        return filename;
    }
}
