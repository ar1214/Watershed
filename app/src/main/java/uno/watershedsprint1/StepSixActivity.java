package uno.watershedsprint1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StepSixActivity extends AppCompatActivity {

    //just navigation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_six);
    }


    public void onClickToStepSix(View v){
        startActivity(new Intent(StepSixActivity.this, DataEntry.class));
    }
}
