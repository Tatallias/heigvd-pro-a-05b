package com.example.painttest;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import Connection.Handler;
import Connection.Protocol;

public class GameActivity extends AppCompatActivity {

    private Activity thisActivity;
    private DrawingBoardView drawingBoardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String ip= getIp(savedInstanceState);
        int port= getPort(savedInstanceState);

        drawingBoardView = (DrawingBoardView) findViewById(R.id.paintView);
        drawingBoardView.connect(ip,port);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        drawingBoardView.init(metrics);
    }

    @Override
    protected void onResume() {
        super.onResume();
        thisActivity = this;

        String ip= getIp(null);
        int port= getPort(null);

        setContentView(R.layout.activity_main);


        drawingBoardView = (DrawingBoardView) findViewById(R.id.paintView);
        drawingBoardView.connect(ip,port);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        drawingBoardView.init(metrics);
    }


    int getPort(Bundle savedInstanceState){
        int port = 0;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                port= extras.getInt("port");
            }
        } else {

            port= (int) savedInstanceState.getSerializable("port");
        }
        return  port;
    }

    String getIp(Bundle savedInstanceState){
        String ip =null;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                ip= extras.getString("ip");
            }
        } else {
            ip= (String) savedInstanceState.getSerializable("ip");

        }
        return  ip;
    }

}