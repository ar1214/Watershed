package uno.watershedsprint1;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DataEntry extends AppCompatActivity {
    ImageView viewImage;
    Button b;
    private Button button;
    private TextView latTextView;
    private TextView longTextView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        b = (Button) findViewById(R.id.buttonTakePic);
        viewImage = (ImageView) findViewById(R.id.imageView1);
        sendButton = (Button) findViewById(R.id.submitButton);
        //catch camera button press
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePic();
            }
        });



        //multistate toggle button


        MultiStateToggleButton btnpicked = (MultiStateToggleButton) findViewById(R.id.mstb_multi_id);
        btnpicked.setValue(1);

        // Get gps button and text views where we will put the info
        button = (Button) findViewById(R.id.buttonLocation);
        latTextView = (TextView) findViewById(R.id.latitude);
        longTextView = (TextView) findViewById(R.id.longitude);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        /////////////////////////////////////////////////////////////////////////////START GPS STUFF
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latTextView.setText(""+location.getLatitude());
                longTextView.setText(""+location.getLongitude());
                try {
                    locationManager.removeUpdates(locationListener);
                }catch(SecurityException E){E.printStackTrace();}
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET

            },10);
            return;
        }
        else{
            //configureButton sets the listener for gps
            locationManager.requestLocationUpdates("gps", 0, 0, locationListener);

        }
        ///////////////////////////////////////////////////////////////////END GPS STUFF


        ////////////////////////////////////////////////////////////////START SEND STUFF

        //set listener to send info

        sendButton.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View view){
                MediaType MEDIA_TYPE_MARKDOWN
                        = MediaType.parse("text/x-markdown; charset=utf-8");

                //get responses from screen

                EditText latitude = (EditText) findViewById(R.id.latitude);
                EditText longitude = (EditText) findViewById(R.id.longitude);
                EditText comment = (EditText) findViewById(R.id.comment);
                String result;

                // attempt to replace radio buttons with multistate toggle
                // see https://github.com/jlhonora/multistatetogglebutton

                MultiStateToggleButton btnpicked = (MultiStateToggleButton) findViewById(R.id.mstb_multi_id);
                int togglestate = btnpicked.getValue();
                Log.d("togglestate",Integer.toString(togglestate) );
                if (togglestate == 0){
                    result = "yes";
                }
                else if (togglestate ==1){
                    result = "no";
                }
                else{
                    result = "not available";
                }


                // the below is from when a radio group was being used, leaving for quick switch if there is any problem with the multistate toggle
                /*RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
                RadioButton btnPicked;

                int selectedId = radioGroup.getCheckedRadioButtonId();
                btnPicked = (RadioButton) findViewById(selectedId);
                String buttonString = btnPicked.getText().toString();

                if (buttonString.equals("Positive")){
                    result = "yes";
                }
                else if (buttonString.equals("Negative")){
                    result = "no";
                }
                else{
                    result = "not available";
                }*/






                //convert info to strings

                String latitudeS = latitude.getText().toString();
                String longitudeS = longitude.getText().toString();
                String commentS = comment.getText().toString();
                if (commentS.length() <= 3)
                {
                    commentS = "invalid comment";
                }

                /// make params array
                String[] params = {result, latitudeS, longitudeS,commentS };
                Log.d("params--",params[0]+" "+params[1]+" "+params[2]+" "+params[3]);
                //execute send

                SendTask myTask = new SendTask();
                myTask.execute(params);
                //disable the sendbutton so multiple submits can't happen
                sendButton.setEnabled(false);
            }
        });

        /////////////////////////////////////////////////////////////////END SEND STUFF

    }



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

                Globals g = Globals.getInstance();
                MediaType mediaType = MediaType.parse("application/json");

                //start httpclient for api communication
                OkHttpClient client = new OkHttpClient();
                //build body
                String tok = g.getToken();
               // Log.d("WHAT IS BEING SENT", params[0]+" "+params[1]+" "+params[2]+ " "+params[3]+ " "+tok);

                RequestBody formBody = new FormEncodingBuilder()
                        .add("projectid", Integer.toString(g.getId()))
                        .add("result", params[0])
                        .add("gps_lat",params[1])
                        .add("gps_long",params[2])
                        .add("comments", params[3])
                        .build();

                //RequestBody formBody = RequestBody.create(mediaType, "{\r\n   \"projectid\":\"2\",\r\n   \"result\":\""+params[0]+"\",\r\n   \"gps_lat\":\""+params[1]+"\",\r\n   \"gps_long\":\""+params[2]+"\",\r\n   \"comments\":\""+params[3]+"\"\r\n}");
                //build request
                Request request = new Request.Builder()
                        .url("http://newatershed.net/api/datapoints/add")
                        .post(formBody)
                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .addHeader("authorization",tok )
                        .addHeader("cach-control", "no-cache")
                        .build();


                try{
                    //send
                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()){
                        worked = true;
                    }

                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                }catch(Exception e){e.printStackTrace();}


                if (worked) {
                    try {
                        locationManager.removeUpdates(locationListener);
                    }catch(SecurityException E){E.printStackTrace();}

                    startActivity(new Intent(DataEntry.this, StepCongratsActivity.class));
                    //send to new page
                    return (null);
                }
                else{
                    startActivity(new Intent(DataEntry.this, emailActivity.class));
                    return (null);
                }
            }catch(Exception e){e.printStackTrace();}return (null);}

    }

    //navigation
    public void onClickToHomePage(View v){
        startActivity(new Intent(DataEntry.this, HomePage.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)

                return;
        }
    }
    //GPS button setup - currently removed
    /*private void configureButton() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
                    button.setEnabled(false);
                }catch(SecurityException e){e.printStackTrace();}
            }
        });

    }*/

    //sending picture to db not implemented yet, but should be in a good state to get there
    private void takePic()
    {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if (requestCode==1){
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp:f.listFiles()){
                    if (temp.getName().equals("temp.jpg")){
                        f=temp;
                        break;
                    }
                }

            try{
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                viewImage.setImageBitmap(bitmap);
                String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "Phoenix" + File.separator + "default";
                f.delete();
                OutputStream outFile = null;
                File file = new File(path, String.valueOf(System.currentTimeMillis())+".jpg");
                try{
                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                    outFile.flush();
                    outFile.close();
                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if (requestCode==2){
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnindex=c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnindex);
            c.close();
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            Log.w("image path from gallery", picturePath + "");
            viewImage.setImageBitmap(thumbnail);

        }
        }
    }

}

