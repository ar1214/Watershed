package uno.watershedsprint1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class emailActivity extends AppCompatActivity {
    Button sendEmail;
    EditText message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);


        sendEmail = (Button) findViewById(R.id.sendEmailButton1);
        message = (EditText)findViewById(R.id.emailText);

        sendEmail.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                message = (EditText) findViewById(R.id.emailText);
                String myMessage = message.getText().toString();
                
                sendEmail(myMessage);
                
            }


        });


        message.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard(v);
                }
            }
        });
    }

    public void onClickToHomePage(View v){
        startActivity(new Intent(emailActivity.this, HomePage.class));
    }

    protected void sendEmail(String myMessage) {

        String[] to =new String[] {getResources().getString(R.string.AAemailToSendTo)};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.AAemailSubject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, myMessage);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email"));
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(message.getWindowToken(), 0);
    }
}