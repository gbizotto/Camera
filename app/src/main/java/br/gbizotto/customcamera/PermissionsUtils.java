package br.gbizotto.customcamera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Gabriela on 02/03/2017.
 */

public final class PermissionsUtils {

    private PermissionsUtils() {
    }

    public static boolean checkCameraPermission(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
}
