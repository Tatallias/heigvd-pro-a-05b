package Connection;


import com.example.painttest.QRCodeReaderActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Handler extends Thread {
    final static Logger LOG = Logger.getLogger(Handler.class.getName());

    BufferedReader input;
    PrintWriter output;
    int serverPort;
    boolean connected = false;
    String in;
    private String serverAddress;
    private boolean running;
    QRCodeReaderActivity connectionActivity=null;


    public Handler(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        running = true;
    }

    private void connect() {
        try {
            Socket clientSocket = new Socket(serverAddress, serverPort);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream());
            connected = true;
            this.in = in;
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
    public void request(String in) {
        this.in = in;
    }

    public void disconnect() {
        running = false;
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

    public int getSeverPort() {
        return serverPort;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    @Override
    public void run() {
        connect();

        while (running) {
            if(in != null) {
                output.println(in);
                output.flush();
                in = null;
            }
        }
    }
}