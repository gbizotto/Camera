package br.gbizotto.customcamera.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Gabriela on 03/03/2017.
 */

public final class CameraUtils {

    private static final int IMAGE_ROTATE_FRONT_FACING_CAMERA = 270;
    private static final int IMAGE_ROTATE_BACK_FACING_CAMERA = 90;
    private static final int IMAGE_PREVIEW_CORRECTION_FACTOR = 360;

    private CameraUtils() {
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
            c.setDisplayOrientation(IMAGE_ROTATE_BACK_FACING_CAMERA);
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

    public static Camera.PictureCallback getPictureCallback(final PictureTaken pictureTaken) {
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

                    fos.write(rotate(data, false));
                    fos.close();

                    pictureTaken.pictureSaved(pictureFile.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    Log.e(CameraUtils.class.getSimpleName(), "File not found: " + e.getMessage(), e);
                } catch (IOException e) {
                    Log.e(CameraUtils.class.getSimpleName(), "Error accessing file: " + e.getMessage(), e);
                }
            }
        };
    }

    private static byte[] rotate(byte[] data, boolean frontFacing) {
        Matrix matrix = new Matrix();
        if (frontFacing) {
            matrix.postRotate(IMAGE_ROTATE_FRONT_FACING_CAMERA);
        } else {
            matrix.postRotate(IMAGE_ROTATE_BACK_FACING_CAMERA);
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (bitmap.getWidth() < bitmap.getHeight()) {
            return data;
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true
        );
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, blob);
        return blob.toByteArray();
    }
}
