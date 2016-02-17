package com.example.logme.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//start up the service at boot.
public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
		Intent pushIntent = new Intent(context, MyService.class);
		context.startService(pushIntent);
		}
	}
}
