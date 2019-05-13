package Connection;


import android.app.Activity;
import android.icu.text.StringSearch;
import android.os.Build;

import com.example.painttest.QRCodeReaderActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

import java.nio.channels.DatagramChannel;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Handler  {
    final static Logger LOG = Logger.getLogger(Handler.class.getName());

    DatagramChannel channel;
    SocketAddress address;
    BufferedReader input;
    PrintWriter output;
    boolean connected = false;
    String in;
    private String ip=null;
    QRCodeReaderActivity connectionActivity=null;

    public Handler(String serverAddress, int serverPort, QRCodeReaderActivity connectionActivity) {
        this(serverAddress,serverPort);
        this.connectionActivity= connectionActivity;
    }
    public Handler(String serverAddress, int serverPort) {
        this(serverAddress,serverPort,true);

    }
    public Handler(String serverAddress, int serverPort,boolean sendConnectionPackage) {
        ip= serverAddress;
        connect(serverAddress,serverPort);
        if(sendConnectionPackage){



            new MessageSender(this).execute("CONNECT");
        }
    }

    public void connect(String serverAddress, int serverPort) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                channel = DatagramChannel
                        .open(StandardProtocolFamily.INET);
            }



            address = new InetSocketAddress(serverAddress, Protocol.PRESENCE_DEFAULT_PORT);


            channel.configureBlocking(false);




            //input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //output = new PrintWriter(clientSocket.getOutputStream());

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Unable to connect to server: {0}", e.getMessage());
            cleanup();
            return;
        }
    }

    public void confirmConnection(){
        if(connectionActivity!=null){
            connectionActivity.validateConnection(this);
        }
    }
    public void request(String in){
        output.println(in);
        output.flush();
    }


    public void sendSpell(String req){
        new MessageSender(this).execute("SEND",req);
    }


    public void disconnect() {
        LOG.log(Level.INFO, "Client requested to be disconnected", in);
        connected = false;
        cleanup();
    }

    private void cleanup() {

        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        if (output != null) {
            output.close();
        }
    }

    public DatagramChannel getChannel() {
        return channel;
    }

    public SocketAddress getAddress() {
        return address;
    }

    public String getIp() {
        return ip;
    }
}