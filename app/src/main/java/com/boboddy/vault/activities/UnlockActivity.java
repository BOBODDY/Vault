package com.boboddy.vault.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.boboddy.vault.R;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class UnlockActivity extends Activity {
    
    private static final String PREFS_NAME = "Vault.prefs";
    
    EditText pin;
    TextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);
        
        label = (TextView) findViewById(R.id.pin_label);
        
        pin = (EditText) findViewById(R.id.pin);
        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pass = s.toString();
                
                if(checkPin(pass)) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    
                    //TODO: play an unlocking sound
                    //TODO: show an animation as a screen transition
                    
                    finish();
                } else {
                    if(pass.length() > 3) {
                        label.setText(R.string.wrong_pin);
                        label.setTextColor(Color.RED);
                    }
                }
            }
        });
    }

    private boolean checkPin(String pin) {
        boolean res = true;

        if(pin.length() == 4) {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);

            String storedHash = prefs.getString("user_pin", "");

            if (storedHash.equals("")) {
                res = false;
            }
            String inputHash = "";
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                digest.update(pin.getBytes());
                inputHash = new String(digest.digest());
            } catch (NoSuchAlgorithmException e) {
                Log.e("Vault", "error hashing inputted pin", e);
            }

            if (storedHash.equals(inputHash)) {
                res = true;
            }
        } else {
            res = false;
        }

        return res;
    }
}
