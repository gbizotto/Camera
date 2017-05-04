package br.gbizotto.customcamera.camera;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by Gabriela on 03/03/2017.
 */

public class FileUtils {

    public static File getOutputMediaFile(String directoryName, String fileName) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), directoryName);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(FileUtils.class.getSimpleName(), "failed to create directory");
                return null;
            }
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }
}
