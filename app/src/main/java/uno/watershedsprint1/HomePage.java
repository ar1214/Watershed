package uno.watershedsprint1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


//just navigation here. Logout is ready (onClickToLogOut) which just sets token to junk.  Was not part of final UI so was removed.
//onClickToDataEntry ready as well, again, not in the final UI as was delivered to developer.

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }
/*
    public void onClickToDataEntry(View v){
        startActivity(new Intent(HomePage.this, DataEntry.class));
    }
*/
    public void onClickToWizardStart(View v){
        startActivity(new Intent(HomePage.this, WizardStart.class));
    }

    public void onClickToEmail(View v){
        startActivity(new Intent(HomePage.this, emailActivity.class));
    }
    public void onClickToMyData(View v){
        startActivity(new Intent(HomePage.this, MyDataActivity.class));
    }
    public void onClickToProjects(View v){
        startActivity(new Intent(HomePage.this, Projects_Activity.class));
    }
 /*
    public void onClickToLogOut(View v){
        Globals g = Globals.getInstance();
        g.setToken("z");
        startActivity(new Intent(HomePage.this, LoginActivity.class));
    }
    */
}
