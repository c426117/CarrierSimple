package c426117.per.carriersimple;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by C426117 on 2016/4/23/023.
 */
public class LinkServer implements Runnable
{
    private String ip = null;
    private int port = 2333;

    LinkServer(String ip,int port)
    {
        this.ip = ip;
        this.port = port;
    }


    public void run ()
    {
        try
        {
            MainActivity.socket = new Socket(ip,port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            MainActivity.socket = null;
        }
    }

}
