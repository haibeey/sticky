package com.haibeey.android.sticky;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    private NoteService service;
    //values chosen at random
    private final static int READ_EXTERNAL_STORAGE=206;
    private final static int WRITE_EXTERNAL_STORAGE=423;
    private final static int SYSTEM_ALERT_WINDOW=8527;

    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            NoteService.LocalBinder localBinder= (NoteService.LocalBinder) iBinder;
            service=localBinder.getService();
            service.trigger(true);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            service.trigger(false);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        //service is bound and we want to close the client
        if (service!=null){
            service.trigger(false);
            unbindService(serviceConnection);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean perm =true;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE);
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE);
            perm=requestPermission(Manifest.permission.SYSTEM_ALERT_WINDOW,SYSTEM_ALERT_WINDOW);
        }

        Log.e("permis",perm+"");
        if (perm){
            //so service wont get killed
            startService(new Intent(this,NoteService.class));

            //bind to started service
            bindService(new Intent(this,NoteService.class),serviceConnection, Context.BIND_ABOVE_CLIENT);
        }

    }

    //return true if a permission is granted and false otherwise
    //if not granted it ask for a new permission
    private boolean  requestPermission(String permissionType,int permissionReqCode){


        if (ContextCompat.checkSelfPermission(this,
                permissionType)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permissionType)) {
                //TODO show permission dialogue
            } else {
                // No explanation needed; request the permission
                if (permissionType.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)){
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, SYSTEM_ALERT_WINDOW);
                }else{
                    ActivityCompat.requestPermissions(this,
                        new String[]{permissionType},
                        permissionReqCode);
                }

            }
            return  false;
        } else {
            // Permission has already been granted
            return  true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission was granted, pass

        } else {
            // permission denied, boo! exist
            onBackPressed();
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private boolean checkOverlaySetting(){
        return  Settings.canDrawOverlays(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==SYSTEM_ALERT_WINDOW){

            if (checkOverlaySetting()) {
               onRestart();
               Log.e("What","why does it still show");

            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
