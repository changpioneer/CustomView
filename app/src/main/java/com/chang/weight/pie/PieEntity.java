package com.chang.weight.pie;

public class PieEntity {

    //每个扇形的占比
    private float value;
    //每个扇形的颜色
    private int color;

    public PieEntity(float value, int color) {
        this.value = value;
        this.color = color;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
