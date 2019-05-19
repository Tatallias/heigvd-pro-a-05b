package com.example.Wizards;


import android.app.Activity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import Connection.Handler;


/**
 * the activity that holds the casting board view where everything happens
 *
 */
public class GameActivity extends AppCompatActivity {

    private Activity thisActivity;
    private CastingBoardView castingBoardView;

    private Handler handler;


    /**
     * gets the ip and port from the intent and creates a new connection and passes it to the castingBoardView
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String ip= getIp(savedInstanceState);
        int port= getPort(savedInstanceState);

        castingBoardView = (CastingBoardView) findViewById(R.id.paintView);
        connect(ip,port);
        castingBoardView.setHostActivity(this);
        castingBoardView.connect(handler);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        castingBoardView.init(metrics);
    }

    /**
     * gets the ip and port from the intent and creates a new connection and passes it to the castingBoardView
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        thisActivity = this;

        String ip= getIp(null);
        int port= getPort(null);

        setContentView(R.layout.activity_main);
        connect(ip,port);
        castingBoardView.setHostActivity(this);

        castingBoardView = (CastingBoardView) findViewById(R.id.paintView);
        castingBoardView.connect(handler);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        castingBoardView.init(metrics);
    }


    /**
     * return the port number stored either in the intent or the savedInstanceState
     * @param savedInstanceState
     * @return the port number of the server
     */
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

    /**
     * return the ip stored either in the intent or the savedInstanceState
     * @param savedInstanceState
     * @return the ip adress of the server
     */
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

    /**
     * if the handler isn't setup already create a new one with the give ip/port
     * @param ip the ip adress of the server
     * @param port the port number of the server
     */
    public void connect(String ip,int port) {
        if(handler==null){
            handler= new Handler(ip,port);
            handler.start();
        }

        //client.start();
    }
}