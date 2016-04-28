package c426117.per.carriersimple;


import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


/**
 * Created by C426117 on 2016/4/24/024.
 */
public class GetDirStat implements Runnable
{
    private String here;
    GetDirStat(String here)
    {
        this.here = here;
    }
    public void run ()
    {
        try
        {
            MainActivity.socket = new Socket(MainActivity.ip,2334);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        BufferedReader in = null;
        PrintStream out = null;
        try
        {
            in = new BufferedReader(new InputStreamReader(MainActivity.socket.getInputStream()));
            out = new PrintStream(MainActivity.socket.getOutputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        out.print(here+"\n");
        out.flush();
        int lenght = 0;
        try
        {
            lenght =Integer.parseInt(in.readLine());
            if(lenght!=0)
            {
                MainActivity.namesList.clear();
                for(int i =0;i!=lenght;i++)
                {
                    String str =in.readLine().toString();
                    Log.d("filename",str+"\n");
                    MainActivity.namesList.add(new ListViewItem(str));
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            in.close();
            out.close();
            MainActivity.socket.close();
            MainActivity.socket = null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
