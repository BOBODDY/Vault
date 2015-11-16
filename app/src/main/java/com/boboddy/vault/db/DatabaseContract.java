package com.boboddy.vault.db;

import android.provider.BaseColumns;

/**
 * Database contract
 */
public class DatabaseContract {
    
    public DatabaseContract() {}
    
    public static abstract class PictureEntry implements BaseColumns {
        public static final String TABLE_NAME = "pictures";
        public static final String COLUMN_NAME_PATH = "filePath";
    }
}
