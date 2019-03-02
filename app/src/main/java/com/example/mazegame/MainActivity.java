//created by Johnavan Thomas - 100966681

package com.example.mazegame;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends Activity {

    private LinearLayout gameLayout; // layout for buttons
    private mazeSolver[][] gamebutton; // grid for buttons


    private static final int NUM_COL = 10; // for 10 buttons per row
    private static final int MARGIN = 1; // necessary for various alignments

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameLayout = findViewById(R.id.mazeContainer);
        gameLayout.post(new Runnable() {
            @Override
            public void run() {
                layoutMaker(gameLayout.getWidth(), gameLayout.getHeight());
            }
        });
    }


    private void layoutMaker (int gameWidth, int gameHeight) {  //inflate layout here


        int NUM_LEN = gameWidth / NUM_COL;
        int NUM_ROW = gameHeight / NUM_LEN;    // Calculate the width & height for each button


        LinearLayout.LayoutParams layoutPrefrences =
                new LinearLayout.LayoutParams(NUM_LEN - MARGIN * 2,
                        NUM_LEN - MARGIN * 2);
        layoutPrefrences.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
        
        
        gamebutton = new mazeSolver[NUM_ROW][NUM_COL];
        
        for (int horizontalButtons = 0; horizontalButtons < NUM_ROW; horizontalButtons++) {
            
            LinearLayout rowLayout = new LinearLayout(this); // layout for row buttons
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);

            for (int verticalButtons = 0; verticalButtons < NUM_COL; verticalButtons++) {

                final mazeSolver mButton = new mazeSolver(this);  // create a new button
                mButton.button.setLayoutParams(layoutPrefrences);
                mButton.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mButton.onClick();
                    }
                });

                mButton.horizontalPosition = horizontalButtons;
                mButton.verticalPosition = verticalButtons;

                gamebutton[horizontalButtons][verticalButtons] = mButton;
                rowLayout.addView(mButton.button);
            }
            gameLayout.addView(rowLayout);
        }

        gamebutton[0][0].updateButton(mazeSolver.START);
        gamebutton[NUM_ROW - 1][NUM_COL - 1].updateButton(mazeSolver.END);
    }


    public void onClickSolve(View view) {

        if (mazeSolver.chooseStart || mazeSolver.chooseEND) {       // disabling buttons during selection
            showToast("Please select a START/END position");
            return;
        }

        if (mazeSolver.solving) {
            mazeSolver.solving = false;
            mazeSolver.chooseEND = false;
            mazeSolver.chooseStart = false;
            recreate();
            return;
        }

        mazeSolver.solving = true;

        Button button = ((Button) view);
        button.setText("Reset Maze");


        new Thread(new Runnable() { // new thread for solving method
            @Override
            public void run() {

                if (solveMaze(gamebutton, mazeSolver.startingXPosition,
                        mazeSolver.startingYPosition)) {
                    showToast("Solved");
                } else {
                    showToast("Cannot solve");
                }
            }
        }).start();
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }



    private static boolean solveMaze( mazeSolver[][] mazeGrid, int currentPositionHorizontal, int currentPositionVertical) {    // MAZE SOLVING METHOD

        if (!mazeSolver.solving) {   // stop processing when user presses the reset
            return false;
        }


        if (currentPositionHorizontal < 0 || currentPositionVertical < 0 ||
                currentPositionHorizontal >= mazeGrid.length ||
                currentPositionVertical >= mazeGrid[0].length) {
            return false;
        }

        mazeSolver current = mazeGrid[currentPositionHorizontal]    // if current position is a wall or already visited one, return false.
                [currentPositionVertical];
        if (current.getButton() == mazeSolver.WALL ||
                current.getButton() == mazeSolver.PATH) {
            return false;
        }

        if (current.getButton() == mazeSolver.END) {   // maze solved when end button is found
            return true;
        }


        if (mazeGrid[currentPositionHorizontal][currentPositionVertical].notValidButton) return false; // if position is not valid then maze not solved

        mazeGrid[currentPositionHorizontal][currentPositionVertical].
                updateButton(mazeSolver.PATH);


        //  Solving method is as follows:
        // go down until not able to
        // go right until not able to
        // go up until not able to
        // go left until not able to
        // if end has not be reached and unable to go in anymore directions then cannot solve

        try {
            Thread.sleep(100);
        } catch (InterruptedException ignore) {
        }

        //  GO DOWN
        if (solveMaze(mazeGrid, currentPositionHorizontal
                + 1, currentPositionVertical)) {
            return true;
        }
        //  GO RIGHT

        if (solveMaze(mazeGrid, currentPositionHorizontal,
                currentPositionVertical + 1)) {
            return true;
        }

        //  GO UP
        if (solveMaze(mazeGrid,
                currentPositionHorizontal - 1, currentPositionVertical)) {
            return true;
        }

        //  GO LEFT
        if (solveMaze(mazeGrid, currentPositionHorizontal,
                currentPositionVertical - 1)) {
            return true;
        }

        // END
        mazeGrid[currentPositionHorizontal][currentPositionVertical].
                updateButton(mazeSolver.NOTVALID);
        mazeGrid[currentPositionHorizontal][currentPositionVertical].
                notValidButton = true;
        return false;
    }

}