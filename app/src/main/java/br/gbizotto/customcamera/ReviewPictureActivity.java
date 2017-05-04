package br.gbizotto.customcamera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @OnClick(R.id.btn_accept)
    public void onOkClick() {
        startActivity(new Intent(this, RedirectActivity.class));
    }
}
