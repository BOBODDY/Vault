package com.boboddy.vault.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.boboddy.vault.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CreatePinActivity extends Activity {

    private static final String PREFS_NAME = "Vault.prefs";

    EditText pin1, pin2;
    Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        pin1 = (EditText) findViewById(R.id.pin1);
        pin2 = (EditText) findViewById(R.id.pin2);
        pin2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String try1 = pin1.getText().toString();
                String try2 = pin2.getText().toString();

                if(try1.equals(try2)) {

                    //Store a SHA-256 hash of the new PIN
                    String inputHash = "";
                    try {
                        MessageDigest digest = MessageDigest.getInstance("SHA-256");
                        digest.update(try1.getBytes());
                        inputHash = new String(digest.digest());
                    } catch (NoSuchAlgorithmException e) {
                        Log.e("Vault", "error hashing inputted pin", e);
                    }

                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
                    prefs.edit().putString("user_pin", inputHash).apply();

                    finish();
                }
            }
        });
    }

}
