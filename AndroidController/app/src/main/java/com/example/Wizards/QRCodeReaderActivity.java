package com.example.Wizards;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import Connection.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/** the connection/qr reading activity that scans for a qr code containing
 * the ips of the host server and attempts to set up a connection. if it succeeds to do so with one of the ips
 * sends the correct one to the game activity
 */
public class QRCodeReaderActivity extends AppCompatActivity {

    private CameraSource cam;
    private SurfaceView cam_view;
    private TextView text_dec;
    public BarcodeDetector qr_detect;
    public Intent intent;

    private String hostIPs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_reader);
        //intent = new Intent(this, GameActivity.class);

        cam_view = (SurfaceView) findViewById(R.id.image_cam);
        text_dec = (TextView) findViewById(R.id.decod_text);


        //setup QRcode detctor
        qr_detect = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cam = new CameraSource.Builder(this, qr_detect)
                .setAutoFocusEnabled(true).build();

        qr_detect.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qr = detections.getDetectedItems();
                if (qr.size() != 0) {
                    String scannedHostIPS = qr.valueAt(0).displayValue;
                    if (!scannedHostIPS.equals(hostIPs)) {
                        hostIPs = scannedHostIPS;

                        tryConnection(hostIPs);
                    }
                }
            }
        });

        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            text_dec.setText(R.string.qr_text_no_perm);
        } else {
            cam_view_setup();
        }
    }

    /**
     * Parses the string containing the ips and port from the qr code and tries to connect to each of them
     * @param ipsAndPort the string of ips and port "<ip1> <ip2> ... <ipn>\n<port>"
     */
    void tryConnection(String ipsAndPort){
        String[] splitIpsAndPort= ipsAndPort.split("\n");
        String[] ips= splitIpsAndPort[0].split(" ");
        int port = Integer.parseInt(splitIpsAndPort[1]);

        for (String s:ips) {


            try {
                Socket socket= new Socket();
                socket.connect(new InetSocketAddress(s,port),500);
                if(socket.isConnected()){
                    startGameActivity(s,port);
                    break;
                }

            } catch (Exception e) {

            }
        }
    }


    void cam_view_setup() {
        cam_view.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cam.start(cam_view.getHolder());
                    }
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cam.stop();
            }
        });
        cam_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(intent,0);
        finish();
    }

    //start the reading of the QRcode if button pressed
    public void startGameActivity(String ip,int port) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("ip",ip);
        intent.putExtra("port",port);

        this.startActivity(intent);
    }




}
