package br.gbizotto.customcamera.camera;

import android.hardware.Camera;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Gabriela on 03/03/2017.
 */

public final class CameraUtils {

    private CameraUtils() {
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.e(CameraUtils.class.getSimpleName(), e.getLocalizedMessage(), e);
        }
        return c; // returns null if camera is unavailable
    }

    public static Camera releaseCamera(Camera camera){
        if (camera != null){
            camera.release();
            camera= null;
        }
        return camera;
    }

    public static Camera.PictureCallback getPictureCallback() {
        return new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                File pictureFile = FileUtils.getOutputMediaFile(FileUtils.MEDIA_TYPE_IMAGE);
                if (pictureFile == null){
                    Log.e(CameraUtils.class.getSimpleName(), "Error creating media file, check storage permissions: ");
                    return;
                }

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    Log.v(CameraUtils.class.getSimpleName(), "diretório = " + pictureFile.getAbsolutePath());

                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.e(CameraUtils.class.getSimpleName(), "File not found: " + e.getMessage(), e);
                } catch (IOException e) {
                    Log.e(CameraUtils.class.getSimpleName(), "Error accessing file: " + e.getMessage(), e);
                }
            }
        };
    }
}
