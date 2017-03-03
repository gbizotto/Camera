package br.gbizotto.customcamera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import br.gbizotto.customcamera.camera.MainActivity;
import br.gbizotto.customcamera.camera2.CameraActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        startActivity(new Intent(this, MainActivity.class));
    }

    @OnClick(R.id.btn_newer)
    public void onNewerClick() {
        startActivity(new Intent(this, CameraActivity.class));
    }

}
