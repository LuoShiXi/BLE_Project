package com.cheyitianxia.bletools;

import com.cheyitianxia.help.GlobalVariable;
import com.cytx.model.MixedValues;
import com.cytx.view.DeviceView;
import com.mt.mtbletools.R;
import com.mt.sdk.ble.MTBLEDevice;
import com.mt.sdk.ble.MTBLEManager;
import com.mt.sdk.ble.MTBLEDevice.MTBLEDEVICETYPE;
import com.mt.sdk.ble.MTBLEManager.MTScanCallback;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TestDeviceList extends FragmentActivity {

	// 扫描设备的变量声明
	public static final String SELECTDEVICE = "SELECTDEVICE";
	private MTBLEManager manger;
	private GlobalVariable mGlobalVariable;

	private ListView lv_paired;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device);

		// 扫描设备时做的声明
		mGlobalVariable = GlobalVariable.getinstance();
		this.manger = mGlobalVariable.getBleManger();

		if (!this.manger.isEnable()) {
			startActivityForResult(new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
		}

		lv_paired = (ListView) findViewById(R.id.paired_devices);

		lv_paired.setAdapter(listadapt);

		lv_paired.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if ((position > (mGlobalVariable.getDeviceList().size() - 1))
						|| (position < 0)) {
					return;
				}

				MTBLEDevice device = mGlobalVariable.getDeviceList().get(
						position);

				if (device.getSetlev() > 1) {
					Toast.makeText(getBaseContext(), "不允许连接",
							Toast.LENGTH_SHORT).show();
					return;
				}

				// 选择搜索到的BLE设备，跳转到 MTSeriTalkActivity，并连接到设备
				if (device.getDevicetype() == MTBLEDEVICETYPE.MTBLE) {
					Intent intent = new Intent();
//					intent.setClass(TestDeviceList.this,
//							MTSeriTalkActivity.class);
					intent.putExtra(SELECTDEVICE, position);
					setResult(Activity.RESULT_OK, intent);
					finish();
					return;
				}
			}
		});
	}

	// 扫描到的设备->列表适配器
	private BaseAdapter listadapt = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (null == convertView) {
				convertView = new DeviceView(getBaseContext());
			}

			((DeviceView) convertView).setDatas(getItem(position));

			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public MTBLEDevice getItem(int position) {
			return mGlobalVariable.getDeviceList().get(position);
		}

		@Override
		public int getCount() {
			return mGlobalVariable.getDeviceList().size();
		}
	};

	// 扫描过滤器
	private boolean scanFilter(MTBLEDevice device) {

		if ((mGlobalVariable.getMixedvalues().getUsermodel() == MixedValues.SMARTUSER)
				|| (mGlobalVariable.getMixedvalues().getUsermodel() == MixedValues.DEVELOPUSER)) { // 智能识别用户，所有设备都可以扫描到
			return true;
		}

		if (mGlobalVariable.getMixedvalues().getUsermodel() == MixedValues.MTBEACONUSER) { // MTBeacon用户，所有设备都可以扫描到

			if ((device.getDevicetype() == MTBLEDEVICETYPE.MTBeacon1)
					|| (device.getDevicetype() == MTBLEDEVICETYPE.MTBeacon2)
					|| (device.getDevicetype() == MTBLEDEVICETYPE.MTBeacon3)
					|| (device.getDevicetype() == MTBLEDEVICETYPE.MTBeacon4)) {
				return true;
			}
			return false;
		}

		if (mGlobalVariable.getMixedvalues().getUsermodel() == MixedValues.MTSERIUSER) { // MTSeri用户，所有设备都可以扫描到

			if (device.getDevicetype() == MTBLEDEVICETYPE.MTBLE) {
				return true;
			}
			return false;
		}

		if (mGlobalVariable.getMixedvalues().getUsermodel() == MixedValues.MTWXUSER) { // MTSeri用户，所有设备都可以扫描到

			if (device.getDevicetype() == MTBLEDEVICETYPE.MTWX) {
				return true;
			}
			return false;
		}

		return true;
	}

	// 重写onResume()函数，进行蓝牙设备的扫描
	@Override
	public void onResume() {
		super.onResume();
		System.out.println("onResume");
		manger.startScan(scanCallback);// 扫描蓝牙设备
	}

	@Override
	public void onPause() {
		super.onPause();
		manger.stopScan();
	}

	// 扫描回调
	private MTScanCallback scanCallback = new MTScanCallback() {

		@Override
		public void onScanFail(int errocode, String erromsg) {

		}

		@Override
		public void onScan(MTBLEDevice device) {

			if (!scanFilter(device)) { // 不符合规则的，就不要继续进行
				return;
			}

			for (MTBLEDevice MTBLEDevice : mGlobalVariable.getDeviceList()) {
				if (MTBLEDevice.getDevice().getAddress()
						.equals(device.getDevice().getAddress())) {
					MTBLEDevice.reflashInf(device);
					listadapt.notifyDataSetChanged();
					return;
				}
			}
			mGlobalVariable.getDeviceList().add(device);
			listadapt.notifyDataSetChanged();
		}
	};
}