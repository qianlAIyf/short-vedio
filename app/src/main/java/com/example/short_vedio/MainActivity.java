package com.example.short_vedio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button buttonIssue, buttonPhoto, buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonIssue = findViewById(R.id.btn_issue);
        buttonPhoto = findViewById(R.id.btn_photo);
        buttonSubmit = findViewById(R.id.btn_submit);

        //设置三个监听事件
        buttonIssue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,VedioIssue.class));
            }
        });
        buttonPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,VedioPhoto.class));
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,VedioSubmit.class));
            }
        });
    }
}
