package com.boboddy.vault.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        
        String[] projection = {PictureEntry._ID,
                                PictureEntry.COLUMN_NAME_PATH};
        
        String sortOrder = PictureEntry.COLUMN_NAME_PATH + " DESC";
        String where = PictureEntry.COLUMN_NAME_PATH + "=?";
        String[] whereArgs = {"*"};

        Cursor c = db.query(
                PictureEntry.TABLE_NAME,
                projection,
                where,
                whereArgs,
                null,
                null,
                sortOrder
        );
        
        c.moveToFirst();
        
        if(c.getCount() > 0) {
            do {
                String path = c.getString(c.getColumnIndexOrThrow(PictureEntry.COLUMN_NAME_PATH));
                
                pictures.add(new Picture(path));
            } while(c.moveToNext());
        }
        
        c.close();
        
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
        
        return newRowId != -1;
    }
}
