package com.cheyitianxia.help;

import java.util.ArrayList;
import java.util.List;

import org.xutils.DbManager;

import com.cytx.model.MixedValues;
import com.cytx.model.UuidAndName;
import com.cytx.net.RequestWithToken;
import com.mt.sdk.ble.MTBLEDevice;
import com.mt.sdk.ble.MTBLEManager;

public class GlobalVariable {
	private static GlobalVariable mGlobalVariable;

	public static GlobalVariable getinstance() {
		if (mGlobalVariable == null) {
			mGlobalVariable = new GlobalVariable();
		}
		return mGlobalVariable;
	}

	private GlobalVariable() {
	}

	private MixedValues mixedvalues;
	private List<MTBLEDevice> deviceList = new ArrayList<MTBLEDevice>(); // 搜索到的所有设备
	private DbManager db; // 数据库
	private MTBLEManager blemanger; // 蓝牙
	private RequestWithToken mRequestWithToken; // 网络请求
	private List<UuidAndName> uuids = new ArrayList<UuidAndName>(); // 所有储存的uuid

	public DbManager getDb() {
		return db;
	}

	public void setDb(DbManager db) {
		this.db = db;
	}

	public List<MTBLEDevice> getDeviceList() {
		return deviceList;
	}

	public MTBLEManager getBleManger() {
		return blemanger;
	}

	public void setBleManger(MTBLEManager blemanger) {
		this.blemanger = blemanger;
	}

	public RequestWithToken getmRequestWithToken() {
		return mRequestWithToken;
	}

	public void setmRequestWithToken(RequestWithToken mRequestWithToken) {
		this.mRequestWithToken = mRequestWithToken;
	}

	public List<UuidAndName> getUuids() {
		return uuids;
	}

	public void setUuids(List<UuidAndName> uuids) {

		if (uuids == null) {
			return;
		}
		this.uuids = uuids;
	}

	public MixedValues getMixedvalues() {
		return mixedvalues;
	}

	public void setMixedvalues(MixedValues mixedvalues) {
		this.mixedvalues = mixedvalues;
	}

}
