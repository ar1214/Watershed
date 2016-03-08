package uno.watershedsprint1;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


public class MyLocListener implements LocationListener{

    @Override
    public void onLocationChanged(Location location)
    {
        if(location != null)
        {


        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider){

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
