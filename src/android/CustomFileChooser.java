package com.custom.cordova;

import android.app.Activity;
import android.content.Intent;

import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;

public class CustomFileChooser extends CordovaPlugin {
    private static final String TAG = "CustomFileChooser";
    private static final String ACTION_OPEN = "open";
    private static final int PICK_FILE_REQUEST = 1;
    CallbackContext callback;

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        if (action.equals(ACTION_OPEN)) {
            openFileDialog(callbackContext, args.getString(0), args.getString(1));
            return true;
        }

        return false;
    }

    public void openFileDialog(CallbackContext callbackContext, String type, String allowMultipleString) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        String[] mimeTypes = type.split(",");
        boolean allowMultiple = Boolean.parseBoolean(allowMultipleString);

        for (int i = 0; i < mimeTypes.length; i++) {
            mimeTypes[i] = mimeTypes[i].trim();
        }

        if (mimeTypes.length > 0) {
            intent.setType(mimeTypes[0]);
        }

        if (mimeTypes.length > 1) {
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }

        if (allowMultiple) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }

        Intent chooser = Intent.createChooser(intent, "Select File");
        cordova.startActivityForResult(this, chooser, PICK_FILE_REQUEST);

        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        callback = callbackContext;
        callbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FILE_REQUEST && callback != null) {
            if (resultCode == Activity.RESULT_OK) {
                JSONArray jsonArray = new JSONArray();

                if (null != data.getClipData()) { // checking multiple selection or not
                    for(int i = 0; i < data.getClipData().getItemCount(); i++) {
                        jsonArray.put(data.getClipData().getItemAt(i).getUri().toString());
                    }
                } else {
                    jsonArray.put(data.getData().toString());
                }

                callback.success(jsonArray.toString());
            } else if (resultCode == Activity.RESULT_CANCELED) {
                PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
                callback.sendPluginResult(pluginResult);
            } else {
                callback.error(resultCode);
            }
        }
    }
}
