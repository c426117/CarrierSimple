package c426117.per.carriersimple;

import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by C426117 on 2016/4/24/024.
 */
public class FileSave implements Runnable
{
    String filename = null;
    byte[] temp = new byte[1024];

    FileSave(String filename)
    {
        this.filename =filename;
    }

    public void run ()
    {
        try
        {
            MainActivity.filesocet = new Socket(MainActivity.ip,2335);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        FileOutputStream fout = null;
        DataInputStream din = null;
        PrintStream out = null;
        File file = new File(MainActivity.downloadPlace+filename.substring(MainActivity.fileTree.peek().length()+1));


        try
        {
            fout = new FileOutputStream(file);
            din = new DataInputStream(MainActivity.filesocet.getInputStream());
            out = new PrintStream(MainActivity.filesocet.getOutputStream());
            out.print(filename+"\n");
            out.flush();
            int i = 0;
            while ((i = din.read(temp))>0)
            {
                fout.write(temp,0,i);
                fout.flush();
                Log.d("写入",String.valueOf(i)+"\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("传输结束");

        try
        {
            fout.close();
            din.close();
            out.close();
            MainActivity.filesocet.close();
            MainActivity.filesocet = null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }



}

