package com.mathisonian.android.whisprabbit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageDisplayActivity extends Activity {
    /** Called when the activity is first created. */

	static ProgressDialog dialog = null;
	
	static final String TAG = "MyActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_display);
        
        final Button button = (Button) findViewById(R.id.ImageResponse);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				response();
			}
		});
        
        TextView tv = (TextView) findViewById(R.id.imageCaption);
//        Log.v(TAG, "text: " + getIntent().getStringExtra("CAPTION"));
        tv.setText(getIntent().getStringExtra("CAPTION"));
        setPicture(getIntent().getStringExtra("URL"));
    }


private void setPicture(String url) {
//	Log.v(TAG, "URL: " + url);
	dialog = ProgressDialog.show(ImageDisplayActivity.this, "","Loading. Please wait...", true);
	new DownloadImageTask().execute(url);
}

private void response() {
	Intent resultIntent = new Intent();
    resultIntent.putExtra("isPost", true);
    setResult(Activity.RESULT_OK, resultIntent);
    finish();
}

public static String getStackTrace(Throwable aThrowable) {
	final Writer result = new StringWriter();
	final PrintWriter printWriter = new PrintWriter(result);
	aThrowable.printStackTrace(printWriter);
	return result.toString();
}


private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    /** The system calls this to perform work in a worker thread and
      * delivers it the parameters given to AsyncTask.execute() */
    protected Bitmap doInBackground(String... urls) {
        return ImageLoader.getBitmap(urls[0]);
    }
    
    /** The system calls this to perform work in the UI thread and delivers
      * the result from doInBackground() */
    protected void onPostExecute(Bitmap result) {
    	ImageView imageView = (ImageView) findViewById(R.id.imageAttachment);
        imageView.setImageBitmap(result);
        dialog.dismiss();
    }
}
}