package com.chang.weight.pie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.chang.weight.utils.MathUtil;

import java.util.Arrays;
import java.util.List;

public class Pie extends View {

    private List<PieEntity> entities;
    private int width, hight;//屏幕的宽高
    private int radius;//圆形半径
    private RectF mRectF;//扇形的外接矩形
    private RectF mTouchRectF;//点击后，延长的扇形的外接矩形
    private Paint mPaint;//绘制扇形的画笔
    private Path mPath;//绘制扇形用到的path
    private Paint mLinePaint;//绘制黑线的画笔
    private float[] startAngles;//扇形的开始角度
    private int position = -1;//点击选中扇形的position

    public Pie(Context context) {
        this(context, null);
    }

    public Pie(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Pie(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRectF = new RectF();
        mTouchRectF = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPath = new Path();
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(3);
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setTextSize(40);
    }

    //onLayout()中view大小改变时调用
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.hight = h;
        int min = Math.min(w, h);
        //圆的半径取屏幕宽高的最小值的70%的一半
        radius = (int) ((min * 0.7f / 2) + 0.5f);

        mRectF.left = -radius;
        mRectF.top = -radius;
        mRectF.right = radius;
        mRectF.bottom = radius;

        mTouchRectF.left = -radius - 15;
        mTouchRectF.top = -radius - 15;
        mTouchRectF.right = radius + 15;
        mTouchRectF.bottom = radius + 15;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        //移动画布，以圆心为坐标原点，以便于计算
        canvas.translate(width / 2, hight / 2);
        //绘制
        drawPie(canvas);
        canvas.restore();
    }

    private void drawPie(Canvas canvas) {
        float startAngle = 0;
        for (int i = 0; i < entities.size(); i++) {
            //绘制扇形；sweepAngle - 1是为了扇形之间的间隙
            PieEntity pieEntity = entities.get(i);
            mPaint.setColor(pieEntity.getColor());
            mPath.moveTo(0, 0);
            float sweepAngle = pieEntity.getValue() * 360 / 100;
            if (position == i) {
                mPath.arcTo(mTouchRectF, startAngle, sweepAngle - 1);
            } else {
                mPath.arcTo(mRectF, startAngle, sweepAngle - 1);
            }
            canvas.drawPath(mPath, mPaint);

            //绘制黑线
            double a = Math.toRadians(startAngle + sweepAngle / 2);
            float startX = (float) (radius * Math.cos(a));
            float startY = (float) (radius * Math.sin(a));
            float endX = (float) ((radius + 50) * Math.cos(a));
            float endY = (float) ((radius + 50) * Math.sin(a));
            canvas.drawLine(startX, startY, endX, endY, mLinePaint);

            //绘制文本
            String value = String.format("%.1f", pieEntity.getValue()) + "%";
            if (startAngle % 360.0f <= 90.0f || startAngle % 360.0f >= 270f) {
                if (startAngle % 360.0f <= 90.0f && startAngle % 360.0f > 45f && sweepAngle > 45) {
                    float v = mLinePaint.measureText(value);
                    canvas.drawText(value, endX - v, endY, mLinePaint);
                } else {
                    canvas.drawText(value, endX, endY, mLinePaint);
                }
            } else {
                float v = mLinePaint.measureText(value);
                canvas.drawText(value, endX - v, endY, mLinePaint);
            }

            startAngles[i] = startAngle;
            startAngle += sweepAngle;
            //每次绘制扇形之后，要对path进行重置操作，这样就可以清除上一次绘制path的画笔的相关记录
            mPath.reset();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                x = x - width / 2;
                y = y - hight / 2;
                //触摸点的角度
                float touchAngle = MathUtil.getTouchAngle(x, y);
                //触摸点距离圆心的距离
                double touchRadiu = Math.sqrt(x * x + y * y);
                if (touchRadiu < radius) {
                    //binarySearch在数组中查找一个数，找到则返回索引；
                    //没有找到则返回： -(第一个大于搜索值的值的索引+1)
                    int searchResult = Arrays.binarySearch(startAngles, touchAngle);
                    if (searchResult > 0) {
                        position = searchResult;
                    } else {
                        position = -searchResult - 1 - 1;
                    }
                    invalidate();
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setData(List<PieEntity> entities) {
        this.entities = entities;
        startAngles = new float[entities.size()];
    }
}
