package com.app.outlook.listener;

import org.json.JSONObject;

/**
 * Created by darko on 3.2.15.
 */


public interface OnServerResponceListener {
    public void onServerResponse(JSONObject response, int method);
}