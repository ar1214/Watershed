package uno.watershedsprint1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WizardStart extends AppCompatActivity {

    //just navigation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_start);
    }

    public void onClickToStepOne(View v){
        startActivity(new Intent(WizardStart.this, StepOneActivity.class));
    }
}
