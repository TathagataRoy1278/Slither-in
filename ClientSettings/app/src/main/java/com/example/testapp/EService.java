package com.example.testapp;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class EService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent in, int flags, int startId)
    {
        return super.onStartCommand(in,flags,startId);
    }
}
