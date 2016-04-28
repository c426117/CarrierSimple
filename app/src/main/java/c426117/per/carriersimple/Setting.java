package c426117.per.carriersimple;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Setting extends AppCompatActivity {

    LinearLayout settingDownload = null;

    private String url = null;

    TextView location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        /*
        SharedPreferences pref = getSharedPreferences("setting",MODE_PRIVATE);
        location = (TextView)findViewById(R.id.download_place);
        location.setText(pref.getString("location","/storage/emulated/0"));
        settingDownload = (LinearLayout)findViewById(R.id.download_setting);
        settingDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "请选择目录"), 1);
            }
        });
        */
    }
    /*
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
                    url = data.getData().toString();
                    location = (TextView)findViewById(R.id.download_place);
                    location.setText(url);
                    SharedPreferences.Editor editor = getSharedPreferences("setting",MODE_PRIVATE).edit();
                    editor.putString("location",url);
                    editor.commit();
                    MainActivity.downloadPlace = url;
                }
            }break;

            default:break;
        }
    }
    */
}
