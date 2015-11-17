package com.boboddy.vault.data;

/**
 * Super secret picture
 */
public class Picture {
    
    private String filepath;
    
    public Picture(String filepath) {
        this.filepath = filepath;
    }
    
    public String getPath() {
        return filepath;
    }
    
    public void setPath(String newPath) {
        this.filepath = newPath;
    }
}
