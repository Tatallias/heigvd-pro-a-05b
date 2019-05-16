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
        if( strings[0].equals("SEND")){
                handler.request(strings[1]);

            return 2l;
        }

        return -1l;
    }
}
