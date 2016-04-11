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
import android.os.StrictMode;
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
import android.widget.LinearLayout;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;

//import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class Projects_Activity extends AppCompatActivity  {
protected String myResponse;
protected Boolean worked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_);

        Button button;
        button = (Button) findViewById(R.id.email_sign_in_button);




        /////////////////////////////////////////////////////////////////////////////WIP

        if (Looper.myLooper() == null)
        {
            Looper.prepare();
        }

        try {

            Globals g = Globals.getInstance();
            MediaType mediaType = MediaType.parse("application/json");

            //start httpclient for api communication

            //build body

            //build request
            Request request = new Request.Builder()
                    .url("http://newatershed.net/api/projects/active")
                    .addHeader("content-type", "application/json")
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", g.getToken())
                    .addHeader("cache-control", "no-cache")
                    .build();
            Log.d("Did it get here222", request.toString());
            sendTask sendTask = new sendTask();
            sendTask.execute(request);




    }catch(Exception e){e.printStackTrace();}

        //////////////////////////////////////////////////////////////////////////////ENDWIP


    }

    private class sendTask extends AsyncTask<Request, Void, Void>{
        private Exception exception;
        private Response response;
        private String sendResponse;

        @Override
        protected Void doInBackground(Request... request){
            try{
                OkHttpClient client = new OkHttpClient();
                 response = client.newCall(request[0]).execute();
                 sendResponse = response.body().string();
                if (response.isSuccessful()){
                    worked = true;
                }

                if (!response.isSuccessful()) throw new IOException("Unexpected code " + myResponse);
            }catch(Exception e){e.printStackTrace();}






            return null;
        }
        @Override
        protected void onPostExecute(Void v){
            setResponse(sendResponse);

        }





    }

    protected void setResponse(String Result){
        LinearLayout layout = (LinearLayout)findViewById(R.id.button_list);
        myResponse = Result;
        try{
            //send

            //get response in string

            String jsonData = myResponse;
            //put it in json
            JSONObject Jobject = new JSONObject(jsonData);

            //get projects array then start chopping
            JSONArray projectArray = Jobject.getJSONArray("projects");

            final int[] projectNums =new int[50];
            Button projButtons[] = new Button[projectArray.length()];
            for (int i=0; i<projectArray.length(); i++)
            {
                final int j = i;
                projectNums[i] = projectArray.getJSONObject(i).getInt("projectid");
                projButtons[i] = new Button(this);
                projButtons[i].setText(projectArray.getJSONObject(i).getString("name"));
                projButtons[i].setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v){
                        setGlobalId(projectNums[j]);
                        onClickToHomePage(v);

                    }
                });
                layout.addView(projButtons[i]);

            }




            //Go through json to find token, set it to Global token using singleton class Globals

            //Headers responseHeaders = response.headers();
            //debug stuff below
                /*for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.d("DEBUG", responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }*/



        if (worked) {
            //startActivity(new Intent(Projects_Activity.this, HomePage.class));
            //send to new page

        }
        else{
            startActivity(new Intent(Projects_Activity.this, emailActivity.class));

        }
    }catch (Exception e){e.printStackTrace();}}
    protected void setGlobalId(int i){
        Globals g = Globals.getInstance();
        g.setId(i);
    }


    public void onClickToSignUp(View v){
        startActivity(new Intent(Projects_Activity.this, SignUpActivity.class));
    }

    public void onClickToHomePage(View v){
        startActivity(new Intent(Projects_Activity.this, HomePage.class));
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



}

