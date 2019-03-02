package com.example.mazegame;


import android.content.Context;
import android.graphics.Color;
import android.widget.Button;
import android.widget.Toast;

public class mazeSolver {
    
    public static final int EMPTY = 0;
    public static final int WALL = 1;
    public static final int START = 2;
    public static final int END = 3;
    public static final int PATH = 4;
    public static final int NOTVALID = 5;
    
    public Button button;  // button element
    
   
    public static boolean chooseStart, chooseEND, solving;  // used to determine what is going on in the application
    public static int startingXPosition, startingYPosition; // start and end buttons for the grid


    public int horizontalPosition, verticalPosition; // x,y of button's location
    private int currButton = EMPTY;  //current position of button
    public boolean notValidButton = false; //check if button is valid



    public mazeSolver (Context context) {
        button = new Button(context);
        button.setPadding(0, 0, 0, 0);
        button.setTextColor(Color.WHITE);
        redraw();
    }


    public void onClick() { // button clicked

        if (solving) return;


        if (currButton == START) { // START button is clicked

            // following two cases are for if the START and the END buttons are  chosen to be in the same posiiton

            if (chooseEND) {
                Toast.makeText(button.getContext(), "Start and End buttons cannot be placed in the same position.", Toast.LENGTH_SHORT).show();
                return;
            }

            updateButton(WALL);
            Toast.makeText(button.getContext(), "Choose a Start position.", Toast.LENGTH_SHORT).show();
            mazeSolver.chooseStart = true;

        } else if (currButton == END) {

            if (chooseStart) {
                Toast.makeText(button.getContext(), "Start and End buttons cannot be placed in the same position.", Toast.LENGTH_SHORT).show();
                return;
            }

            updateButton(WALL);
            Toast.makeText(button.getContext(), "Choose an Exit position.", Toast.LENGTH_SHORT).show();
            mazeSolver.chooseEND = true;

        } else if (chooseStart) {

            updateButton(START);   // change start button position
            chooseStart = false;
            chooseEND = false;

        } else if (chooseEND) {

            updateButton(END);   // change end button position
            chooseStart = false;
            chooseEND = false;

        } else {

            switch (currButton) {
                case EMPTY:
                    updateButton(WALL); // empty button to wall, and vice versa
                    break;
                case WALL:
                    updateButton(EMPTY);
                    break;
            }
        }
    }





    private void redraw() { // Updating the UI


        button.setBackgroundResource(R.color.colorPrimary);
        button.setText(null);

        switch (currButton) { // creating, adding any necessary text, and adding color to buttons
            case EMPTY:
                break;
            case WALL:
                button.setBackgroundResource(R.color.wall);
                button.setText("W");
                break;
            case START:
                button.setBackgroundResource(R.color.start);
                break;
            case END:
                button.setBackgroundResource(R.color.end);
                break;
            case NOTVALID:
                button.setBackgroundResource(R.color.colorAccent);
                break;
            case PATH:
                button.setBackgroundResource(R.color.path);
                button.setText("P");
                break;
        }

        if (currButton == START || currButton == END) {
            button.setText(currButton == START ? "S" : "E");
        }
    }

    public void updateButton(int state) { //updating the button
        this.currButton = state;
        if (state == START) {
            startingXPosition = horizontalPosition;
            startingYPosition = verticalPosition;
        }
        button.post(new Runnable() {
            @Override
            public void run() {
                redraw();
            }
        });
    }

    public int getButton() { // get the button
        return currButton;
    }

}
