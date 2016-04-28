package c426117.per.carriersimple;

import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class LinkSettingActivity extends AppCompatActivity {

    private String ip = "10.177.6.203";
    private int prot = 2333;

    private ImageView hostimage = null;
    private EditText ipedit = null;
    private TextView hostname = null;
    private Button linkbutton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_setting);

        final SharedPreferences.Editor editor = getSharedPreferences("ipconfig",MODE_PRIVATE).edit();
        SharedPreferences pref = getSharedPreferences("ipconfig",MODE_PRIVATE);

        hostimage = (ImageView)findViewById(R.id.hostimage_linksetting);
        hostname = (TextView)findViewById(R.id.hostname_linksetting);
        ipedit = (EditText)findViewById(R.id.ipedit_linksetting);
        linkbutton = (Button)findViewById(R.id.button_settinglink);

        if(MainActivity.linkok)
        {
            hostimage.setImageResource(R.drawable.linkok);
            hostname.setText("已连接:"+MainActivity.ip);
        }

        ipedit.setText(pref.getString("ip",null));

        linkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip = ipedit.getText().toString();
                editor.putString("ip",ip);
                editor.commit();

                if(MainActivity.socket==null)
                {
                    LinkServer linkServer = new LinkServer(ip,prot);
                    Thread thread = new Thread(linkServer);
                    thread.start();

                    try
                    {
                        thread.join();
                        if(MainActivity.socket!=null)
                        {

                            hostname.setText("链接到:"+MainActivity.socket.getInetAddress().toString());
                            hostimage.setImageResource(R.drawable.linkok);
                            MainActivity.ip = ip;

                            GetDirStat getDirStat = new GetDirStat("D:");
                            Thread thread_getdirstat = new Thread(getDirStat);
                            thread_getdirstat.start();
                            Toast.makeText(LinkSettingActivity.this,"链接成功",Toast.LENGTH_SHORT).show();
                            MainActivity.linkok = true;
                            //MainActivity.socket = null;
                        }
                        else
                        {
                            Toast.makeText(LinkSettingActivity.this,"链接失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Toast.makeText(LinkSettingActivity.this,"链接失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }




}
