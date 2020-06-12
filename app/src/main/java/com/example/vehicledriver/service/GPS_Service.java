package com.example.vehicledriver.service;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.vehicledriver.R;
import com.example.vehicledriver.api.ApiClient;
import com.example.vehicledriver.api.ApiInterface;
import com.example.vehicledriver.pojo.Data;
import com.example.vehicledriver.pojo.SendDta;
import com.example.vehicledriver.utils.GlobalPref;

import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class GPS_Service extends Service implements LocationListener {

    private Context mContext;
    // flag for GPS status
    boolean isGPSEnabled = false;
    GlobalPref globalPref;
    // flag for network status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    ApiClient apiClients;
    ApiInterface apiService;
    Location location;//Location
    double latitude;//Latitude
    double longitude;//Longitude
    private PowerManager.WakeLock wl;
    // The minimum time between updates in milliseconds
    static int time;
    private static final long MIN_TIME_BW_UPDATES = 1000 * time;

    // Declaring a Location Manager
    protected LocationManager mlocationManager;

    public GPS_Service() {
    }

    public GPS_Service(Context mContext, String time) {
        this.mContext = mContext;
        this.time = Integer.parseInt(time);

    }

    @Override
    public void onCreate() {
        super.onCreate();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "myapp:myWakeLock");

        globalPref=new GlobalPref(getApplicationContext());
        apiClients = new ApiClient();

        apiService = apiClients.getApi(getApplicationContext()).create(ApiInterface.class);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(222, new Notification());
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {

        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Location Service")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        startForeground(222, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getLocation();
        if(wl!=null) {
            wl.acquire(4*60*60*1000L /*10 minutes*/);
        }
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(wl!=null) {
            wl.release();
        }
        mlocationManager.removeUpdates(this);
    }

    public Location getLocation() {


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mlocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            assert mlocationManager != null;
            isGPSEnabled = mlocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = mlocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    //noinspection MissingPermission
                    mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, 1f, this);

                    if (mlocationManager != null) {
                        //noinspection MissingPermission
                        location = mlocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        //noinspection MissingPermission
                        mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, 1f, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (mlocationManager != null) {
                            //noinspection MissingPermission
                            location = mlocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }

        return location;
    }

    public void stopUsingGPS() {
        if (mlocationManager != null) {
            mlocationManager.removeUpdates(GPS_Service.this);
        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }


        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS is settings");

        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {
        Intent intent = new Intent("location.intent.vehicle");
        intent.putExtra("latitude", location.getLatitude());
        intent.putExtra("longitude", location.getLongitude());
        getApplicationContext().sendBroadcast(intent);
        String token;
        String serverKey;
        if(globalPref.getToken()==null){
            token=GlobalPref.TOKEN_VAL;
        }else {
            token=globalPref.getToken();
        }

        if(globalPref.getServerKey()==null){
            serverKey="key="+GlobalPref.SERVER_KEY_VAL;
        }else {
            serverKey="key="+globalPref.getServerKey();
        }

        SendDta sendDta = new SendDta(token,
                new Data(location.getLatitude(), location.getLongitude()));
        apiService.sendMessage(serverKey,"application/json", sendDta)
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                            Log.d("asda","S");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.d("asda",""+throwable);
                    }
                });

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}