package br.gbizotto.customcamera;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import br.gbizotto.customcamera.camera.OlderVersionsActivity;
import br.gbizotto.customcamera.camera2.NewerVersionsActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class RedirectActivity extends AppCompatActivity {

    @BindView(R.id.btn_older)
    Button mBntOlder;
    @BindView(R.id.btn_newer)
    Button mBntNewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_older)
    public void onOlderClick() {
        RedirectActivityPermissionsDispatcher.openCameraWithCheck(this);
    }

    @OnClick(R.id.btn_newer)
    public void onNewerClick() {
        RedirectActivityPermissionsDispatcher.openCameraWithCheck(this);
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void openCamera() {

//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ) {
//            startActivity(new Intent(this, OlderVersionsActivity.class));
//        } else {
//            startActivity(new Intent(this, NewerVersionsActivity.class));
//        }

        startActivity(new Intent(this, OlderVersionsActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RedirectActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
