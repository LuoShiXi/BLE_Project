package com.cheyitianxia.bletools;

import java.util.ArrayList;
import java.util.List;

import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.ex.DbException;

import com.cheyitianxia.help.GlobalVariable;
import com.cytx.model.MixedValues;
import com.cytx.model.UuidAndName;
import com.cytx.net.RequestWithToken;
import com.mt.sdk.ble.MTBLEManager;

import android.app.Application;

public class MTTBleToolsApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		getDatas();

		initInternet();

		initBLE();
	}

	// 初始化数据
	private GlobalVariable mGlobalVariable;

	private void getDatas() {
		mGlobalVariable = GlobalVariable.getinstance();

		x.Ext.init(this);
		DbManager db = x.getDb(new DbManager.DaoConfig().setDbName("mtcarp.db").setDbVersion(1)
				.setDbOpenListener(new DbManager.DbOpenListener() {
					@Override
					public void onDbOpened(DbManager db) {
						db.getDatabase().enableWriteAheadLogging();
					}
				}).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
					@Override
					public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
					}
				}));
		mGlobalVariable.setDb(db);

		try {
			List<UuidAndName> uuids = mGlobalVariable.getDb().findAll(UuidAndName.class);
			
			if(uuids == null){
				uuids = new ArrayList<UuidAndName>();
				uuids.add(new UuidAndName("微信FD", "FDA50693-A4E2-4FB1-AFCF-C6EB07647825"));
				uuids.add(new UuidAndName("微信AB", "AB8190D5-D11E-4941-ACC4-42F30510B408"));
				mGlobalVariable.getDb().save(uuids);
				
				uuids = mGlobalVariable.getDb().findAll(UuidAndName.class);
			}
			mGlobalVariable.setUuids(uuids);
			
			MixedValues mixedvalues = mGlobalVariable.getDb().findFirst(MixedValues.class);
			if(mixedvalues == null){
				System.out.println("mixedvalues == null");
				mixedvalues = new MixedValues();
				mGlobalVariable.getDb().save(mixedvalues);
				mixedvalues = mGlobalVariable.getDb().findFirst(MixedValues.class);
			}else{
				System.out.println("mixedvalues != null");
			}
			mGlobalVariable.setMixedvalues(mixedvalues);
			
		} catch (DbException e) {
			e.printStackTrace();
			System.out.println("DbException->"+e.toString());
		}
	}

	// 初始化网络
	private void initInternet() {
		mGlobalVariable.setmRequestWithToken(RequestWithToken.getInstance(getApplicationContext()));
	}

	// 初始化蓝牙
	private void initBLE() {
		mGlobalVariable.setBleManger(MTBLEManager.getInstance(getApplicationContext()));
	}

}
