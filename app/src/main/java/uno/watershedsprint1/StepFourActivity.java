package uno.watershedsprint1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StepFourActivity extends AppCompatActivity {

    //just navigation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_four);
    }



    public void onClickToStepFive(View v){
        startActivity(new Intent(StepFourActivity.this, StepFiveActivity.class));
    }
}
