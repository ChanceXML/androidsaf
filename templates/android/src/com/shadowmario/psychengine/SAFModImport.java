package com.shadowmario.psychengine;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import java.io.File;

public class SAFModImporter {

    public static final int REQUEST_CODE_PICK_FOLDER = 9991;

    public static void openFolderPicker(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION |
                Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        );
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_FOLDER);
    }

    public static void handleFolderResult(Activity activity, Intent data) {
        if (data == null) return;

        Uri treeUri = data.getData();
        if (treeUri == null) return;

        SAFBridge.lastTreeUri = treeUri.toString();

        activity.getContentResolver().takePersistableUriPermission(
                treeUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
        );

        SAFModSync.sync(activity, treeUri.toString());
    }
}
