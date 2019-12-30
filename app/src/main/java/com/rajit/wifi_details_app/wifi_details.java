package com.rajit.wifi_details_app;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.roger.catloadinglibrary.CatLoadingView;

public class wifi_details extends AppCompatActivity {

    TextView sourceTv, connectionStatus, signalStrengthPercent, wifiIpAddress, wifiSSID, wifiUpSpeed,
            wifiDownSpeed, signalTv, ssidTv, connectionTv, ipTv, wifiSourceTv, upspeedTv, downspeedTv;
    Button recheck, sourceOnOffSwitch;
    LinearLayout mainLayout;
    String source;
    WifiManager wifiManager;
    NetworkCapabilities nc;
    int downSpeed = 0, upSpeed = 0;

    Typeface nunitoTTF, latoBoldTTF, latoSemiBoldTTF;

    CatLoadingView mView, mView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_details);

        setTitle("WiFi Details");

        mainLayout = findViewById(R.id.mainLayout);

        signalTv = findViewById(R.id.signalTv);
        signalStrengthPercent = findViewById(R.id.signalStrengthPercent);

        ssidTv = findViewById(R.id.ssidTv);
        wifiSSID = findViewById(R.id.wifiSSID);

        connectionTv = findViewById(R.id.connectionTv);
        connectionStatus = findViewById(R.id.connectionStatus);

        ipTv = findViewById(R.id.ipTv);
        wifiIpAddress = findViewById(R.id.wifiIpAddress);

        sourceTv = findViewById(R.id.sourceTv);
        wifiSourceTv = findViewById(R.id.wifiSourceTv);

        upspeedTv = findViewById(R.id.upspeedTv);
        wifiUpSpeed = findViewById(R.id.wifiUpSpeed);

        downspeedTv = findViewById(R.id.downspeedTv);
        wifiDownSpeed = findViewById(R.id.wifiDownSpeed);

        sourceOnOffSwitch = findViewById(R.id.sourceOnOffSwitch);
        recheck = findViewById(R.id.recheck);

        source = getIntent().getStringExtra("Source");
        sourceTv.setText(source);

        settingTypeface();

        makeAllTvNull();

        mView = new CatLoadingView();
        mView1 = new CatLoadingView();
        mView.show(getSupportFragmentManager(), "Please Wait..");

        sourceOnOffSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String state = sourceOnOffSwitch.getText().toString();

                if (source.equals("WIFI")) {

                    if (state.equals("Turn WiFi Off")) {


                        wifiManager.setWifiEnabled(false);
                        recheck.setEnabled(false);
                        recheck.setBackgroundColor(getResources().getColor(R.color.light_grey));
                        connectionStatus.setText("Unavailable");
                        connectionStatus.setTextColor(getResources().getColor(R.color.light_grey));
                        sourceOnOffSwitch.setText("Turn WiFi On");
                        sourceOnOffSwitch.setBackgroundColor(getResources().getColor(R.color.btn_color));
                        makeAllTvNull();

                    } else {
                        wifiManager.setWifiEnabled(true);
                        recheck.setEnabled(true);
                        recheck.setBackgroundColor(getResources().getColor(R.color.btn_color));
                        connectionStatus.setText("Active");
                        connectionStatus.setTextColor(getResources().getColor(R.color.green));
                        sourceOnOffSwitch.setText("Turn WiFi Off");
                        sourceOnOffSwitch.setBackgroundColor(getResources().getColor(R.color.green));


                        mView1.show(getSupportFragmentManager(), "Please Wait");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                checkNetworkInfo();
                                if (!wifiUpSpeed.getText().toString().equals("Null")) {
                                    mView1.dismiss();

                                }


                            }
                        }, 6000);


                    }

                }

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkNetworkInfo();
                if (!wifiUpSpeed.getText().toString().equals("Null")) {
                    mView.dismiss();

                }
            }
        }, 4000);


        recheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionStatus.getText().toString().equals("Unavailable")) {
                    recheck.setEnabled(false);
                } else {
                    makeAllTvNull();
                    mView.show(getSupportFragmentManager(), "Please Wait");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkNetworkInfo();
                            if (!wifiUpSpeed.getText().toString().equals("Null")) {
                                mView.dismiss();
                            }
                        }
                    }, 4000);
                }
            }
        });


    }

    private void settingTypeface() {

        nunitoTTF = Typeface.createFromAsset(getAssets(), "fonts/nunito_bold.ttf");
        latoBoldTTF = Typeface.createFromAsset(getAssets(), "fonts/lato_bold.ttf");
        latoSemiBoldTTF = Typeface.createFromAsset(getAssets(), "fonts/lato_semibold.ttf");

        //TextView

        signalTv.setTypeface(latoBoldTTF);
        signalStrengthPercent.setTypeface(nunitoTTF);
        ssidTv.setTypeface(latoBoldTTF);
        wifiSSID.setTypeface(nunitoTTF);
        connectionTv.setTypeface(latoBoldTTF);
        connectionStatus.setTypeface(nunitoTTF);
        ipTv.setTypeface(latoBoldTTF);
        wifiIpAddress.setTypeface(nunitoTTF);
        sourceTv.setTypeface(latoBoldTTF);
        wifiSourceTv.setTypeface(nunitoTTF);
        upspeedTv.setTypeface(latoBoldTTF);
        wifiUpSpeed.setTypeface(nunitoTTF);
        downspeedTv.setTypeface(latoBoldTTF);
        wifiDownSpeed.setTypeface(nunitoTTF);

        sourceOnOffSwitch.setTypeface(latoSemiBoldTTF);
        recheck.setTypeface(latoSemiBoldTTF);
    }


    private void showSnackBar() {

        // make snackbar
        Snackbar mSnackbar = Snackbar.make(mainLayout, "Done", Snackbar.LENGTH_SHORT);
        // get snackbar view
        View mView = mSnackbar.getView();
        // get textview inside snackbar view
        TextView mTextView = (TextView) mView.findViewById(com.google.android.material.R.id.snackbar_text);
        // set text to center
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            mTextView.setTypeface(latoBoldTTF);
        mView.setBackgroundColor(getResources().getColor(R.color.green));
        // show the snackbar
        mSnackbar.show();
    }

    private void makeAllTvNull() {

        signalStrengthPercent.setText("Null");
        wifiSSID.setText("Null");
        wifiIpAddress.setText("Null");
        wifiUpSpeed.setText("Null");
        wifiDownSpeed.setText("Null");
        sourceTv.setText("Null");
    }

    private void checkNetworkInfo() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        signalStrengthPercent.setText("" + getSignalStrength(wifiManager) + " %");
        wifiIpAddress.setText("" + getIPAddress(wifiManager));
        wifiSSID.setText(getSSID(wifiManager));
        sourceTv.setText("WIFI");
        getPing();
        showSnackBar();
    }

    private int getSignalStrength(WifiManager wifimanager) {

        int numberOfLevels = 5;
        WifiInfo wifiInfo = wifimanager.getConnectionInfo();
        double level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
        double sfsd = (level / numberOfLevels) * 100;
        return (int) sfsd;
    }

    private String getIPAddress(WifiManager wifimanager) {
        int ipAddress = wifimanager.getConnectionInfo().getIpAddress();
        String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return ip;

    }

    private String getSSID(WifiManager wifimanager) {

        return wifimanager.getConnectionInfo().getSSID();
    }

    private void getPing() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        nc = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            nc = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        }

        if (nc != null) {
            downSpeed = nc.getLinkDownstreamBandwidthKbps();
            upSpeed = nc.getLinkUpstreamBandwidthKbps();
            wifiUpSpeed.setText("" + upSpeed / 1000 + " Kbps");
            wifiDownSpeed.setText("" + downSpeed / 1000 + " Kbps");
        } else {


        }


    }
}
