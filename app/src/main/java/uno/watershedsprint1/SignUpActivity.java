package uno.watershedsprint1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.AsyncTask;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class SignUpActivity extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_sign_up);

        Button btn = (Button) findViewById(R.id.signUp);
        final String url = "http://newatershed.net/api/users/";








// when button is pressed, start background thread to send api call

        btn.setOnClickListener(new View.OnClickListener(){
            @Override

                public void onClick(View view){
                MediaType MEDIA_TYPE_MARKDOWN
                        = MediaType.parse("text/x-markdown; charset=utf-8");

                //get responses from screen
                EditText first = (EditText) findViewById(R.id.first_name);
                EditText last = (EditText) findViewById(R.id.last_name);
                EditText email = (EditText) findViewById(R.id.email);
                EditText address = (EditText) findViewById(R.id.address);
                EditText city = (EditText) findViewById(R.id.city);
                EditText state = (EditText) findViewById(R.id.state);
                EditText zip = (EditText) findViewById(R.id.zip);
                EditText affiliation = (EditText) findViewById(R.id.affiliation);
                EditText password = (EditText) findViewById(R.id.password);
                EditText passwordConfirm = (EditText) findViewById(R.id.verify_password);

                //convert them to strings
                String firstS = first.getText().toString();
                String lastS = last.getText().toString();
                String emailS = email.getText().toString();
                String addressS = address.getText().toString();
                String cityS = city.getText().toString();
                String stateS = state.getText().toString();
                String zipS = zip.getText().toString();
                String affiliationS = affiliation.getText().toString();
                String passwordS = password.getText().toString();
                String passwordConfirmS = passwordConfirm.getText().toString();

                /// make params array
                String[] params = {firstS, lastS, emailS, addressS, affiliationS, passwordS, passwordConfirmS, cityS, stateS,zipS};
                //gogogo
                SendTask myTask = new SendTask();
                String responseString = myTask.doInBackground(params);


                }


        });


    }

    //communication with db
    private class SendTask extends AsyncTask<String, Void, String> {
        Boolean worked = false;
        ProgressDialog authProgressDialog;
        protected void onPreExectute(){
            authProgressDialog.setTitle("Please wait");
            authProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params){
            if (Looper.myLooper() == null)
            {
                Looper.prepare();
            }

            try {

                //build okhttp request
                MediaType mediaType = MediaType.parse("application/json");
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormEncodingBuilder()
                        .add("email", params[2])
                        .add("password", params[5])
                        .add("password_verify",params[6])
                        .add("first_name", params[0])
                        .add("last_name", params[1])
                        .add("address", params[3])
                        .add("city", params[7])
                        .add("state", params[8])
                        .add("zip", params[9])
                        .build();

                Request request = new Request.Builder()
                        .url("http://newatershed.net/api/users/")
                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    worked = true;

                }
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            }catch(Exception e){e.printStackTrace();}


            if (worked) {
                //set token
               // String token;
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                //send to new page
                return (null);
            }
            else{
                startActivity(new Intent(SignUpActivity.this, emailActivity.class));
                return (null);
            }
        }




        protected void onPostExecute(Void result){
            authProgressDialog.dismiss();
        }




}



    public void onClickToHomePage(View v){
        startActivity(new Intent(SignUpActivity.this, HomePage.class));
    }
}
