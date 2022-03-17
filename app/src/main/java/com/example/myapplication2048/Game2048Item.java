
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

    private Paint myPaint; //The paint brush tool
    private int myNumber; //The number on the View.
    private String myNumberValue; //the number on the View, String type
    private int defaultFontSize = 95; //Save the size of the number displayed on the View
    private Rect myBound; //the area to draw the text

    public Game2048Item(Context c) {
        this(c,null);
    }

    public Game2048Item(Context c, AttributeSet a) {
        this(c, a,0);
    }

    public Game2048Item(Context c, AttributeSet a, int defaultStyle) {
        super(c, a, defaultStyle);
        myPaint = new Paint();
    }

    // Set the font size.
    public void setFontSize(int frontsize){
        defaultFontSize = frontsize;
    }


    // set the number of the current grid
    public void setNumber(int myNumber) {
        this.myNumber = myNumber;
        myNumberValue = Integer.toString(myNumber);
        myBound = new Rect();
        // number number size
        myPaint.setTextSize(defaultFontSize);
        /**
         * Get the smallest rectangle corresponding to the specified string, using the location of the (0, 0) point as the baseline
         * @param text The string to measure the smallest rectangle
         * @param start The index of the starting character in the string to be measured
         * @param end The length of the character to be measured
         * @param bounds Receive the result of the measurement
         */
        myPaint.getTextBounds(myNumberValue,0,myNumberValue.length(),myBound);
        // force repaint
        invalidate();
    }
    // return the current number on the grid
    public int getNumber(){
        return myNumber;
    }



    /**
     * According to the number on the grid, draw a different background color
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String myBackgroundColor = ""; //Save the background color for drawing
        switch (myNumber){
            case 0: // color of the grid when there is no number
                myBackgroundColor = "#CCC0B3";
                break;
            case 2:
                myBackgroundColor = "#EEE4DA";
                break;
            case 4:
                myBackgroundColor = "#EDE0C8";
                break;
            case 8:
                myBackgroundColor = "#F2B179";
                break;
            case 16:
                myBackgroundColor = "#F49563";
                break;
            case 32:
                myBackgroundColor = "#F57940";
                break;
            case 64:
                myBackgroundColor = "#F55D37";
                break;
            case 128:
                myBackgroundColor = "#EEE863";
                break;
            case 256:
                myBackgroundColor = "#EDB040";
                break;
            case 512:
                myBackgroundColor = "#ECB040";
                break;
            case 1024:
                myBackgroundColor = "#EB9437";
                break;
            case 2048:
                myBackgroundColor = "EA7821";
                break;
            default:
                myBackgroundColor = "#EA7821";
                break;
        }
        // Set the color of the brush
        myPaint.setColor(Color.parseColor(myBackgroundColor));
        // There are three styles:
        // Paint.Style.STROKE Stroke.
        // FILL Fill.
        // FILL_AND_STROKE Stroke and fill
        myPaint.setStyle(Paint.Style.FILL);
        /**
         * Draws a rectangle
         * The first parameter: the left position of the rectangle
         * The second parameter: the top position of the rectangle
         * The third parameter:the right position of the rectangle
         * The fourth parameter: the lower position of the rectangle
         * The fifth parameter: the brush tool
         */

        canvas.drawRect(0,0,getWidth(),getHeight(),myPaint);

        if(myNumber != 0){
            drawText(canvas);
        }
    }

    //Drawing text
    private void drawText(Canvas canvas) {
        myPaint.setColor(Color.BLACK);
        float x = (getWidth() - myBound.width())/2;
        float y = getHeight()/2 + myBound.height()/2;
        canvas.drawText(myNumberValue,x,y,myPaint);
    }

}

