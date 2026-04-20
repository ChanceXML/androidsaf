package com.shadowmario.psychengine;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SAFModSync {

    public static void sync(Context context, String treeUriString) {
        try {
            Uri treeUri = Uri.parse(treeUriString);

            DocumentFile root = DocumentFile.fromTreeUri(context, treeUri);
            if (root == null || !root.isDirectory()) return;

            File modsDir = new File(context.getFilesDir(), "mods");
            if (!modsDir.exists()) modsDir.mkdirs();

            syncFolder(context, root, modsDir);

            Log.d("SAF_SYNC", "Mods synced!");

        } catch (Exception e) {
            Log.e("SAF_SYNC", "Sync failed", e);
        }
    }

    private static void syncFolder(Context context, DocumentFile source, File target) {

        for (DocumentFile file : source.listFiles()) {

            if (file.isDirectory()) {
                File newDir = new File(target, file.getName());
                if (!newDir.exists()) newDir.mkdirs();
                syncFolder(context, file, newDir);
            } else {
                File dest = new File(target, file.getName());

                if (dest.exists() && dest.length() == file.length()) {
                    continue;
                }

                copyFile(context, file, dest);
            }
        }
    }

    private static void copyFile(Context context, DocumentFile source, File dest) {
        try {
            InputStream in = context.getContentResolver().openInputStream(source.getUri());
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[8192];
            int len;

            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            in.close();
            out.close();

        } catch (Exception e) {
            Log.e("SAF_SYNC", "File copy failed: " + source.getName(), e);
        }
    }
}
