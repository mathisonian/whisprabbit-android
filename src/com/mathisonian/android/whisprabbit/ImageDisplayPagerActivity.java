package com.mathisonian.android.whisprabbit;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageDisplayPagerActivity extends FragmentActivity {

	static int NUM_ITEMS;
	static Context context;
	static ImageDisplayFragment imageDisplayFragment;
	static String server = "http://www.whisprabbit.com";

	MyAdapter mAdapter;
	static ArrayList<String> responses;
	static ArrayList<String> images;

	ViewPager mPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_display_pager);
		Intent intent = getIntent();
		responses = intent.getStringArrayListExtra("RESPONSES");
		images = intent.getStringArrayListExtra("IMAGES");
		NUM_ITEMS = responses.size();

		mAdapter = new MyAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.DisplayPager);
		mPager.setAdapter(mAdapter);
		context = this;
		imageDisplayFragment = new ImageDisplayFragment();
	}

	public static class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			return ImageDisplayFragment.newInstance(position);
		}
	}

	public static class ImageDisplayFragment extends Fragment {
		int mNum;

		public static ImageDisplayFragment newInstance(int index) {
			ImageDisplayFragment f = new ImageDisplayFragment();

			// Supply index input as an argument.
			Bundle args = new Bundle();
			args.putInt("index", index);
			f.setArguments(args);

			return f;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mNum = getArguments() != null ? getArguments().getInt("index") : 1;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			if (container == null)
				return null;

			View v = inflater.inflate(R.layout.image_display, container, false);
			TextView tv = (TextView) v.findViewById(R.id.imageCaption);
			tv.setText(responses.get(mNum));

			ImageView iv = (ImageView) v.findViewById(R.id.imageAttachment);
			iv.setImageBitmap(ImageLoader.getBitmap(server + "/uploads/" + images.get(mNum)));

			return v;
		}
	}
}
