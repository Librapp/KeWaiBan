package com.lims.kewaiban;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;

import com.lims.kewaiban.util.SPUtil;

/**
 * @author Luke
 * @date 2015年7月28日下午3:08:21
 * @description
 */
public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.splash_activity);
		if (SPUtil.getInstance(this).getInt("category", 0) == 0) {
			new AlertDialog.Builder(this)
					.setItems(R.array.category, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							SPUtil.getInstance(SplashActivity.this).putInt(
									"category", which);
							dialog.dismiss();
						}
					}).setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							startActivity(new Intent(SplashActivity.this,
									MainActivity.class));
						}
					}).create().show();
		} else
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
		super.onCreate(savedInstanceState);
	}
}
