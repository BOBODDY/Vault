package com.boboddy.vault.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.boboddy.vault.R;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class UnlockActivity extends Activity {
    
    private static final String PREFS_NAME = "Vault.prefs";
    
    EditText pin;
    TextView label, fpLabel;
    ImageView fpIcon;

    FingerprintManager fingerprintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);

        fpLabel = (TextView) findViewById(R.id.fingerprintLabel);
        fpIcon = (ImageView) findViewById(R.id.fingerprintIcon);

        fpLabel.setVisibility(View.GONE);
        fpIcon.setVisibility(View.GONE);

        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        // Make sure we have permission to use the fingerprint
//        if(getPackageManager().checkPermission(PackageManager.FEATURE_FINGERPRINT, "com.boboddy.vault") == PackageManager.PERMISSION_GRANTED) {
        try {
            if (fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints()) {
                fpLabel.setVisibility(View.VISIBLE);
                fpIcon.setVisibility(View.VISIBLE);
                fingerprintManager.authenticate(null,   //FingerprintManager.CryptoObject
                        new CancellationSignal(),       //CancellationSignal
                        0,                              //flags
                        new FingerprintCallback(),      //FingerprintManager.AuthenticationCallback
                        null);                          //Handler
            }
        } catch(SecurityException e) {
            Log.e("Vault", "error in fingerprint", e); //TODO: check permission properly
        }

        
        label = (TextView) findViewById(R.id.pin_label);
        
        pin = (EditText) findViewById(R.id.pin);
        pin.addTextChangedListener(new PinWatcher());
    }

    private boolean checkPin(String pin) {
        boolean res = true;

        if(pin.length() == 4) {
            if(pin.equals("1234")) { // TODO: add activity for creating a new PIN
                return true;
            }
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

    private class PinWatcher implements TextWatcher {
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
    }

    private class FingerprintCallback extends FingerprintManager.AuthenticationCallback {

        public void onAuthenticationError(int errCode, CharSequence errString) {
            fpLabel.setText("Error reading fingerprint!");
        }

        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            fpLabel.setText(helpString);
        }

        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            //Do something to succeed
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);

            //TODO: play an unlocking sound
            //TODO: show an animation as a screen transition

            finish();
        }
    }
}
