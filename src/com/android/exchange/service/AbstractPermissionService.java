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
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import com.android.exchange.PermissionRequestActivity;

public abstract class AbstractPermissionService extends Service {
    protected boolean requestPermissions(String[] permissions) {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        boolean[] result = new boolean[1];

        Handler handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int[] grantResults = (int[]) msg.obj;
                result[0] = true;
                for (int i = 0; i < grantResults.length; i++) {
                    result[0] &= grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
                getLooper().quitSafely();
            }
        };
        Messenger messenger = new Messenger(handler);

        startActivity(PermissionRequestActivity.createIntent(this, permissions, messenger));
        Looper.loop();

        return result[0];
    }
}
