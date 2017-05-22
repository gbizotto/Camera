/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.gbizotto.customcamera.camera2;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;

import java.io.File;

import br.gbizotto.customcamera.PermissionsUtils;
import br.gbizotto.customcamera.PictureTaken;
import br.gbizotto.customcamera.R;
import br.gbizotto.customcamera.ReviewPictureActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@RequiresApi(api = 21)
public class NewerVersionsActivity extends AppCompatActivity implements PictureTaken {

    @BindView(R.id.texture)
    AutoFitTextureView mTextureView;

    private File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        mFile = new File(getExternalFilesDir(null), "pic.jpg");
    }

    @Override
    protected void onResume() {
        super.onResume();

        CameraUtils.startCamera(this, this, mFile, mTextureView, this);

        if (mTextureView.isAvailable()) {
            CameraUtils.openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(buildSurfaceTextureListener());
        }
    }

    @Override
    public void onPause() {
        CameraUtils.stopCamera();
        super.onPause();
    }

    @OnClick(R.id.button_capture)
    public void onTakePictureClick() {
        takePicture();
    }

    private void takePicture() {
        if (PermissionsUtils.checkCameraPermission(this)) {
            CameraUtils.lockFocus(this, this);
        }
    }

    @Override
    public void pictureSaved(String filePath) {
        Intent intent = new Intent(this, ReviewPictureActivity.class);
        intent.putExtra("filePath", filePath);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private TextureView.SurfaceTextureListener buildSurfaceTextureListener() {
        return new TextureView.SurfaceTextureListener() {

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
                CameraUtils.openCamera(width, height);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
                CameraUtils.configureTransform(width, height);
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture texture) {
            }

        };
    }
}
