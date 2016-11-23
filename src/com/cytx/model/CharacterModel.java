package com.cytx.model;

import android.bluetooth.BluetoothGattCharacteristic;

public class CharacterModel{
	private String name = "unknow";
	private BluetoothGattCharacteristic charact;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BluetoothGattCharacteristic getCharact() {
		return charact;
	}
	public void setCharact(BluetoothGattCharacteristic charact) {
		this.charact = charact;
	}
}
