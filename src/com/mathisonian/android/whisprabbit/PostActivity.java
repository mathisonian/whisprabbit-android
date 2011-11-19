package com.mathisonian.android.whisprabbit;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.urbanairship.push.PushManager;
import com.urbanairship.push.PushPreferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
//import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
//import android.widget.Toast;

public class PostActivity extends Activity {
	String server = "http://www.whisprabbit.com";
	final int IMAGE_ACTIVITY = 1;
	Bitmap myImage = null;
	static final String TAG = "MyActivity";
	CheckBox checkBox;
	static ProgressDialog dialog = null;

	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.post_layout);

			final Button button = (Button) findViewById(R.id.ButtonCreate);
			button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					createPost();
				}
			});

			final Button cameraButton = (Button) findViewById(R.id.ButtonCamera);
			cameraButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent cameraIntent = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(cameraIntent, IMAGE_ACTIVITY);
				}
			});
			
			final EditText editText = (EditText) findViewById(R.id.textContent);
			editText.setOnEditorActionListener(new OnEditorActionListener() {
		        public boolean onEditorAction(TextView v, int actionId,
		                KeyEvent event) {
		            if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
		                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		                in.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		                return true;
		            }
		            return false;
		        }
		    });

			myImage = (Bitmap) getLastNonConfigurationInstance();
			Intent intent = getIntent();
			
			checkBox = (CheckBox) findViewById(R.id.checkBoxPrivate);
			if (!intent.getBooleanExtra("isThread", false)) {
				checkBox.setVisibility(View.GONE);
			}
			
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Invite");
			alert.setMessage("Enter a @twitterHandle or email address");

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			  String value = input.getText().toString();
			  // Do something with value!
			  }
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});

//			alert.show();
		} catch (Exception e) {
//			Log.v(TAG, getStackTrace(e));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == IMAGE_ACTIVITY) {
			if (resultCode == RESULT_CANCELED) {
				return;
			}
			myImage = (Bitmap) data.getExtras().get("data");
		}
	}

	void createPost() {
		EditText editText = (EditText) findViewById(R.id.textContent);
		String myContent = editText.getText().toString();
		String url = server + "/php/";
		Intent intent = getIntent();
		String t_id = "";
		String uploadName;
		String isPrivate = "0";
		if (intent.getBooleanExtra("isThread", false)) {
			url += "createThread.php";
			uploadName = "pictureUpload";
			if(checkBox.isChecked()) {
				isPrivate = "1";
			}
			
		} else {
			url += "createResponse.php";
			t_id = intent.getStringExtra("t_id");
			uploadName = "responseUpload";
		}

		// Create a new HttpClient and Post Header
		HttpPost httppost = new HttpPost(url);

		try {
			// Add data
			MultipartEntity mp = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			if (myImage != null) {
				
				Matrix mtx = new Matrix();
				mtx.postRotate(-90);
				Bitmap rotatedBitmap = Bitmap.createBitmap(Bitmap.createBitmap(
						myImage, 0, 0, myImage.getWidth(), myImage.getHeight(),
						mtx, true));

				ByteArrayOutputStream bos = new ByteArrayOutputStream();

				rotatedBitmap.compress(CompressFormat.PNG, 100, bos);

				byte[] data = bos.toByteArray();
				mp.addPart(uploadName, new ByteArrayBody(data, "temp.png"));
			}
			mp.addPart("c", new StringBody(myContent, Charset.forName("UTF-8")));
			
			if (intent.getBooleanExtra("isThread", false)) {
				mp.addPart("p", new StringBody(isPrivate, Charset.forName("UTF-8")));				
			} else {
				mp.addPart("t", new StringBody(t_id, Charset.forName("UTF-8")));
			}
	        
			PushPreferences prefs = PushManager.shared().getPreferences();
			mp.addPart("a", new StringBody(prefs.getPushId(), Charset.forName("UTF-8")));

			httppost.setEntity(mp);
			dialog = ProgressDialog.show(PostActivity.this, "","Creating post, please wait...", true, true);
			new PostTask().execute(httppost);

		} catch (Exception e) {
		}
	}
	
	private class PostTask extends AsyncTask<HttpPost, Void, Boolean> {
		
		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 * @return 
		 */
		protected Boolean doInBackground(HttpPost... httppost) {
			HttpClient httpclient = new DefaultHttpClient();
			// Execute HTTP Post Request
			try {
				httpclient.execute(httppost[0]);
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		/**
		 * The system calls this to perform work in the UI thread and delivers
		 * the result from doInBackground()
		 */
		protected void onPostExecute(Boolean result) {
			Intent resultIntent = new Intent();
			resultIntent.putExtra("POST_IDENTIFIER", 1);
			if(result) {
				setResult(Activity.RESULT_OK, resultIntent);				
			}
			dialog.dismiss();
			finish();
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		final Bitmap data = myImage;
		return data;
	}

	public static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}
}
