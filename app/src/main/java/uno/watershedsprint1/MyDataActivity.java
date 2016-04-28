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
        // now working, gets list of datapoints from db
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
                String project = (jsonTemp.getString("project"));
                project = project.substring(9,project.length()-2);
                String result = (jsonTemp.getString("result"));


                if (result.equals("yes")){
                result = "POSITIVE";
            }else if (result.equals("no")){
                    result = "NEGATIVE";
                }
                else{
                    result = "NEUTRAL";
                }
                String date = jsonTemp.getString("created_at");

                // handle output if date is available, handled if date is not available in else
                if (!date.equals("null")) {
                    String[] dateSplit = date.split("T");
                    date = dateSplit[0];
                    Log.d("dateSplit0 ", dateSplit[0]);
                    String[] newDateSplit = date.split("-");
                    String month = newDateSplit[1];

                    Log.d("dateSplit --", newDateSplit[0]);
                    String day = newDateSplit[2];
                    String year = newDateSplit[0];
                    //strip leading double quote
                    month = month.substring(1, month.length());
                    //set month to text
                    switch (month) {
                        case "1":
                            month = "January";
                            break;
                        case "2":
                            month = "February";
                            break;
                        case "3":
                            month = "March";
                            break;
                        case "4":
                            month = "April";
                            break;
                        case "5":
                            month = "May";
                            break;
                        case "6":
                            month = "June";
                            break;
                        case "7":
                            month = "July";
                            break;
                        case "8":
                            month = "August";
                            break;
                        case "9":
                            month = "September";
                            break;
                        case "10":
                            month = "October";
                            break;
                        case "11":
                            month = "November";
                            break;
                        case "12":
                            month = "December";
                            break;
                        default:
                            //if month comes back out of scope we will just print it for now
                            break;
                    }
                    listItems.add("\t\t\t\t\t\t\t\t\t\t\t\t"+month+" "+day+", "+year);
                }
                else{
                    //there has to be a better way to space this, but time crunch
                    listItems.add("\t\t\t\t\t\t\t\t\t\t\t\tNO DATE AVAILABLE");
                }

                String lat = (jsonTemp.getString("gps_lat"));

                //project name removed from ui, leaving this for quick addition


                //listItems.add("Project: "+project);
                /*listItems.add("Location (GPS):");
                listItems.add("\t\t\t\t\t\t"+lat);
                listItems.add("\t\t\t\t\t"+lng);
                listItems.add(" ");*/
                String lng = (jsonTemp.getString("gps_long"));
                listItems.add("Lat: "+lat+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+result+"\nLong: "+lng);
                listItems.add("");



                adapter.notifyDataSetChanged();

                    }



            }catch(Exception e){e.printStackTrace();}






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

    // more failed keyboard atttempts
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



}

