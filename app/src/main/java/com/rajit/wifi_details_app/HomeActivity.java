package com.rajit.wifi_details_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    TextView wifi_state, networkInfom, internetTv, connectionTv, checkerTv, wifiTv, wifiDot;
    Button wifi_check;

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    Typeface nunitoTTF, latoSemiBoldTTF, latoBoldTTF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        wifi_state = findViewById(R.id.wifi_state);
        wifiTv = findViewById(R.id.wifiTv);
        wifiDot = findViewById(R.id.wifiDot);

        wifi_check = findViewById(R.id.wifi_check);

        networkInfom = findViewById(R.id.networkInfo);

        internetTv = findViewById(R.id.internetTv);
        connectionTv = findViewById(R.id.connectionTv);
        checkerTv = findViewById(R.id.checkerTv);
        internetTv = findViewById(R.id.internetTv);

        wifi_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(wifi_state.getText().toString().equals("Off")){

                    Toast.makeText(HomeActivity.this, "Please turn on WiFi", Toast.LENGTH_SHORT).show();
                    wifi_check.setEnabled(false);
                    wifi_check.setBackgroundColor(getResources().getColor(R.color.light_grey));
                }else{

                    checkConnection("WIFI");
                }



            }
        });

        settingTypeface();


    }

    private void settingTypeface() {

        nunitoTTF = Typeface.createFromAsset(getAssets(), "fonts/nunito_bold.ttf");
        latoBoldTTF = Typeface.createFromAsset(getAssets(), "fonts/lato_bold.ttf");
        latoSemiBoldTTF = Typeface.createFromAsset(getAssets(), "fonts/lato_semibold.ttf");

        //TextView

        internetTv.setTypeface(nunitoTTF);
        connectionTv.setTypeface(nunitoTTF);
        checkerTv.setTypeface(nunitoTTF);

        wifiTv.setTypeface(latoBoldTTF);
        wifiDot.setTypeface(latoBoldTTF);
        wifi_state.setTypeface(latoBoldTTF);

        //Check Connection Button

        wifi_check.setTypeface(latoBoldTTF);

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {

                switch (networkInfo.getType()) {

                    case ConnectivityManager.TYPE_WIFI:
                        Toast.makeText(context, "WIFI Connected", Toast.LENGTH_SHORT).show();
                        wifi_state.setText("On");
                        wifi_check.setEnabled(true);
                        wifi_check.setBackgroundColor(getResources().getColor(R.color.btn_color));
                        break;
                }


            } else {

                Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
                wifi_state.setText("Off");
                wifi_check.setEnabled(false);
                wifi_check.setBackgroundColor(getResources().getColor(R.color.light_grey));
            }
        }
    };

    private void checkConnection(final String connectionSource) {

        Intent intent = new Intent(getApplicationContext(), wifi_details.class);
        intent.putExtra("Source", connectionSource);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_exit_dialog, viewGroup, false);
        Button positiveBtn = dialogView.findViewById(R.id.positiveBtn);
        Button negativeBtn = dialogView.findViewById(R.id.negativeBtn);
        TextView promptQ = dialogView.findViewById(R.id.promptQ);

        //Setting Typeface for Dialog Buttons

        promptQ.setTypeface(nunitoTTF);
        positiveBtn.setTypeface(latoSemiBoldTTF);
        negativeBtn.setTypeface(latoSemiBoldTTF);

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finishAffinity();

            }
        });

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

        dialog.show();

    }
}
