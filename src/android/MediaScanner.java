package cordova.plugin.media.scanner;

import org.apache.cordova.CordovaPlugin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PermissionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.net.Uri;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.os.Environment;
import android.util.Log;

public class MediaScanner extends CordovaPlugin {
    public static final int WRITE_PERM_REQUEST_CODE = 1;
    private final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String TAG = "MediaScanner";
    CallbackContext _callback;
    String sourceFilePath = "";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this._callback = callbackContext;
        if (action.equals("checkPermission")) {
            callbackContext.success();
            return true;
        } else if (action.equals("mediaImageScan") || action.equals("mediaVideoScan")) {
            String mediaPath = this.checkFilePath(args.optString(0));
            this.sourceFilePath = mediaPath;
            if (!mediaPath.equals("")) {
                if (PermissionHelper.hasPermission(this, "WRITE_EXTERNAL_STORAGE")) {
                    this.mediaScan(mediaPath, callbackContext);
                } else {
                    PermissionHelper.requestPermission(this, WRITE_PERM_REQUEST_CODE, WRITE_EXTERNAL_STORAGE);
                }
            } else {
                callbackContext.error("Media file path error!");
                return false;
            }
        }

        return true;
    }

    private void mediaScan(String mediaPath, CallbackContext callback) {
        File folder;
        folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File in = new File(mediaPath);
        File out = new File(folder + "/" + mediaPath.substring(mediaPath.lastIndexOf("/") + 1));

        if (in != null && out != null) {
            try {
                this.copy(in, out);
            } catch (IOException ex) {
                callback.error("File copy error!");
                return;
            }

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(Uri.fromFile(out));
            this.cordova.getActivity().sendBroadcast(mediaScanIntent);
            callback.success("Scan finish");
            return;
        } else {
            callback.error("File error!");
            return;
        }
    }

    private void copy(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    private String checkFilePath(String mediaPath) {
        String return_value = "";
        try {
            return_value = mediaPath.replaceAll("^file://", "");
        } catch (Exception e) {
        }

        return return_value;
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults)
            throws JSONException {
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                _callback.error("Permissions denied");
                return;
            }
        }

        switch (requestCode) {
        case WRITE_PERM_REQUEST_CODE:
            this.mediaScan(this.sourceFilePath, this._callback);
            break;
        }
    }
}
