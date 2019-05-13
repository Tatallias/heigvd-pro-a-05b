package Connection;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.logging.Level;

import static Connection.Handler.LOG;

public class MessageSender extends AsyncTask<String, Void, Long> {


    Handler handler ;
    ByteBuffer receivingBuffer, sendingBuffer;

    String in;


    public MessageSender(Handler h) {
        handler= h;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("AsyncTask", "onPreExecute");
    }

    @Override
    protected void onPostExecute(Long result) {
       if(result==1){
           handler.confirmConnection();
       }
    }

    @Override
    protected Long doInBackground(String... strings) {
        int i = 3253256;
        if (strings[0].equals("CONNECT")) {
            try {
                sendingBuffer = ByteBuffer.allocate(200);
                receivingBuffer = ByteBuffer.allocate(200);

                sendingBuffer.put((byte) 1);
                sendingBuffer.flip();

                SocketAddress ad = null;
                int waitedTime=0;
                do {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.getChannel().send(sendingBuffer, handler.getAddress());
                    ad = handler.getChannel().receive(receivingBuffer);

                    waitedTime+=50;
                } while (ad == null&& waitedTime<1000);

                if(ad!=null){
                    handler.connected=true;
                    return 1l;
                }
                LOG.log(Level.INFO, "Client received " + receivingBuffer.toString(), in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1l;

        }else if( strings[0].equals("SEND")){
            try {
                sendingBuffer = ByteBuffer.allocate(400);
                receivingBuffer = ByteBuffer.allocate(400);

                sendingBuffer.put(strings[1].getBytes());
                sendingBuffer.flip();


                handler.getChannel().send(sendingBuffer,  handler.getAddress());


                LOG.log(Level.INFO, "Client received " + receivingBuffer.toString(), in);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return 2l;
        }

        return -1l;
    }
}
