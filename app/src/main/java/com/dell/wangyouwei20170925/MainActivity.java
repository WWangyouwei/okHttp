package com.dell.wangyouwei20170925;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.OKhttpManager;

public class MainActivity extends AppCompatActivity {
    private OKhttpManager manager = OKhttpManager.getInstance();
    private String url="http://news-at.zhihu.com/api/4/news/latest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager.asyncJsonStringByURL(url, new OKhttpManager.Func1() {
            @Override
            public void onResponse(String result) {
                Toast.makeText(MainActivity.this,result,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
