package uno.watershedsprint1;

//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.annotation.TargetApi;
import android.app.Activity;
//import android.app.ProgressDialog;
import android.app.ListActivity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
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
import java.util.ArrayList;
//import java.util.ArrayList;
//import java.util.List;

//import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class MyDataActivity extends ListActivity {
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    protected String myResponse;
    protected Boolean worked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);
        adapter = new ArrayAdapter<String>(this,R.layout.my_text_view, listItems);
        setListAdapter(adapter);








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
                    .url("http://newatershed.net/api/datapoints/getuser")
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
            JSONArray projectArray = Jobject.getJSONArray("datapoints");



            for (int i=projectArray.length()-1; i>=0; i--)
            {

                JSONObject jsonTemp = projectArray.getJSONObject(i);
                /*JSONObject jsonTemp = new JSONObject(temp);
                Log.d("Did it get here 333", temp);*/
                String working = (jsonTemp.getString("project"));
                working = working.substring(9,working.length()-2);
                listItems.add("Project: "+working);
                working = (jsonTemp.getString("result"));
                if (working.equals("yes")){
                working = "Positive";
            }else if (working.equals("no")){
                    working = "Negative";
                }
                else{
                    working = "Not Conclusive";
                }
                listItems.add(working);
                listItems.add("Location (GPS):");
                working = (jsonTemp.getString("gps_lat"));
                listItems.add("\t\t\t\t\t\t"+working);
                working = (jsonTemp.getString("gps_long"));
                listItems.add("\t\t\t\t\t"+working);
                listItems.add(" ");



                adapter.notifyDataSetChanged();

                    }



            }catch(Exception e){e.printStackTrace();}




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
                startActivity(new Intent(MyDataActivity.this, emailActivity.class));

            }
        }





    public void onClickToHomePage(View v){
        startActivity(new Intent(MyDataActivity.this, HomePage.class));
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



}

