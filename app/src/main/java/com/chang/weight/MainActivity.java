package com.chang.weight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chang.weight.icon.IconActivity;
import com.chang.weight.pie.PieActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pieClick(View v) {
        startActivity(new Intent(this, PieActivity.class));
    }

    public void iconClick(View v) {
        startActivity(new Intent(this, IconActivity.class));
    }
}
