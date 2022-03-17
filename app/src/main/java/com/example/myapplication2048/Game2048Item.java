
package com.example.myapplication2048;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * The grid view inside the game interface
 */
public class Game2048Item extends View{

    private Paint paint; //The paint brush tool
    private int mNumber; //The number on the View.
    private String mNumberVal; //the number on the View, String type
    private int fontSize = 100; //Save the size of the number displayed on the View
    private Rect mBound; //the area to draw the text

    public Game2048Item(Context context) {
        this(context,null);
    }

    public Game2048Item(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Game2048Item(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }


    // return the current number on the grid
    public int getNumber(){
        return mNumber;
    }

    // set the number of the current grid
    public void setNumber(int mNumber) {
        this.mNumber = mNumber;
        mNumberVal = mNumber + "";
        // number number size
        paint.setTextSize(fontSize);
        mBound = new Rect();
        /**
         * Get the smallest rectangle corresponding to the specified string, using the location of the (0, 0) point as the baseline
         * @param text The string to measure the smallest rectangle
         * @param start The index of the starting character in the string to be measured
         * @param end The length of the character to be measured
         * @param bounds Receive the result of the measurement
         */
        paint.getTextBounds(mNumberVal,0,mNumberVal.length(),mBound);
        // force repaint
        invalidate();
    }

    /**
     * Depending on the number on the grid, draw a different background color
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String mBgColor = ""; //Save the background color for drawing
        switch (mNumber){
            case 0: // color of the grid when there is no number
                mBgColor = "#CCC0B3";
                break;
            case 2:
                mBgColor = "#EEE4DA";
                break;
            case 4:
                mBgColor = "#EDE0C8";
                break;
            case 8:
                mBgColor = "#F2B179";
                break;
            case 16:
                mBgColor = "#F49563";
                break;
            case 32:
                mBgColor = "#F57940";
                break;
            case 64:
                mBgColor = "#F55D37";
                break;
            case 128:
                mBgColor = "#EEE863";
                break;
            case 256:
                mBgColor = "#EDB040";
                break;
            case 512:
                mBgColor = "#ECB040";
                break;
            case 1024:
                mBgColor = "#EB9437";
                break;
            case 2048:
                mBgColor = "EA7821";
                break;
            default:
                mBgColor = "#EA7821";
                break;
        }
        // Set the color of the brush
        paint.setColor(Color.parseColor(mBgColor));
        // There are three styles:
        // Paint.Style.STROKE Stroke.
        // FILL Fill.
        // FILL_AND_STROKE Stroke and fill
        paint.setStyle(Paint.Style.FILL);
        /**
         * Draws a rectangle
         * The first parameter: the left position of the rectangle
         * The second parameter: the top position of the rectangle
         * The third parameter:the right position of the rectangle
         * The fourth parameter: the lower position of the rectangle
         * The fifth parameter: the brush tool
         */

        canvas.drawRect(0,0,getWidth(),getHeight(),paint);

        if(mNumber != 0){
            drawText(canvas);
        }
    }

    //Drawing text
    private void drawText(Canvas canvas) {
        paint.setColor(Color.BLACK);
        float x = (getWidth() - mBound.width())/2;
        float y = getHeight()/2 + mBound.height()/2;
        canvas.drawText(mNumberVal,x,y,paint);
    }

    // Set the font size.
    public void setFontSize(int size){
        fontSize = size;
    }
}

