package com.cytx.view;

import com.mt.mtbletools.R;
import com.mt.sdk.ble.MTBLEDevice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DeviceView extends LinearLayout {

	private Context context;

	public DeviceView(Context context) {
		super(context);
		this.context = context;

		initView();
	}

	// 初始化控件
	private TextView devicename_txt;
	private TextView device_rssi_txt;
	private TextView battery_txt;
	private TextView device_mac_txt;

	private LinearLayout ibeacon_layout;
	private TextView settype_txt;
	private TextView device_major_txt;
	private TextView device_minor_txt;
	private TextView device_measurepower_txt;
	private TextView device_uuid_txt;

	private void initView() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.fmt_device, this);

		devicename_txt = (TextView) findViewById(R.id.devicename_txt);
		device_rssi_txt = (TextView) findViewById(R.id.device_rssi_txt);
		battery_txt = (TextView) findViewById(R.id.battery_txt);
		device_mac_txt = (TextView) findViewById(R.id.device_mac_txt);
		ibeacon_layout = (LinearLayout) findViewById(R.id.ibeacon_layout);
		settype_txt = (TextView) findViewById(R.id.settype_txt);
		device_major_txt = (TextView) findViewById(R.id.device_major_txt);
		device_minor_txt = (TextView) findViewById(R.id.device_minor_txt);
		device_measurepower_txt = (TextView) findViewById(R.id.device_measurepower_txt);
		device_uuid_txt = (TextView) findViewById(R.id.device_uuid_txt);
	}

	// 设置数据，更新UI
	public void setDatas(MTBLEDevice device) {
		// 更新蓝牙基本参数
		devicename_txt.setText(device.getDevice().getName());
		device_rssi_txt.setText(
				getResources().getString(R.string.rssi) + device.getAvgRssi());
		device_mac_txt.setText(getResources().getString(R.string.mac) + device.getDevice().getAddress());

		// 更新普通BLE UI
		if (device.getDevicetype() == MTBLEDevice.MTBLEDEVICETYPE.BLE) {
			battery_txt.setVisibility(View.INVISIBLE);
			ibeacon_layout.setVisibility(View.GONE);
			return;
		}

		// 更新MTSeri UI
		if (device.getDevicetype() == MTBLEDevice.MTBLEDEVICETYPE.MTBLE) {
			battery_txt.setVisibility(View.VISIBLE);
			ibeacon_layout.setVisibility(View.GONE);

			battery_txt.setText(getResources().getString(R.string.battery) + device.getBattery()
					+ getResources().getString(R.string.percent));
			return;
		}
		
		// 更新MTWX UI
		if (device.getDevicetype() == MTBLEDevice.MTBLEDEVICETYPE.MTWX) {
			battery_txt.setVisibility(View.VISIBLE);
			ibeacon_layout.setVisibility(View.GONE);

			battery_txt.setText(getResources().getString(R.string.battery) + device.getBattery()
					+ getResources().getString(R.string.percent));
			return;
		}

		// 更新IBeacon UI
		if ((device.getDevicetype() == MTBLEDevice.MTBLEDEVICETYPE.IBeacon) || (device.getDevicetype() == MTBLEDevice.MTBLEDEVICETYPE.MTBeacon1)
				|| (device.getDevicetype() == MTBLEDevice.MTBLEDEVICETYPE.MTBeacon2)
				|| (device.getDevicetype() == MTBLEDevice.MTBLEDEVICETYPE.MTBeacon3)
				|| (device.getDevicetype() == MTBLEDevice.MTBLEDEVICETYPE.MTBeacon4)) {
			battery_txt.setVisibility(View.INVISIBLE);;
			ibeacon_layout.setVisibility(View.VISIBLE);
			settype_txt.setVisibility(View.GONE);

			device_major_txt.setText(getResources().getString(R.string.major) + device.getMajor());
			device_minor_txt.setText(getResources().getString(R.string.minor) + device.getMinor());
			device_measurepower_txt
					.setText(getResources().getString(R.string.measur) + device.getMeasuedPower());
			device_uuid_txt.setText(getResources().getString(R.string.uuid) + device.getBeaconUUID());
		}

		// 更新MTBeacon1参数
		if (device.getDevicetype() == MTBLEDevice.MTBLEDEVICETYPE.MTBeacon1) {
			battery_txt.setVisibility(View.VISIBLE);
			battery_txt.setText(getResources().getString(R.string.battery) + device.getBattery()
					+ getResources().getString(R.string.percent));
			return;
		}
		// 更新MTBeacon2参数
		if (device.getDevicetype() == MTBLEDevice.MTBLEDEVICETYPE.MTBeacon2) {
			battery_txt.setVisibility(View.VISIBLE);
			settype_txt.setVisibility(View.VISIBLE);
			battery_txt.setText(getResources().getString(R.string.battery) + device.getBattery()
					+ getResources().getString(R.string.percent));
			settype_txt.setText(
					getResources().getString(R.string.setlv) + MTBLEDevice.getSetLvName(device.getSetlev()));
			return;
		}

		// 更新MTBeacon3参数
		if (device.getDevicetype() == MTBLEDevice.MTBLEDEVICETYPE.MTBeacon3) {
			battery_txt.setVisibility(View.VISIBLE);
			settype_txt.setVisibility(View.VISIBLE);
			battery_txt.setText(getResources().getString(R.string.battery) + device.getBattery()
					+ getResources().getString(R.string.percent));
			settype_txt.setText(
					getResources().getString(R.string.setlv) + MTBLEDevice.getSetLvName(device.getSetlev()));
			return;
		}
		// 更新MTBeacon4参数
		if (device.getDevicetype() == MTBLEDevice.MTBLEDEVICETYPE.MTBeacon4) {
			battery_txt.setVisibility(View.VISIBLE);
			settype_txt.setVisibility(View.VISIBLE);
			battery_txt.setText(getResources().getString(R.string.battery) + device.getBattery()
					+ getResources().getString(R.string.percent));
			settype_txt.setText(
					getResources().getString(R.string.setlv) + MTBLEDevice.getSetLvName(device.getSetlev()));
			return;
		}

	}
}
