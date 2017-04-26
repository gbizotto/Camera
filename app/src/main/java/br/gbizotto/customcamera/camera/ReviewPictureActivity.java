package br.gbizotto.customcamera.camera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import br.gbizotto.customcamera.ImageUtils;
import br.gbizotto.customcamera.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewPictureActivity extends AppCompatActivity {

    @BindView(R.id.img_picture)
    ImageView mImgPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_picture);

        ButterKnife.bind(this);

        String filePath = getIntent().getStringExtra("filePath");

        ImageUtils.loadImageAtImageView(this, mImgPicture, ImageUtils.getUriFromFile(filePath));
    }
}
