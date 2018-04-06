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
package com.android.exchange;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class PermissionRequestActivity extends Activity
        implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = Eas.LOG_TAG;

    public static Intent createIntent(Context context, String[] permissions, Messenger messenger) {
        return new Intent(context, PermissionRequestActivity.class)
            .putExtra("permissions", permissions)
            .putExtra("messenger", messenger);
    }

    private Messenger mMessenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String[] permissions = intent.getStringArrayExtra("permissions");
        if (permissions == null) {
            Log.e(TAG, "No permission StringArray found in intent for PermissionRequestActivity");
            finish();
            return;
        }

        mMessenger = intent.getParcelableExtra("messenger");
        if (permissionsGranted(permissions)) {
            int[] results = new int[permissions.length];
            for (int i = 0; i < results.length; i++) {
                results[i] = PackageManager.PERMISSION_GRANTED;
            }
            sendResults(results);
            finish();
        } else {
            requestPermissions(permissions, 0);
        }
    }

    private void sendResults(int[] results) {
        Message msg = Message.obtain();
        msg.obj = results;
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            // ignored
        }
    }

    private boolean permissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            String permissions[], int[] grantResults) {
        sendResults(grantResults);
        finish();
    }
}
