/*
 * Copyright (C) 2018 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.exchange.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Messenger;
import com.android.exchange.PermissionRequestActivity;
import java.lang.InterruptedException;

public abstract class AbstractPermissionService extends Service {
    protected boolean requestPermissions(String[] permissions) {
        boolean[] result = new boolean[1];

        HandlerThread handlerThread = new HandlerThread("PermissionThread");
        handlerThread.start();

        Handler handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int[] grantResults = (int[]) msg.obj;
                result[0] = true;
                for (int i = 0; i < grantResults.length; i++) {
                    result[0] &= grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
                handlerThread.quit();
            }
        };
        Messenger messenger = new Messenger(handler);

        Intent intent = PermissionRequestActivity.createIntent(this, permissions, messenger);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // Wait for handlerThread to finish
        try {
            handlerThread.join();
        } catch(InterruptedException e) {
            return false;
        }

        return result[0];
    }
}
