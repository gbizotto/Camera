package br.gbizotto.customcamera.camera;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;


import br.gbizotto.customcamera.PictureTaken;
import br.gbizotto.customcamera.R;
import br.gbizotto.customcamera.ReviewPictureActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OlderVersionsActivity extends AppCompatActivity implements PictureTaken {

    private static final String TAG = OlderVersionsActivity.class.getSimpleName();

    @BindView(R.id.camera_preview)
    FrameLayout mFramePreview;

    private Camera mCamera;
    private CameraPreview mPreview;

    private final boolean mIsFrontFacing = false;
    private final String mFileName = "custom-camera-older";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Create an instance of Camera
        mCamera = CameraUtils.getCameraInstance(mIsFrontFacing, this);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        mFramePreview.addView(mPreview);
    }

    @OnClick(R.id.button_capture)
    public void onCaptureButtonClick() {
        Camera.PictureCallback pictureCallback = CameraUtils.getPictureCallback(this, mFileName, this, mIsFrontFacing);
        mCamera.takePicture(null, null, pictureCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCamera = CameraUtils.releaseCamera(mCamera);
    }

    @Override
    public void pictureSaved(String filePath) {
        Intent intent = new Intent(this, ReviewPictureActivity.class);
        intent.putExtra("filePath", filePath);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}
