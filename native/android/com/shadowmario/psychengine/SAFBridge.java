package com.shadowmario.psychengine;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class SAFBridge {

    public static MainActivity activity;
    public static int REQUEST_CODE = 777;
    public static String lastTreeUri;

    public static void openModsFolderPicker() {
        if (activity == null) {
            Log.e("SAF", "Activity is null!");
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);

            activity.startActivityForResult(intent, REQUEST_CODE);
        } catch (Exception e) {
            Log.e("SAF", "Failed to open picker: " + e);
        }
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE) return;

        if (resultCode != -1) {
            Log.e("SAF", "User cancelled folder picker");
            return;
        }

        if (data == null) {
            Log.e("SAF", "Data is null!");
            return;
        }

        Uri uri = data.getData();

        if (uri == null) {
            Log.e("SAF", "URI is null!");
            return;
        }

        try {
            final int flags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            activity.getContentResolver().takePersistableUriPermission(uri, flags);

            Log.d("SAF", "Selected folder: " + uri.toString());

            SAFModSync.sync(activity, uri.toString());

        } catch (Exception e) {
            Log.e("SAF", "Permission error: " + e);
        }
    }
}
