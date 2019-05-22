package com.example.Wizards;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import Utility.Util;

/**
 * The welcome screen activity with a button to open the qr scan/join Activity
 */
public class WelcomeActivity extends AppCompatActivity {

    static final int PERMISSIONS_REQUEST=1;
    private Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //display of welcome Screen
        setContentView(R.layout.activity_welcome_screen);

        thisActivity=this;

        //check the permission to use the camera to read QRcode
        if(checkSelfPermission(android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{android.Manifest.permission.CAMERA},PERMISSIONS_REQUEST);
        }


        // creation of the button to join a game
        final Button guest= (Button) findViewById(R.id.GuestButton);
        guest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                StartQRCodeReaderActivity(v);
            }
        });



    }

    /**
     * Statrt the qr codeReaderActivity or directly launches the game if running on emulator(for testing purposes)
     * @param view
     */
    public void StartQRCodeReaderActivity(View view) {

        if( !Util.isEmulator()){
            Intent intent = new Intent(this, QRCodeReaderActivity.class);
            this.startActivity(intent);
        }else {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("port",8384);
            intent.putExtra("ip","10.192.94.100");

            this.startActivity(intent);
        }
    }


}
