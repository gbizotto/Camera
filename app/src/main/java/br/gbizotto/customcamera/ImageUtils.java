package br.gbizotto.customcamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by gabriela on 4/26/17.
 */

public final class ImageUtils {

    private ImageUtils() {
    }

    public static void loadImageAtImageView(final Context context, final ImageView imageView, final Uri uri) {
        if (uri != null) {
            Picasso picasso = Picasso.with(context);
            picasso.invalidate(uri);
            picasso.load(preparePicturePath(uri))
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            imageView.setImageBitmap(getBitmap(uri));
                        }
                    });
        }
    }


    public static Uri getUriFromFile(String fileName) {
        return Uri.parse(new File(fileName).toString());
    }



    private static Uri preparePicturePath(Uri uri) {
        return uri.getScheme() != null ? uri : new Uri.Builder().scheme("file") .appendEncodedPath("/"+uri.getPath()).build();
    }

    private static Bitmap getBitmap(Uri imageUri) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        return BitmapFactory.decodeFile(imageUri.getPath(), options);
    }

    public static String getFileContentBase64(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            int size = (int) file.length();
            byte[] bytes = new byte[size];
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
            return Base64.encodeToString(bytes, Base64.DEFAULT).replaceAll("\\n", "").trim();
        }
        return null;
    }
}
