package com.mathisonian.android.whisprabbit;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class ImageLoader {
	
	final int stub_id = R.drawable.stub;
	
	public void DisplayImage(String url, ImageView imageView) {
		Bitmap bitmap = getBitmap(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			imageView.setImageResource(stub_id);
		}
	}

	public static Bitmap getBitmap(String urlString) {
		Bitmap bm = null;
		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bm;
	}
}
