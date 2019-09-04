package com.chang.weight.pie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.chang.weight.R;

import java.util.ArrayList;
import java.util.List;

public class PieActivity extends AppCompatActivity {

    //扇形的颜色和占比
    private int[] colors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69,
            0xFF808080, 0xFFE6B800, 0xFF7CFC00};
    private float[] values = {17.8f, 2.2f,  4.4f, 15.6f, 8.9f,20.0f, 11.1f, 13.3f, 6.7f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);
        initPie();

    }

    private void initPie() {
        Pie pieView = (Pie) findViewById(R.id.pie);

        List<PieEntity> entities = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            entities.add(new PieEntity(values[i], colors[i]));
        }
        pieView.setData(entities);
    }
}
