package com.rajit.wifi_details_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

public class SampleReceiver extends BroadcastReceiver {

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");

        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null){

            switch (networkInfo.getType()){

                case ConnectivityManager.TYPE_WIFI:
                    Toast.makeText(context, "WIFI Connected", Toast.LENGTH_SHORT).show();

                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    Toast.makeText(context, "Mobile Connected", Toast.LENGTH_SHORT).show();
                    break;
            }


        }else{

            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();

        }
    }

}
