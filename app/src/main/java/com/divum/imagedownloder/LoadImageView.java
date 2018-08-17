package com.divum.imagedownloder;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.divum.imagedownloder.ImageCacheDetails.AnimateFirstDisplayListener;
import com.divum.silvan.R;
import com.divum.silvan.SplashActivity;
import com.divum.utils.App_Variable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


public class LoadImageView {

	static DisplayImageOptions options;
	public static ImageLoader imageLoader;
	static ImageLoadingListener animateFirstListener;
	static Context contact;

	public LoadImageView(Context _contact) {
		// TODO Auto-generated constructor stub
		contact=_contact;
		imageLoader = ImageLoader.getInstance();
		animateFirstListener = new AnimateFirstDisplayListener();
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.splash_main)
		.showImageForEmptyUri(R.drawable.splash_main)
		.showImageOnFail(R.drawable.splash_main)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new RoundedBitmapDisplayer(0))
		.build();
	}

	public static void LoadImage(String imageUrl,ImageView image) {
		// TODO Auto-generated constructor stub		
		try{
			System.out.println("image url:"+imageUrl);
			imageLoader.displayImage(imageUrl,image, options,animateFirstListener);
		}catch (Exception e) {
			// TODO: handle exception
			Log.e("LoadImageView   ", "error  "+e);
		}
	}

	public static void SplashImage(String url, ImageView img_splash,final Context context) {
		// TODO Auto-generated method stub
		// Load image, decode it to Bitmap and display Bitmap in ImageView (or any other view 
		//  which implements ImageAware interface)
		imageLoader.displayImage(url, img_splash, options, new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				Log.d("load","onLoadingStarted");

			}
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				Log.d("load","onLoadingFailed");
				App_Variable.CONFIG_NUMBER="0000";
				((SplashActivity) context).NextScreen();
			}
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				Log.d("load","onLoadingComplete");
				((SplashActivity) context).NextScreen();
			}
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				Log.d("load","onLoadingCancelled");
				App_Variable.CONFIG_NUMBER="0000";
				((SplashActivity) context).NextScreen();
			}
		});
	}	
}
