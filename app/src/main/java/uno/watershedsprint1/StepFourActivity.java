package uno.watershedsprint1;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StepFourActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_four);
        final TextView clockText = (TextView) findViewById(R.id.clockText);


        CountDownTimer myTimer = new CountDownTimer(600000,1000){


            public void onTick(long millisUntilFinished) {
                int seconds = (int) ((millisUntilFinished / 1000) % 60);
                int minutes = (int) ((millisUntilFinished / 60000));
                if (seconds < 10 ) {
                    clockText.setText("0"+minutes + ":" +"0" + seconds);
                }else {
                    clockText.setText("0"+minutes+":"+seconds);
                }
            }


            public void onFinish(){
                clockText.setText("Time's Up!");

            }
        }.start();
    }




}
