package br.gbizotto.customcamera.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import br.gbizotto.customcamera.PictureTaken;
import br.gbizotto.customcamera.R;

/**
 * Created by Gabriela on 03/03/2017.
 */

@SuppressWarnings("deprecation")
public final class CameraUtils {

    private static final int IMAGE_QUALITY = 100;
    private static final int IMAGE_ROTATE_FRONT_FACING_CAMERA = 270;
    private static final int IMAGE_ROTATE_BACK_FACING_CAMERA = 90;
    private static final int IMAGE_PREVIEW_CORRECTION_FACTOR = 360;

    private CameraUtils() {
    }

    public static Camera getCameraInstance(boolean frontFacing, Activity activity) {
        Camera camera = null;
        try {
            if (frontFacing) {
                camera = findFrontFacingCamera(activity);
            } else {
                camera = Camera.open();
                camera.setDisplayOrientation(IMAGE_ROTATE_BACK_FACING_CAMERA);
            }

            Camera.Parameters params = camera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            List<Camera.Size> sizes = params.getSupportedPictureSizes();

            Camera.Size mSize = null;
            for (Camera.Size size : sizes) {
                if (mSize == null || mSize.width > size.width) {
                    mSize = size;
                }
            }
            if (mSize != null) {
                params.setPictureSize(mSize.width, mSize.height);
            }
            camera.setParameters(params);

        } catch (Exception e) {
            Log.e(CameraUtils.class.getSimpleName(), e.getMessage(), e);
        }

        return camera;
    }

    private static Camera findFrontFacingCamera(Activity activity) {
        int numberOfCameras = Camera.getNumberOfCameras();

        Camera frontFacingCamera = null;
        int counter = 0;
        while (frontFacingCamera == null && counter < numberOfCameras) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(counter, cameraInfo);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                frontFacingCamera = Camera.open(counter);
            } else {
                counter++;
            }
        }

        if (frontFacingCamera != null) {
            setCameraDisplayOrientation(activity, counter, frontFacingCamera);
        }

        return frontFacingCamera;
    }

    public static Camera releaseCamera(Camera camera) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        return camera;
    }

    public static Camera.PictureCallback getPictureCallback(final Context context, final String fileName, final PictureTaken pictureTaken, final boolean frontFacing) {
        return new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                File pictureFile = FileUtils.getOutputMediaFile(context.getString(R.string.pictures_directory), fileName);
                if (pictureFile == null) {
                    Log.e(CameraUtils.class.getSimpleName(), context.getString(R.string.pictures_directory_error));
                    return;
                }

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(rotate(data, frontFacing));
                    fos.close();

                    pictureTaken.pictureSaved(pictureFile.getAbsolutePath());
                } catch (IOException e) {
                    Log.e(CameraUtils.class.getSimpleName(), e.getMessage(), e);
                }
            }
        };
    }

    private static void setCameraDisplayOrientation(Activity activity,
                                                    int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % IMAGE_PREVIEW_CORRECTION_FACTOR;
            result = (IMAGE_PREVIEW_CORRECTION_FACTOR - result) % IMAGE_PREVIEW_CORRECTION_FACTOR;
        } else {
            result = (info.orientation - degrees + IMAGE_PREVIEW_CORRECTION_FACTOR) % IMAGE_PREVIEW_CORRECTION_FACTOR;
        }
        camera.setDisplayOrientation(result);
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
        rotatedBitmap.compress(Bitmap.CompressFormat.PNG, IMAGE_QUALITY, blob);
        return blob.toByteArray();
    }
}
