package com.example.mark.scarnesdice;

import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private boolean isPlayerTurn = true;
    private long playerScore = 0, computerScore = 0, turnScore = 0;
    private Button roll, hold, reset;
    private ImageView diceImage;
    private TextView scoreText;
    private int currentDiceValue = 1;
    private String SCOREPLAYER = "Your Score : ";
    private String SCORECOMPUTER = "Computer Score : ";
    private String SCORETURN = "Turn Score : ";
    int images[] = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreText = findViewById(R.id.userLabel);
        diceImage =  findViewById(R.id.dice);
        roll =  findViewById(R.id.roll);
        hold =  findViewById(R.id.hold);
        reset =  findViewById(R.id.reset);
        updateUi();

        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlayerTurn)
                    roll();
            }
        });
        hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlayerTurn)
                    hold();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
    }

    private void roll() {
        currentDiceValue = new Random().nextInt(6) + 1;
        if (currentDiceValue == 1) {
            turnScore = 0;
            hold();
        } else {
            turnScore += currentDiceValue;
        }
        updateUi();
    }

    private void hold() {
        if (isPlayerTurn)
            playerScore += turnScore;
        else
            computerScore += turnScore;
        turnScore = 0;
        currentDiceValue = 1;
        updateUi();
        isPlayerTurn = !isPlayerTurn;
        if(computerScore>100|| playerScore>100)
        {
            Toast.makeText(this,(computerScore>100?"Computer":"Player")+" won",Toast.LENGTH_SHORT).show();
            reset();
        }
        if (!isPlayerTurn) {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerTurn();
                }
            },1000);
        }
    }


    private void reset() {
        playerScore = turnScore = computerScore = 0;
        isPlayerTurn = true;
        updateUi();
    }

    private void computerTurn() {
        if (!isPlayerTurn) {
            if (turnScore < 20) {
                roll();
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        computerTurn();
                    }
                },1000);
            } else
                hold();
        }
    }

    private void updateUi() {
        scoreText.setText(SCOREPLAYER + playerScore + "\n" + SCORECOMPUTER + computerScore + "\n" + SCORETURN + turnScore);
        diceImage.setImageResource(images[currentDiceValue - 1]);
    }

}