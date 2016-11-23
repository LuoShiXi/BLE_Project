package com.cytx.model;

import android.bluetooth.BluetoothGattService;

public class ServiceModel {
	private String name = "unknow";
	private BluetoothGattService service;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BluetoothGattService getService() {
		return service;
	}
	public void setService(BluetoothGattService service) {
		this.service = service;
	}
}
