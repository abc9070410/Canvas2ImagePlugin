package org.devgeeks.Canvas2ImagePlugin;

import java.util.Calendar;
import java.nio.ByteBuffer;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.*;
import java.lang.*;
import java.sql.*;

/**
 * Canvas2ImagePlugin.java
 *
 * Android implementation of the Canvas2ImagePlugin for iOS.
 * Inspirated by Joseph's "Save HTML5 Canvas Image to Gallery" plugin
 * http://jbkflex.wordpress.com/2013/06/19/save-html5-canvas-image-to-gallery-phonegap-android-plugin/
 *
 * @author Vegard Løkken <vegard@headspin.no>
 */
public class Canvas2ImagePlugin extends CordovaPlugin {
    public static final String ACTION = "saveImageDataToLibrary";

    private static final int BMP_WIDTH_OF_TIMES = 4;
    private static final int BYTE_PER_PIXEL = 3;

    @Override
    public boolean execute(String action, JSONArray data,
            CallbackContext callbackContext) throws JSONException {

        if (action.equals(ACTION)) {

            String base64 = data.optString(0);
            String fileName = data.optString(1);

            if (base64.equals("")) // isEmpty() requires API level 9
            {
                callbackContext.error("Missing base64 string");
            }

            if (base64 == null) {
                callbackContext.error("The image could not be decoded");
            } else {

                // Save the image
                File imageFile = savePhoto(base64, fileName);
                if (imageFile == null) {
                    callbackContext.error("Error while saving image");
                }

                // Update image gallery
                scanPhoto(imageFile);

                callbackContext.success(imageFile.toString());
            }

            return true;
        } else {
            return false;
        }
    }

    private File savePhoto(String base64, String fileName) {
        File retVal = null;

        try {
            Calendar c = Calendar.getInstance();
            String date = "" + c.get(Calendar.DAY_OF_MONTH)
                    + c.get(Calendar.MONTH)
                    + c.get(Calendar.YEAR)
                    + c.get(Calendar.HOUR_OF_DAY)
                    + c.get(Calendar.MINUTE)
                    + c.get(Calendar.SECOND);

            String deviceVersion = Build.VERSION.RELEASE;
            Log.i("Canvas2ImagePlugin", "Android version " + deviceVersion);
            int check = deviceVersion.compareTo("2.3.3");

            File folder;
            /*
             * File path = Environment.getExternalStoragePublicDirectory(
             * Environment.DIRECTORY_PICTURES ); //this throws error in Android
             * 2.2
             */
            if (check >= 1) {
                folder = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                if (!folder.exists()) {
                    folder.mkdirs();
                }
            } else {
                folder = Environment.getExternalStorageDirectory();
            }

            File imageFile = new File(folder, fileName);

            if (true) {
                saveBase64(base64, imageFile);
            } else {
                FileOutputStream out = new FileOutputStream(imageFile);

                out.flush();
                out.close();
            }

            retVal = imageFile;
        } catch (Exception e) {
            Log.e("Canvas2ImagePlugin", "An exception occured while saving image: "
                    + e.toString());
        }
        return retVal;
    }

    /* Invoke the system's media scanner to add your photo to the Media Provider's database, 
     * making it available in the Android Gallery application and to other apps. */
    private void scanPhoto(File imageFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        cordova.getActivity().sendBroadcast(mediaScanIntent);
    }

    public static int saveBase64(String base64, File imageFile) {
        int success = 1;

        try {
            FileOutputStream outStream = new FileOutputStream(imageFile);

            byte[] imageBytes = Base64.decode(base64, Base64.DEFAULT);
            InputStream inStream = new ByteArrayInputStream(imageBytes);

            int length = -1;
            int size = (int) imageBytes.length;
            byte[] buffer = new byte[size];

            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
                outStream.flush();
            }

            inStream.close();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            success = 0;
        } finally {
            return success;
        }
    }
}
