package com.fwj.mobilesafe.service;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class GpsService extends Service {
	private LocationManager manager;// 手机位置的管理者
	private SharedPreferences sp;
	private MyLocationListener listener;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		sp=getSharedPreferences("config", MODE_PRIVATE);
		manager=(LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria=new Criteria();
		criteria.setAltitudeRequired(true);
		String bestProvider = manager.getBestProvider(criteria, true);
		listener = new MyLocationListener();
		manager.requestLocationUpdates(bestProvider, 0, 0, listener);
		
	}
	private class MyLocationListener implements LocationListener{
		//  当位置发生变化的时候 
		@Override
		public void onLocationChanged(Location location) {
			String latitude = "latitude"+location.getLatitude();  // 维度
			String longitude = "longitude"+location.getLongitude();// 经度
			Editor edit = sp.edit();
			edit.putString("latitude", latitude);
			edit.putString("longitude", longitude);
			edit.commit();
		}
		// 当定位方式状态发生变化    不可用-> 可用   可用->不可用
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		// 可用
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		// 不可用
		@Override
		public void onProviderDisabled(String provider) {
			
		}
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(listener!=null){
			manager.removeUpdates(listener);  // 搜狗    cpu级别省电优化
			listener=null;
		}
	}
}
