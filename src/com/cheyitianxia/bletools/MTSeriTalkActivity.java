package com.cheyitianxia.bletools;

import com.cheyitianxia.bletools.TalkFragment.Msg.DIR;
import com.cheyitianxia.help.GlobalVariable;
import com.mt.mtbletools.R;
import com.mt.sdk.ble.MTBLEDevice;
import com.mt.sdk.ble.MTBLEManager;
import com.mt.sdk.ble.base.MTSeriaBase;
import com.mt.sdk.ble.model.BLEBaseAction.Option;
import com.mt.sdk.ble.model.ErroCode;
import com.mt.sdk.ble.model.WriteCharactWithAckAction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

public class MTSeriTalkActivity extends FragmentActivity {
	private GlobalVariable mGlobalVariable;
	private MTBLEManager manger;
	private MTSeriaBase mBle;
	private static final int REQUEST_CONNECT_DEVICE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backfragment);

		mGlobalVariable = GlobalVariable.getinstance();
		manger = mGlobalVariable.getBleManger();

		mBle = new MTSeriaBase(getApplicationContext(), manger);
		initFragment();

		// getDefaultDatas();

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			// 当DeviceListActivity返回与设备连接的消息
			if (resultCode == Activity.RESULT_OK) {
				// 当DeviceListActivity返回与设备连接的消息
				index = data.getIntExtra(TestDeviceList.SELECTDEVICE, -1);
				if (index == -1) {
					finish();
					return;
				}
				if (mGlobalVariable.getDeviceList().size() == 0) {
					finish();
					return;
				}
				device = mGlobalVariable.getDeviceList().get(index);
				startConnect();
			}
			break;
		}
	}

	// 获取传送过来的数据
	private MTBLEDevice device;
	private int index;

	// private void getDefaultDatas() {
	// index = getIntent().getIntExtra(ScanFragment.SELECTDEVICE, -1);
	//
	// }

	// 连接
	private ProgressDialog pd;

	private void startConnect() {
		 pd = ProgressDialog.show(MTSeriTalkActivity.this, "Wait!", "正在连接",
		 true, true, new OnCancelListener() {
		
		 @Override
		 public void onCancel(DialogInterface dialog) {
		 mBle.disConnect();
		 }
		 });

		try {
			ErroCode result = mBle.connect(device.getDevice().getAddress(), 1,
					false, callback);
			if (result.getCode() != 0) { // 连接失败
				return;
			} else {
				talkfragment.setConnectStatues(true);
			}
		} catch (Exception e) {

		}
	}

	// 初始化fragment
	private TalkFragment talkfragment;
	private int workingmodel = 0;
	private FragmentManager fm;

	private void initFragment() {
		fm = getSupportFragmentManager();
		talkfragment = new TalkFragment(getApplicationContext()) {

			@Override
			protected void onSendMsg(Msg msg) {
				super.onSendMsg(msg);

				if (!mBle.isConnected()) {
					System.out.println("!mBle.isConnected()");
					return;
				}
				workingmodel = 0;
				if (workingmodel == 0) {
					mBle.addWriteDatasWithAckAction(new WriteCharactWithAckAction(
							null, null, msg.getRealmsg(), new Option(1000)) {
						@Override
						public void onReciveDatas(
								BluetoothGattCharacteristic characteristic,
								byte[] datas) {
							super.onReciveDatas(characteristic, datas);
							setStatues(ActionStatues.DONE);
							String ackcmd = new String(datas);
							Msg msg = new Msg(characteristic, DIR.RECIVE,
									datas, ackcmd);
							talkfragment.receiveSelfdata(msg);
						}

						@Override
						public void onFail(ErroCode erro) {
							super.onFail(erro);
						}
					});
				}
			}
		};
		talkfragment.setProperty(true, true);
		talkfragment.setConnectStatues(mBle.isConnected());
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.page_continer, talkfragment);
		ft.commit();
	}

	// 连接回调
	private MTSeriaBase.CallBack callback = new MTSeriaBase.CallBack() {

		@Override
		public void onConnect(ErroCode errocode) {
			System.out.println("onBaseConnect->" + errocode.toString());
			 if ((pd != null) && (pd.isShowing())) {
			 pd.dismiss();
			 }

			if (errocode.getCode() == 0) {
				return;
			}

			// 连接失败的情况
			Toast.makeText(MTSeriTalkActivity.this,
					"连接失败->" + errocode.getMsg(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void reTryConnect(int lasttimes) {
			if ((pd != null) && (pd.isShowing())) {
				pd.setTitle("正在重连->" + lasttimes);
			}
		}

		@Override
		public void onDisConnect(ErroCode errocode) {
			Toast.makeText(getApplicationContext(), "断开连接", Toast.LENGTH_SHORT)
					.show();
			if (talkfragment != null) {
				talkfragment.setConnectStatues(mBle.isConnected());
			}
		}

		@Override
		public void onManualDisConnect(ErroCode errocode) {
		}

		@Override
		public void onDatasRecive(BluetoothGattCharacteristic rxd_charact,
				byte[] datas) {
			if (workingmodel == 0) {
				TalkFragment.Msg msg = new TalkFragment.Msg(rxd_charact,
						DIR.RECIVE, datas, null);
				talkfragment.receiveSelfdata(msg);
			}
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mBle.disConnect();
	}

	public void SearchDevice(View v) {
		Intent intent = new Intent();
		intent.setClass(this, TestDeviceList.class);
		startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
	}

}
