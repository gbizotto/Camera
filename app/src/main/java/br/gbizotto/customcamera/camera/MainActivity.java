package br.gbizotto.customcamera.camera;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;


import br.gbizotto.customcamera.R;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Camera mCamera;
    private CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Create an instance of Camera
        mCamera = CameraUtils.getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    @OnClick(R.id.button_capture)
    public void onCaptureButtonClick() {
        Camera.PictureCallback pictureCallback = CameraUtils.getPictureCallback();
        mCamera.takePicture(null, null, pictureCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCamera = CameraUtils.releaseCamera(mCamera);
    }
}
