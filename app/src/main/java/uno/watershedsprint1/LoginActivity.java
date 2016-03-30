package uno.watershedsprint1;

//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.annotation.TargetApi;
import android.app.Activity;
//import android.app.ProgressDialog;
import android.content.Intent;
//import android.content.pm.PackageManager;
import android.os.Looper;
//import android.support.annotation.NonNull;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
//import android.app.LoaderManager.LoaderCallbacks;

//import android.content.CursorLoader;
//import android.content.Loader;
//import android.database.Cursor;
//import android.net.Uri;
import android.os.AsyncTask;

//import android.os.Build;
import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
import android.util.Log;
import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
//import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

//import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;

//import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button;
        button = (Button) findViewById(R.id.email_sign_in_button);



        //set listener to send info

        button.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View view){
                MediaType MEDIA_TYPE_MARKDOWN
                        = MediaType.parse("text/x-markdown; charset=utf-8");

                //get responses from screen

                EditText email = (EditText) findViewById(R.id.email);
                EditText password = (EditText) findViewById(R.id.password);

                //convert them to strings

                String emailS = email.getText().toString();
                String passwordS = password.getText().toString();

                /// make params array
                String[] params = {emailS, passwordS};

                //execute send
                SendTask myTask = new SendTask();
                myTask.execute(params);
            }
        });
    }

    //SendTask is where the communication with the server takes place

    private class SendTask extends AsyncTask<String, Void, String> {
        Boolean worked = false;
        //progressDialog a wip
        /*ProgressDialog authProgressDialog;
        protected void onPreExectute(){
            authProgressDialog.setTitle("Please wait");
            authProgressDialog.show();
        }*/

        @Override
        protected String doInBackground(String... params){
            if (Looper.myLooper() == null)
            {
                Looper.prepare();
            }

            try {


                MediaType mediaType = MediaType.parse("application/json");

                //start httpclient for api communication
                OkHttpClient client = new OkHttpClient();
                //build body
                RequestBody formBody = new FormEncodingBuilder()
                        .add("email", params[0])
                        .add("password", params[1])
                        .build();
                //build request
                Request request = new Request.Builder()
                        .url("http://newatershed.net/api/users/token")
                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .post(formBody)
                        .build();
                try{
                //send
                Response response = client.newCall(request).execute();
                //get response in string
                String jsonData = response.body().string();
                //put it in json
                JSONObject Jobject = new JSONObject(jsonData);
                //set up singleton class
                Globals g = Globals.getInstance();
                Log.d("Did it get here", request.toString());

                //get data section, then split it on ':' to get token
                String tok = Jobject.getString("data");
                String[] tokparse = tok.split(":");
                //put token in tok then send to singleton 'g'
                tok = tokparse[1];
                //get rid of quotations
                tok = tok.substring(1,tok.length()-1);
                //Log.d("TOKEN ATTEMPT", tok);
                g.setToken(tok);
                //Log.d("did it make it?????????", g.getToken());

                //Go through json to find token, set it to Global token using singleton class Globals

                //Headers responseHeaders = response.headers();
                //debug stuff below
                /*for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.d("DEBUG", responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }*/

                if (response.isSuccessful()){
                    worked = true;
                }

                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            }catch(Exception e){e.printStackTrace();}


            if (worked) {
                startActivity(new Intent(LoginActivity.this, HomePage.class));
                //send to new page
                return (null);
            }
            else{
                startActivity(new Intent(LoginActivity.this, emailActivity.class));
                return (null);
            }
        }catch(Exception e){e.printStackTrace();}return (null);}



        //wip
       /* protected void onPostExecute(Void result){
            authProgressDialog.dismiss();
        }*/




    }




    public void onClickToSignUp(View v){
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

    public void onClickToHomePage(View v){
        startActivity(new Intent(LoginActivity.this, HomePage.class));
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



}

