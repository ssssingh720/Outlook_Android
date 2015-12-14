/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.app.outlook.services;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.media.RingtoneManager;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.v4.app.NotificationCompat;
        import android.util.Log;

        import com.app.outlook.R;
        import com.app.outlook.activities.HomeListingActivity;
        import com.app.outlook.manager.SharedPrefManager;
        import com.app.outlook.modal.IntentConstants;
        import com.app.outlook.modal.NotificationVo;
        import com.app.outlook.modal.OutlookConstants;
        import com.app.outlook.receivers.NotificationReceiver;
        import com.google.android.gms.gcm.GcmListenerService;
        import com.google.gson.Gson;

public class MyGcmListenerService extends GcmListenerService {
    private int NotificationCount = 0;


    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_LOGGEDIN)) {
            sendNotification(message);
        }
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        final NotificationVo notification = new Gson().fromJson(message, NotificationVo.class);

        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra(IntentConstants.NOTIFICATION_MESSAGE, message);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ++NotificationCount /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap bitmapLarge = BitmapFactory.decodeResource(getResources(),
                R.drawable.app_icon);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("OutLook")
                .setContentText(notification.getMsg())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLargeIcon(bitmapLarge)
                .setPriority(1)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(++NotificationCount /* ID of notification */, notificationBuilder.build());
    }
}
