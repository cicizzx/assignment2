
package com.example.myapplication2048;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import  com.example.myapplication2048.MainActivity;


class Game2048Layout extends GridLayout {

    // Set the number of cells per row
    private int myColumn = 5;
    //hold all the cells
    private Game2048Item[][] myGameItem ;
    //the horizontal and vertical margins between the cells
    private int myMargin = 9;
    //the margins between the panels
    private int myPadding;
    //Listen to the user's swipe gestures
    private GestureDetector myGestureDetector;
    //Used to check if a new value needs to be generated.
    //check if a merge has occurred
    private boolean isMergeHappen = false;
    //check if a move has occurred
    private boolean isMoveHappen = false;
    //record the score
    private int myScore = 0;
    //used to save the width of the grid
    private int myChildWidth;

    //callback interface
    private onGame2048Listener myGame2048Listener;

    //judge whether it is the first time or restart the game, on the random generation of four numbers.


    private boolean isFirst = true;


    /**
     * Enumeration, defines the user's gesture, the values are
     * up, down, left, right
     */
    private enum ACTION {
        LEFT,RIGHT,UP,DOWN
    }


    public Game2048Layout(Context context) {
        this(context,null);
    }

    public Game2048Layout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Game2048Layout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //convert the value to standard size, i.e. convert 10 to 10sp
        myMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,myMargin,getResources().getDisplayMetrics());
        // Set the margins in the Layout to be the same, first get the values of the four margins, and then take the lowest value.
        myPadding = min(getPaddingLeft(),getPaddingTop(),getPaddingRight(),getPaddingBottom());
        myGestureDetector = new GestureDetector(context,new MyGestureDetector());

    }

    // Game over callback interface
    public interface onGame2048Listener{
        //set the score
        void onScoreChange(int score);
        //The end of the game is a callback.
        void onGameOver();
    }

    public void setmGame2048Listener(onGame2048Listener mGame2048Listener) {
        this.myGame2048Listener = mGame2048Listener;
    }


    /**
     * Set the width and height of the Layout,
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //get the length of the square, take the minimum value
        int length = Math.min(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
        //Get the width of the in-game grid,
        //game grid width = (screen width - distance between inner edges of container * 2 - margin between grids * (number of grids - 1) ) / number of grids per row
        myChildWidth = (length - myPadding * 2 - myMargin * (myColumn - 1)) / myColumn;
        // Set the size of the layout
        setMeasuredDimension(length,length);
    }

    //Prevent multiple calls
    private boolean once  = false;
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(!once){
            if(myGameItem == null){
                //Initialize the array
                myGameItem = new Game2048Item[myColumn][myColumn];
            }
            for(int i = 0; i < myColumn; i++){
                for(int j = 0; j < myColumn; j++){
                    //Initialize the grid
                    Game2048Item t = new Game2048Item(getContext());
                    myGameItem[i][j] = t;

                    //define the position of the grid in the layout
                    Spec a = GridLayout.spec(i), b = GridLayout.spec(j);
                    GridLayout.LayoutParams glp = new LayoutParams(a,b);
                    // Set the width and height of the view
                    glp.height = myChildWidth;
                    glp.width = myChildWidth;
                    if( (j + 1)  != myColumn){
                        //if not the last column, add the right distance
                        glp.rightMargin = myMargin;
                    }
                    if( i > 0){
                        //If not the first row, add the top margin
                        glp.topMargin = myMargin;
                    }
                    //set to fill the whole container
                    glp.setGravity(Gravity.FILL);
                    addView(t);
                    t.setLayoutParams(glp);
                }
            }
            // Random number generation
            generateNum();
        }
        once = true;
    }


    /**
     * Get the smallest value among multiple values
     */
    private int min(int... params){
        int min = params[0];
        for(int param:params){
            min = Math.min(min, param);
        }
        return min;
    }


    /**
     * Leave the touch events to be listened to by our own defined class
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        myGestureDetector.onTouchEvent(event);
        return true;
    }

    /**
     * Inherit GestureDetector, and implement the gesture listener by itself.
     */
    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        //Define a minimum distance, if the user moves a distance greater than this minimum distance before the method will be executed
        final int FLING_MIN_DISTANCE = 33;
        /**
         * The user's finger moves quickly on the touch screen and releases the action touch method
         * @param e1 MotionEvent when first pressed.
         * @param e2 MotionEvent of the last movement.
         * @param velocityX velocity of movement on the x-axis, pixels/sec.
         * @param velocityY The velocity of the y-axis, in pixels/sec.
         * @return
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //Get the x, y coordinates of the move
            float a = e2.getX() - e1.getX(), b = e2.getY() - e1.getY();
            // Get the decision value of the movement speed of x-axis and y-axis.
            float absA = Math.abs(velocityX), absB = Math.abs(velocityY);

            // If the x-axis travels a distance greater than the minimum response distance
            // of a positive number. And the x-axis is moving faster than the y-axis,
            // then it is assumed that the user is sliding to the right
            if(a > FLING_MIN_DISTANCE && absA > absB){
                action(ACTION.RIGHT);
            }else if( a < -FLING_MIN_DISTANCE && absA > absB){
                //x is greater than the negative minimum response distance,
                // and the x-axis is moving faster than the y-axis,
                // then it is assumed that the user is sliding to the left
                action(ACTION.LEFT);
            }else if( b > FLING_MIN_DISTANCE && absA < absB){
                //Slide down
                action(ACTION.DOWN);
            }else if(b < -FLING_MIN_DISTANCE && absA < absB){
                //Slide up
                action(ACTION.UP);
            }
            return true;
        }
    }

    /**
     * According to the user's gesture, the corresponding merge operation is performed
     */
    private void action(ACTION action) {
        Log.d("zzxdebug", "action detected!" + action);
        // Get the grid with the number on the View.
        for(int i = 0 ; i < myColumn; i++){
            // An array to hold the cells in each row that are not 0.
            List<Game2048Item> rowTemp = new ArrayList<>();
            for(int j = 0;j < myColumn; j++){
                // Provide array subscripts based on gestures.
                int rowIdx = getRowIndexByAction(action,i,j);
                int colidx = getColIndexByAction(action,i,j);
                Game2048Item t = myGameItem[rowIdx][colidx];
                // Determine if there is a number in the cell, and store it in the temporary array if there is.
                if(t.getNumber() != 0){
                    rowTemp.add(t);
                }
            }

            //Determine if a move was made to prevent the user from swiping multiple times
            // in the same direction (which is actually just a swipe event),
            // then a random number will be automatically generated
            for(int j = 0 ; j < rowTemp.size(); j++){
                int rowIdx = getRowIndexByAction(action,i,j);
                int colIdx = getColIndexByAction(action,i,j);
                //Get the number in the corresponding position
                Game2048Item t = myGameItem[rowIdx][colIdx];
                // If the number in the original position does not correspond to the number in the array
                // (the number in the array has been removed from the 0's), it means that a move has occurred.
                if(t.getNumber() != rowTemp.get(j).getNumber()){
                    isMoveHappen = true;
                }
            }

            // Perform a merge operation
            mergeItem(rowTemp);
            //add the merged array to the list, with the remaining positions filled with zeros
            for(int j = 0; j < myColumn; j++){
                if(rowTemp.size() > j){
                    int number = rowTemp.get(j).getNumber();
                    switch (action){
                        case LEFT:
                            myGameItem[i][j].setNumber(number);
                            break;
                        case RIGHT:
                           //The data is acquired from left to right, so the value should be taken from the back of the array when it is assigned.
                            myGameItem[i][myColumn - j - 1].setNumber(number);
                            break;
                        case UP:
                            myGameItem[j][i].setNumber(number);
                            break;
                        case DOWN:
                            myGameItem[myColumn - j - 1][i].setNumber(number);
                            break;
                    }
                }else{
                    //
                    switch (action){
                        case LEFT:
                            myGameItem[i][j].setNumber(0);
                            break;
                        case RIGHT:
                            myGameItem[i][myColumn - j - 1].setNumber(0);
                            break;
                        case UP:
                            myGameItem[j][i].setNumber(0);
                            break;
                        case DOWN:
                            myGameItem[myColumn - j - 1][i].setNumber(0);
                            break;

                    }

                }
            }
        }
        // Generate a random number.
        generateNum();
    }

    /**
     * Root gesture to determine which direction of the array row subscript is being fetched.
     * @param action : gesture
     * @return : return row subscripts
     * The returned data is sorted to ensure that the returned data is in the same order as the fetched data.
     */
    private int getRowIndexByAction(ACTION action,int i,int j){
        int rowIdx = -1;
        switch (action){
            case UP:
                //Slide up, fill the array from top to bottom, pack the data behind the position of one to one correspondence.
                rowIdx = j;
                break;
            case DOWN:
                //Slide down to fill the array from bottom to top, to ensure that the positions correspond to each other when fetching data later.
                rowIdx = myColumn - j - 1;
                break;
            //slide left, slide right. The change is the column, so the row does not change.
            case LEFT:
            case RIGHT:
                rowIdx = i;
                break;
        }
        return rowIdx;
    }

    /**
     * Determine which direction of the array column subscript is being fetched based on the gesture.
     * @param action : gesture
     * @return : return the subscript of the column
     */
    private int getColIndexByAction(ACTION action,int i,int j){
        int ColIdx = -1;
        switch (action){
            case UP:
            case DOWN:
                //Slide up, slide down, column fixed
                //Slide down, top to bottom, column by column to get the array.
                ColIdx = i;
                break;
            case RIGHT:
                //Slide right, fetch data from right to left
                ColIdx = myColumn - j - 1;
                break;
            case LEFT:
                //Slide left, fetch data from left to right
                ColIdx = j;
                break;
        }
        return ColIdx;
    }

    /**
     * Generate a random number
     */
    private void generateNum() {
        // first check if all squares are filled, if yes, the game is over
        Log.d("zzxdebug", "generate num");
        if(isGameOver()){
            Log.e("zzxdebug", "GAME OVER");
            if(myGame2048Listener != null){
                myGame2048Listener.onGameOver();
            }
            return;
        }

        // If the game is loaded for the first time or restarted, four random numbers are generated
        if(isFirst){
            Log.d("zzxdebug", "First loaded!");
            for(int i = 0 ; i < 4; i++){
                int a = new Random().nextInt(myColumn ), b = new Random().nextInt(myColumn );
                Game2048Item t = myGameItem[a][b];
                while (t.getNumber() != 0){
                    // If there is a number on the randomly generated grid,
                    // another randomly generated grid will be generated after
                    a = new Random().nextInt(myColumn);
                    b = new Random().nextInt(myColumn);
                    t = myGameItem[a][b];
                }
                t.setNumber(Math.random() > 0.83 ? 4:2);
                // Set a display animation
                Animation scaleAnimation = new ScaleAnimation(0,1,0,1,
                        Animation.RELATIVE_TO_SELF,0.5F,Animation.RELATIVE_TO_SELF,0.5f);
                scaleAnimation.setDuration(200);
                t.startAnimation(scaleAnimation);
            }
            isMoveHappen = isMergeHappen = false;
            isFirst = false;
        }
        if(isMoveHappen && !isMergeHappen){
            // Get a random grid
            int a = new Random().nextInt(myColumn), b = new Random().nextInt(myColumn);
            Game2048Item t = myGameItem[a][b];
            while (t.getNumber() != 0){
                // If there is a number on the randomly generated grid,
                // another randomly generated grid will be generated after
                a = new Random().nextInt(myColumn);
                b = new Random().nextInt(myColumn);
                t = myGameItem[a][b];
            }
            //Math.random()Generate a random number greater than 0,less than 1.
            t.setNumber(Math.random() > 0.812 ? 4:2 );
        }
        isMergeHappen = isMoveHappen = false;
    }





    //merge array operation
    private void mergeItem(List<Game2048Item> rowTemp) {
        Log.d("zzxdebug", "Merge num");
        //If the number is only 1, then the operation will not be performed
        if (rowTemp.size() < 2){
            return;
        }
        //Loop merge to prevent some merge once and then merge again
        boolean isStop = true;
        while(isStop){
            for(int i = 0; i < rowTemp.size() - 1; i++){
                Game2048Item t1 = rowTemp.get(i);
                Game2048Item t2 = rowTemp.get(i + 1);
                //merge if the numbers are equal
                if(t1.getNumber() == t2.getNumber()){
                    //set the merge to happen
                    isMergeHappen = true;
                    int val = t1.getNumber() + t2.getNumber();
                    //add the score
                    myScore += val;
                    //set to the new number
                    t1.setNumber(val);
                    myGame2048Listener.onScoreChange(myScore);
                    //move the numbers behind it forward in order
                    for(int j = i + 1;j < rowTemp.size() - 1;j++){
                        rowTemp.get(j).setNumber(rowTemp.get(j + 1).getNumber());
                    }
                    // set the last item to 0
                    rowTemp.get(rowTemp.size() - 1).setNumber(0);
                }
            }
            //Save to check if the condition passes.
            boolean isSame = true;
            //check once if the merge is still possible
            for(int i = 0;i < rowTemp.size() - 1; i++){
                Game2048Item t1 = rowTemp.get(i);
                Game2048Item t2 = rowTemp.get(i + 1);
                if(t1.getNumber() == t2.getNumber() &&
                        t1.getNumber() != 0 ){
                    isSame = false;
                    break;
                }
            }
            if(isSame){
                // If the check passes, then the loop will be jumped
                isStop = false;
            }
        }
    }

    /**
     * Check if the number is filled
     * @return true yes,false no
     */
    private boolean ifFull() {
        Log.d("zzxdebug", "the number is filled");
        for(int i = 0; i < myColumn; i++){
            for(int j = 0 ; j < myColumn; j++){
                Game2048Item item = myGameItem[i][j];
                if(item.getNumber() == 0){
                    return false;
                }
            }
        }
        return true;
    }

    //Restart game
    public void reStart(){
        Log.d("zzxdebug", "Game restart!");
        for(int i = 0; i < myColumn; i++){
            for(int j = 0 ; j < myColumn; j++){
                Game2048Item t = myGameItem[i][j];
                t.setNumber(0);
            }
        }
        myScore = 0;
        myGame2048Listener.onScoreChange(0);
        isFirst = true;
        generateNum();
    }

    /**
     * Check if the game is over
     */
    private boolean isGameOver(){
        //If the grid is not yet filled, it means it is not finished
        if(!ifFull()){
            return false;
        }
        //If the grid is filled, then check if the adjacent subitems have the same number
        for(int i = 0 ; i < myColumn ; i++){
            for(int j = 0; j < myColumn ; j++){
                Game2048Item t = myGameItem[i][j];
                // If it is not the last column, then compare it with the item on the right
                if( (j + 1) != myColumn){
                    Game2048Item right = myGameItem[i][j + 1];
                    if(t.getNumber() == right.getNumber()){
                        return false;
                    }
                }
                // If it is not the first column, then it will be compared to the left term
                if( j != 0){
                    Game2048Item left = myGameItem[i][j - 1];
                    if(t.getNumber() == left.getNumber()){
                        return false;
                    }
                }
                // If last line, then compare with next line
                if( (i + 1) != myColumn){
                    Game2048Item bottom = myGameItem[i + 1][j];
                    if(t.getNumber() == bottom.getNumber()){
                        return false;
                    }
                }
                //If not the first line, compare with the previous line
                if( i != 0){
                    Game2048Item top = myGameItem[i - 1][j];
                    if(t.getNumber() == top.getNumber()){
                        return false;
                    }
                }
            }
        }
        return true;
    }




}

