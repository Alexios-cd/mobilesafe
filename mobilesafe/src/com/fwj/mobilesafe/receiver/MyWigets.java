package com.fwj.mobilesafe.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.fwj.mobilesafe.service.WidgetsService;

public class MyWigets extends AppWidgetProvider {

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		//System.out.println("onReceive");
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Intent intent=new Intent(context,WidgetsService.class);
		context.startService(intent);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		//System.out.println("onDeleted");
		Intent intent=new Intent(context,WidgetsService.class);
		context.stopService(intent);
	}
	//  
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		//System.out.println("onEnabled");
		
		
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		System.out.println("onDisabled");
	}
	
}
