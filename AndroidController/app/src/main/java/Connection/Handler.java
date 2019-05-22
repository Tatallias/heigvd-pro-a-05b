package Connection;


import com.example.Wizards.GameActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class managing all the communication with the with the server
 */


public class Handler extends Thread implements Serializable {
    final static Logger LOG = Logger.getLogger(Handler.class.getName());

    BufferedReader input;
    PrintWriter output;
    int serverPort;
    boolean connected = false;
    String in;
    private int playerNumber = 0;
    private String serverAddress;
    private boolean running;

    private GameActivity gameActivity ;

    public Handler(String serverAddress, int serverPort, GameActivity gameActivity) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.gameActivity= gameActivity;
        running = true;
    }


    private void connect() {
        try {

            Socket clientSocket = new Socket(serverAddress, serverPort);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream());
            connected = true;
            output.println("READY");
            output.flush();
            final String answer = input.readLine();

                gameActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      gameActivity.setPlayerIdText(Integer.parseInt(answer));
                    }
                });





        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Unable to connect to server: {0}", e.getMessage());
            cleanup();

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
        if (!connected) {
            connect();
        }
        while (running) {
            if (in != null) {
                output.println(in);
                output.flush();
                in = null;
            }
        }
    }
}