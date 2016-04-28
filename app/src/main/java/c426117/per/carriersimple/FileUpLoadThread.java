package c426117.per.carriersimple;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by C426117 on 2016/4/25/025.
 */
public class FileUpLoadThread implements Runnable
{
    private String filenameclient = null;
    private String filenameserver = null;


    FileUpLoadThread(String filename)
    {
        int cutfilenameclient = 0;
        int cutfilenameserver = 0;
        int count = 0;
        for(int i = 0;i!=filename.length();i++)
        {
            if(filename.charAt(i)=='/')
            {
                count++;
                if(count == 3)
                {
                    cutfilenameclient = i;
                }
                cutfilenameserver=i;
            }
        }

        filenameclient = filename.substring(cutfilenameclient);
        filenameserver = MainActivity.fileTree.peek()+filename.substring(cutfilenameserver);
    }

    public void run ()
    {
        try
        {
            Socket socket = new Socket(MainActivity.ip,2336);
            PrintStream out = new PrintStream(socket.getOutputStream());
            out.print(filenameserver+"\n");
            out.flush();
            out.close();
            socket.close();

            Thread.sleep(100);
            socket = new Socket(MainActivity.ip,2336);
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            FileInputStream fin = new FileInputStream(filenameclient);

            byte[] buf = new byte[1024];
            int l = 0;
            while ((l = fin.read(buf))>0)
            {
                dout.write(buf,0,l);
                dout.flush();
            }

            fin.close();
            dout.close();
            socket.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
