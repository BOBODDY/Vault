package com.boboddy.vault.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.boboddy.vault.db.DatabaseContract.*;

/**
 * Helper class for creating and maintaining a database
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "Vault";

    private static final String DB_NAME = "pictures";
    private static final int DB_VERSION = 1;

    private static final String PICTURE_TABLE_CREATE =
            "CREATE TABLE " + PictureEntry.TABLE_NAME + " (" +
                    PictureEntry._ID + " INTEGER PRIMARY KEY, " +
                    PictureEntry.COLUMN_NAME_PATH + ");";
    
    private static final String PICTURE_TABLE_DELETE = "" +
            "DROP TABLE IF EXISTS " + PictureEntry.TABLE_NAME;

    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(TAG, "in Database helper constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "creating database");
        db.execSQL(PICTURE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //This deletes data, should figure out a proper upgrade path
        Log.d(TAG, "upgrading database");
        db.execSQL(PICTURE_TABLE_DELETE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "downgrading database");
        onUpgrade(db, oldVersion, newVersion);
    }
}
