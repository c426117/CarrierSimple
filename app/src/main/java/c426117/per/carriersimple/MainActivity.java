package c426117.per.carriersimple;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    public static String ip = null;
    public static Socket socket = null;
    public static Socket filesocet = null;
    public static String downloadPlace = "/storage/emulated/0/";//目前固定使用这个
    public static List<ListViewItem> namesList = new ArrayList<ListViewItem>();
    public static boolean linkok = false;


    public static BufferedReader in = null;
    public static PrintStream out = null;

    private ImageView hostimage = null;
    private TextView hostname = null;
    private MyAdapter myAdapter = null;

    private TextView nothing = null;
    private ListView filestat = null;//目录结构
    private String startplace = "D:";//根目录
    public static Stack<String> fileTree = null;

    private String uploadFilename = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nothing = (TextView)findViewById(R.id.main_nothing);
        filestat = (ListView)findViewById(R.id.filenames_listview);
        fileTree = new Stack<String>();
        fileTree.push(startplace);


        nothing.setVisibility(View.VISIBLE);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "请选择文件上传"), 1);

            }
        });


        //----------------------------------侧滑菜单的控制
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);//开启菜单的监听


        filestat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem listViewItem = namesList.get(position);
                String temp = listViewItem.getlujing();
                if(temp.charAt(temp.length()-1)=='#')
                {
                    temp = temp.substring(0,temp.length()-1);
                    fileTree.push(temp);
                    Thread thread = new Thread(new GetDirStat(temp));
                    try
                    {
                        thread.start();
                        thread.join();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    myAdapter = new MyAdapter(MainActivity.this,R.layout.listviewitem,namesList);
                    filestat.setAdapter(myAdapter);
                }
                else
                {
                    //启动下载
                    final String filename = temp;
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("是否下载此文件?");
                    dialog.setMessage("将文件保存到"+downloadPlace);
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Thread thread = new Thread(new FileSave(filename));
                            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                            Notification notification = new Notification.Builder(MainActivity.this)
                                    .setContentTitle("文件开始下载")
                                    .setContentText("下载文件"+filename)
                                    .setSmallIcon(R.drawable.ic_save)
                                    .build();
                            notificationManager.notify(1,notification);

                            thread.start();
                            try
                            {
                                thread.join();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            Notification notification2 = new Notification.Builder(MainActivity.this)
                                    .setContentTitle("文件下载完成")
                                    .setContentText("下载文件"+filename+"完成")
                                    .setSmallIcon(R.drawable.ic_save)
                                    .build();
                            notificationManager.notify(2,notification2);
                        }
                    });

                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }

        if(!startplace.equals(fileTree.peek()))
        {
            fileTree.pop();
            String temp = fileTree.peek();
            Thread thread = new Thread(new GetDirStat(temp));
            try
            {
                thread.start();
                thread.join();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            myAdapter = new MyAdapter(MainActivity.this,R.layout.listviewitem,namesList);
            filestat.setAdapter(myAdapter);
        }


    }

    @Override


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId())
        {
            case R.id.linksetting :
            {
                Intent intent_toLinksetting = new Intent(MainActivity.this,LinkSettingActivity.class);
                startActivity(intent_toLinksetting);
            }break;

            case R.id.settings :
            {

                Intent intent_setting = new Intent(MainActivity.this,Setting.class);
                startActivity(intent_setting);

            }break;

            case R.id.stoplink :
            {

            }break;

            case R.id.exit :
            {
                linkok = false;
                this.finish();
            }break;

            default:break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onResume ()
    {
        super.onResume();
        if(linkok)
        {
            hostimage=(ImageView)findViewById(R.id.hostimage_slideview);
            hostname = (TextView)findViewById(R.id.hostname_slideview);
            hostimage.setImageResource(R.drawable.linkok);
            hostname.setText("已连接:"+ip);
            nothing = (TextView)findViewById(R.id.main_nothing);
            nothing.setVisibility(View.GONE);

            myAdapter = new MyAdapter(MainActivity.this,R.layout.listviewitem,namesList);
            filestat.setAdapter(myAdapter);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode)
        {
            case 1 :
            {
                if(resultCode== Activity.RESULT_OK)
                {
                    uploadFilename = data.getData().toString();
                    if(linkok)
                    {
                        Thread uploadThread = new Thread(new FileUpLoadThread(uploadFilename));
                        uploadThread.start();
                        NotificationManager notificationManager3 = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification3 = new Notification.Builder(MainActivity.this)
                                .setContentTitle("文件开始上传")
                                .setContentText("上传文件")
                                .build();
                        notificationManager3.notify(3,notification3);
                        try
                        {
                            uploadThread.join();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        NotificationManager notificationManager4 = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification4 = new Notification.Builder(MainActivity.this)
                                .setContentTitle("文件上传完成")
                                .setContentText("上传文件完成")
                                .build();
                        notificationManager4.notify(4,notification4);
                    }
                }
            }break;

            default:break;
        }
    }

}
