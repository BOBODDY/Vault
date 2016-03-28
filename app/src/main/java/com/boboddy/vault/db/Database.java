package com.boboddy.vault.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.boboddy.vault.data.Picture;

import com.boboddy.vault.db.DatabaseContract.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nick on 11/16/15.
 */
public class Database {
    
    DatabaseOpenHelper dbHelper;
    Context context;
    
    public Database(Context ctx) {
        context = ctx;
        initializeDbHelper();
    }
    
    public void initializeDbHelper() {
        if(context != null) {
            dbHelper = new DatabaseOpenHelper(context);            
        }
    }
    
    public List<Picture> getPictures() {
        List<Picture> pictures = new ArrayList<>();
        
        if(dbHelper == null) {
            initializeDbHelper();
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String[] projection = null;
        
        String sortOrder = PictureEntry.COLUMN_NAME_PATH + " DESC";
        String where = PictureEntry.COLUMN_NAME_PATH + "=?";
        String[] whereArgs = {"*"};

//        Cursor c = db.query(
//                PictureEntry.TABLE_NAME,
//                projection,
//                where,
//                whereArgs,
//                null,
//                null,
//                sortOrder
//        );
        //I don't like this, it's a bit smelly
        Cursor c = db.rawQuery("SELECT * FROM " + PictureEntry.TABLE_NAME, null);
        
        c.moveToFirst();
        Log.d("Vault", "cursor count: " + c.getCount());
        if(c.getCount() > 0) {
            do {
                String path = c.getString(c.getColumnIndex(PictureEntry.COLUMN_NAME_PATH));
                Log.d("Vault", "cursor: " + c.toString());
                Log.d("Vault", "path: " + path);
                pictures.add(new Picture(path));
            } while(c.moveToNext());
        }
        
        c.close();

        Log.d("Vault", "# images returned by db: " + pictures.size());
        
        return pictures;
    }
    
    public boolean addPicture(Picture newPic) {
        if(dbHelper == null) {
            initializeDbHelper();
        }
        
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PictureEntry.COLUMN_NAME_PATH, newPic.getPath());
        
        long newRowId = db.insert(PictureEntry.TABLE_NAME, null, values);
        
        db.close();

        Log.d("Vault", "added image to row " + newRowId);
        
        return newRowId != -1;
    }
}
