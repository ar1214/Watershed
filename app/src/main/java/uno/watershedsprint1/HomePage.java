package uno.watershedsprint1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public void onClickToDataEntry(View v){
        startActivity(new Intent(HomePage.this, DataEntry.class));
    }

    public void onClickToWizardStart(View v){
        startActivity(new Intent(HomePage.this, WizardStart.class));
    }

    public void onClickToEmail(View v){
        startActivity(new Intent(HomePage.this, emailActivity.class));
    }
    public void onClickToMyData(View v){
        startActivity(new Intent(HomePage.this, MyDataActivity.class));
    }
}
