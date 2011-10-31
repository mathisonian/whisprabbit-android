package com.mathisonian.android.whisprabbit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity {
	String server = "http://www.whisprabbit.com";
	static final String TAG = "MyActivity";
	static ProgressDialog dialog = null;
	static final int POST_RESULTS = 0;
	ListView lv;
	ArrayAdapter<String> adapter;
	ArrayList<String> tagList;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.search_layout);
		try {

			final Button button = (Button) findViewById(R.id.SearchButton);
	        button.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                search();
	            }
	        });
	        
	        lv = (ListView) this.findViewById(R.id.SearchList);
	        
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					search(position);
				}
			});
	        
	        tagList = new ArrayList<String>();
	        adapter = new ArrayAdapter<String>(this, R.layout.list_item, tagList);
			updateList();
			lv.setAdapter(adapter);

		} catch(Exception e) {
//			Log.v(TAG, getStackTrace(e));
		}
			
    }
    
    void updateList() {
	    adapter.clear();
	    
    	try {
	    	String urlString = server + "/php/getTags.php";
	    	
			URL url = new URL(urlString);
			URLConnection urlConnection = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			 
			String json = "";
			String line;
			while ((line = in.readLine()) != null) {
			  json += line;
			}
			
//			Toast.makeText(getApplicationContext(), json, Toast.LENGTH_SHORT).show();
			
			JSONArray ja = new JSONArray(json);
			int length = ja.length();

			for (int i = 0; i < length; i++) {
				JSONObject jo = ja.getJSONObject(i);
				 adapter.add(jo.getString("tag").replace("\n", " ").trim());
			}
		} catch(Exception e) {
//			Log.v(TAG, getStackTrace(e));
		}
		adapter.notifyDataSetChanged();
    }
    
	public static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}
	
    void search(int position) {
    	Intent resultIntent = new Intent();
	    resultIntent.putExtra("SEARCH_TERM", adapter.getItem(position));
	    setResult(Activity.RESULT_OK, resultIntent);
	    finish();
    }
    
    void search() {
    	Intent resultIntent = new Intent();
    	EditText editText = (EditText) findViewById(R.id.SearchText);
	    resultIntent.putExtra("SEARCH_TERM", editText.getText().toString());
	    setResult(Activity.RESULT_OK, resultIntent);
	    finish();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.thread_bottom_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.thread_refresh:
	        updateList();
	        return true;
	    case R.id.thread_sort:
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}
