package com.haibeey.android.sticky;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private NoteService service;
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
        service.trigger(false);
        unbindService(serviceConnection);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this,NoteService.class),serviceConnection, Context.BIND_AUTO_CREATE);
    }
}
