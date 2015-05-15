package com.EE5.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class Axis extends ImageView{
    Paint paint = new Paint();

    private Vector xVector = new Vector(1,0);
    private Vector yVector = new Vector(0,1);

    public Axis(Context context) {
        super(context);
        /*int centreX = this.getWidth()/2;
        int centreY = this.getHeight()/2;
        this.setRotationX(centreX);
        this.setRotationY(centreY);*/
    }

    public Axis(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {
        /*paint.setColor(Color.RED);
        paint.setStrokeWidth(4);

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);

        int scale = 50;
        int centreX = this.getWidth()/2;
        int centreY = this.getHeight()/2;
        canvas.drawLine(centreX, centreY,
                (float) (centreX +  xVector.getX()*scale),
                (float) (centreY - xVector.getY()*scale),
                paint);
        canvas.drawLine(centreX, this.getHeight()/2,
                (float) (centreX + yVector.getX()*scale),
                (float) (centreY - yVector.getY()*scale)
                , paint);*/
        super.onDraw(canvas);

        //this.setRotation();
    }

    public Vector getxVector() {
        return xVector;


    }

    public void setxVector(Vector xVector) {
        this.xVector = xVector;
    }

    public Vector getyVector() {
        return yVector;
    }

    public void setyVector(Vector yVector) {
        this.yVector = yVector;
    }
}
