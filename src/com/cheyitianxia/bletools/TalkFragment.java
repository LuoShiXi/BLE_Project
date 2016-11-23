package com.cheyitianxia.bletools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.activity.location.AssistantLocation;
import com.carCloth.shop.CarClothShop;
import com.carCloth.onoff.CarClothOff;
import com.carCloth.onoff.CarClothOn;
import com.cheyitianxia.bletools.MyScrollView;
import com.cytx.view.TalkMsgView;
import com.mt.mtbletools.R;
import com.mt.sdk.tools.MTTools;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class TalkFragment extends Fragment implements OnGestureListener {

	private ProgressDialog pDialog;

	// 发送指令信息
	private static String mSendMessage = "5a0000a5";

	// 车衣指令反馈信息接收
	private static String mBackMessage;

	// 接收OBD信息
	private static String carMess;
	private static String mcarMess;

	// 主页车辆主要信息显示（转速、油量、油耗、电量）
	private TextView tvSpeed;
	private TextView tvOilmass;
	private TextView tvOilwear;
	private TextView tvElect;
	//
	private static String mSpeed;
	private static String mOilmass;
	private static String mOilwear;
	private static String mElect;

	// 车衣操作跳转按钮
	private Button mCarCloth_ON;
	private Button mCarCloth_OFF;
	private Button mBuy;
	private Button mLocation;

	private boolean connectStatues = true;
	private Context context;

	public TalkFragment() {
		mSendMessage = "5a0000a5";
		mcarMess = "$OBD+RTD=0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
		mSpeed = "";
		mOilmass = "";
		mOilwear = "";
		mElect = "";
	}

	public void setSpeed(String speed) {
		mSpeed = speed;
	}

	public String getSpeed() {
		return mSpeed;
	}

	public void setOilmass(String Oilmass) {
		mOilmass = Oilmass;
	}

	public String getOilmass() {
		return mOilmass;
	}

	public void setOilwear(String Oilwear) {
		mOilwear = Oilwear;
	}

	public String getOilwear() {
		return mOilwear;
	}

	public void setElect(String elect) {
		mElect = elect;
	}

	public String getElect() {
		return mElect;
	}

	// 发送指令信息设定
	public void setSendMessage(String mess) {
		mSendMessage = mess;
	}

	public String getSendMessage() {
		return mSendMessage;
	}

	// 获取OBD信息
	public void setObdMessage(String mess) {
		mcarMess = mess;
	}

	public String getObdMessage() {
		return mcarMess;
	}

	// 得到指令回调信息
	public void setBackMessage(String mess) {
		mBackMessage = mess;
	}

	public String getBackMessage() {
		return mBackMessage;
	}

	public TalkFragment(Context context) {
		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new CreateNewProduct().execute();
		// 实时刷新车速等信息
		new Thread(mRunnable).start();

		thread.start();
	}

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view != null) {
			return view;
		}
		view = inflater.inflate(R.layout.fragment_talk, container, false);
		initView();
		return view;
	}

	// 初始化控件
	private List<TalkFragment.Msg> chats = new ArrayList<TalkFragment.Msg>();
	private TextView conect_flag_btn;

	// //////////////////////////
	// //////////////////////////
	private ImageButton btn_led;

	// 图片切换
	private TextView date_TextView;
	private ViewFlipper viewFlipper;
	private boolean showNext = true;
	private boolean isRun = true;
	private int currentPage = 0;
	private final int SHOW_NEXT = 0011;
	private static final int FLING_MIN_DISTANCE = 50;
	private static final int FLING_MIN_VELOCITY = 0;
	private GestureDetector mGestureDetector;

	@SuppressWarnings("deprecation")
	private void initView() {
		tvSpeed = (TextView) view.findViewById(R.id.tv_speed);
		tvOilmass = (TextView) view.findViewById(R.id.tv_oilmass);
		tvOilwear = (TextView) view.findViewById(R.id.tv_oilwear);
		tvElect = (TextView) view.findViewById(R.id.tv_elect);
		mLocation = (Button) view.findViewById(R.id.btn_daohang);
		btn_led = (ImageButton) view.findViewById(R.id.image_led);
		mCarCloth_ON = (Button) view.findViewById(R.id.btn_carClothOn);
		mCarCloth_OFF = (Button) view.findViewById(R.id.btn_carClothOff);
		mBuy = (Button) view.findViewById(R.id.btn_buy);
		conect_flag_btn = (TextView) view.findViewById(R.id.conect_flag_btn);
		btn_led.setOnClickListener(myOnClickListener);
		mCarCloth_ON.setOnClickListener(myOnClickListener);
		mBuy.setOnClickListener(myOnClickListener);
		mCarCloth_OFF.setOnClickListener(myOnClickListener);
		mLocation.setOnClickListener(myOnClickListener);

		date_TextView = (TextView) view.findViewById(R.id.home_date_tv);
		date_TextView.setText(getDate());
		viewFlipper = (ViewFlipper) view.findViewById(R.id.mViewFlipper_vf);
		mGestureDetector = new GestureDetector(this);
		viewFlipper.setLongClickable(false);
		viewFlipper.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (viewFlipper.getDisplayedChild()) {
				case 0:
					Log.d("one->", "00000000000");
					break;
				case 1:
					Log.d("two->", "11111111111");
					break;
				case 2:
					Log.d("three->", "2222222222");
					break;
				case 3:
					Log.d("four->", "3333333333");
					break;
				default:
					break;
				}
			}
		});
		displayRatio_selelct(currentPage);

		MyScrollView myScrollView = (MyScrollView) view
				.findViewById(R.id.viewflipper_scrollview);
		myScrollView.setGestureDetector(mGestureDetector);

		reflashAllDatas();
		setProperty(writeable, readable);
		setConnectStatues(connectStatues);
	}

	// 按钮监听
	private OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == btn_led) {
				setSendMessage("5a0318a5");
				setSendDatas();
				mSendMessage = "5a0000a5";
				return;
			}
			if (v == mBuy) {
				Intent intent_shop = new Intent();
				intent_shop.setClass(getActivity(), CarClothShop.class);
				startActivity(intent_shop);
				return;
			}
			if (v == mCarCloth_ON) {
				Intent intent_clothON = new Intent();
				intent_clothON.setClass(getActivity(), CarClothOn.class);
				startActivity(intent_clothON);
				return;
			}
			if (v == mCarCloth_OFF) {
				Intent intent_clothOFF = new Intent();
				intent_clothOFF.setClass(getActivity(), CarClothOff.class);
				startActivity(intent_clothOFF);
				return;
			}
			if (v == mLocation) {
				Intent intent_location = new Intent();
				intent_location
						.setClass(getActivity(), AssistantLocation.class);
				startActivity(intent_location);
				return;
			}

		}
	};

	public void setSendDatas() {

		Msg msg = getSendMsg();
		chats.add(msg);
		onSendMsg(msg);
		chatlistAdapter.notifyDataSetChanged();
	}

	// 发送数据回调
	protected void onSendMsg(Msg msg) {
	}

	// 读取数据回调
	protected void onRead() {
	}

	// 设置权限
	private boolean writeable = false;
	private boolean readable = false;

	public void setProperty(boolean writeable, boolean readable) {
		this.writeable = writeable;
		this.readable = readable;
	}

	// 设置连接状态
	public void setConnectStatues(boolean statues) {
		this.connectStatues = statues;
		if (conect_flag_btn == null) {
			return;
		}
		if (connectStatues) {
			conect_flag_btn.setText(context.getResources().getString(
					R.string.connected));
		} else {
			conect_flag_btn.setText(context.getResources().getString(
					R.string.disconnect));
		}
	}

	// 刷新全部控件状态
	private void reflashAllDatas() {
		// 显示控制状态
		setConnectStatues(connectStatues);
	}

	// 获取要发送的数据
	private Msg getSendMsg() {
		Msg msg = new Msg();
		msg.setDir(Msg.DIR.SEND);
		// 16进制状态
		String tmp_str = "";
		byte[] tmp_byte = null;
		byte[] write_msg_byte = null;

		// ////////////////////////
		// 获取要传送的指令
		tmp_str = getSendMessage();
		System.out.println(getSendMessage() + "?");

		if (0 == tmp_str.length())
			return null;
		tmp_byte = tmp_str.getBytes();
		write_msg_byte = new byte[tmp_byte.length / 2 + tmp_byte.length % 2];
		for (int i = 0; i < tmp_byte.length; i++) {
			if ((tmp_byte[i] <= '9') && (tmp_byte[i] >= '0')) {
				if (0 == i % 2)
					write_msg_byte[i / 2] = (byte) (((tmp_byte[i] - '0') * 16) & 0xFF);
				else
					write_msg_byte[i / 2] |= (byte) ((tmp_byte[i] - '0') & 0xFF);
			} else {
				if (0 == i % 2)
					write_msg_byte[i / 2] = (byte) (((tmp_byte[i] - 'a' + 10) * 16) & 0xFF);
				else
					write_msg_byte[i / 2] |= (byte) ((tmp_byte[i] - 'a' + 10) & 0xFF);
			}
		}
		msg.setRealmsg(write_msg_byte);
		// msg.setDismsg(senddatas_16_edit.getText().toString());
		return msg;
	}

	// 列表适配器
	private BaseAdapter chatlistAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new TalkMsgView(getContext());
			}
			((TalkMsgView) convertView).setDatas(getItem(position));
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public TalkFragment.Msg getItem(int position) {
			return chats.get(position);
		}

		@Override
		public int getCount() {
			return chats.size();
		}
	};

	// 接收自身数据
	public void receiveSelfdata(Msg msg) {

		msg.setDismsg(MTTools.convertBytearrayToString(msg.getRealmsg()));
		System.out.println("$$$$$$$$$$$$$$$$$$$"
				+ MTTools.convertBytearrayToString(msg.getRealmsg()));
		chats.add(msg);
		handl.post(new Runnable() {

			@Override
			public void run() {
				chatlistAdapter.notifyDataSetChanged();
			}
		});
	}

	// 信息包
	public static class Msg {
		private byte[] realmsg; // 实际数据
		private String dismsg; // 显示数据
		private BluetoothGattCharacteristic charact; // 对应特征值
		private DIR dir = DIR.SEND;

		public Msg() {
		}

		public Msg(BluetoothGattCharacteristic charact, DIR dir,
				byte[] realmsg, String dismsg) {
			this.charact = charact;
			this.dir = dir;
			this.realmsg = realmsg;
			this.dismsg = dismsg;
		}

		public byte[] getRealmsg() {
			return realmsg;
		}

		public void setRealmsg(byte[] realmsg) {
			this.realmsg = realmsg;
		}

		public String getDismsg() {
			return dismsg;
		}

		public void setDismsg(String dismsg) {
			this.dismsg = dismsg;
		}

		public BluetoothGattCharacteristic getCharact() {
			return charact;
		}

		public void setCharact(BluetoothGattCharacteristic charact) {
			this.charact = charact;
		}

		public DIR getDir() {
			return dir;
		}

		public void setDir(DIR dir) {
			this.dir = dir;
		}

		// 发送方向
		public enum DIR {
			SEND, RECIVE
		}
	}

	// 发送数据
	@SuppressLint("HandlerLeak")
	private Handler handl = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			Msg sendmsg = getSendMsg();
			onSendMsg(sendmsg);
		}
	};

	/* 车辆信息显示代码 */
	Runnable mRunnable = new Runnable() {

		public void run() {
			while (true) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				nHandler.sendMessage(nHandler.obtainMessage());

			}

		}
	};
	@SuppressLint("HandlerLeak")
	Handler nHandler = new Handler() {

		public void handleMessage(Message msg) {

			// //////////////////////////
			// 待更正
			// 读取OBD信息命令的发送
			// setSendMessage("5axxxxa5");
			// setSendDatas();
			/* carMess = getObdMessage(); */
			carMess = "newMess";// Msg.getDismsg().toString();
			setTextMess(); // 更新油量等信息

		}
	};

	private void setTextMess() {
		if (!carMess.equals("")) {
			setObdMessage(carMess);
		}
		if (carMess.trim() == "") {
		}
		if (carMess.equals("")) {
		} else if (carMess.regionMatches(0, "$OBD+RTD=", 0, 9)) {

			String receiveSubString = carMess.substring(9);

			String strArray[] = receiveSubString.split(",");
			setSpeed(strArray[0]);
			setOilmass(strArray[18]);
			setOilwear(strArray[16]);
			setElect(strArray[2]);
			tvSpeed.setText(getSpeed() + " " + "km/h");
			tvOilmass.setText(getOilmass() + " " + "%");
			tvOilwear.setText(getOilwear() + " " + "L/KM");
			tvElect.setText(getElect() + " " + "V");
			receiveSubString = null;
			carMess = null;
		}
	}

	/* 界面设计代码 */

	Thread thread = new Thread() {

		@Override
		public void run() {
			// 图片展示run
			while (isRun) {
				try {
					Thread.sleep(1000 * 5);
					Message msg = new Message();
					msg.what = SHOW_NEXT;
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_NEXT:
				if (showNext) {
					showNextView();
				} else {
					showPreviousView();
				}
				break;
			}
		}
	};

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		this.viewFlipper.setClickable(true);
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		Log.e("view", "onFling");
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			Log.e("fling", "left");
			showNextView();
			showNext = true;
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			Log.e("fling", "right");
			showPreviousView();
			showNext = false;
		}
		this.viewFlipper.setClickable(false);
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}

	// 获取当前日期
	private String getDate() {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		int w = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}

		String mDate = c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1)
				+ "月" + c.get(Calendar.DATE) + "日  " + weekDays[w];
		return mDate;
	}

	// 图片切换选择
	private void displayRatio_selelct(int id) {
		int[] ratioId = { R.id.home_ratio_img_04, R.id.home_ratio_img_03,
				R.id.home_ratio_img_02, R.id.home_ratio_img_01 };
		ImageView img = (ImageView) view.findViewById(ratioId[id]);
		img.setSelected(true);
	}

	private void displayRatio_normal(int id) {
		int[] ratioId = { R.id.home_ratio_img_04, R.id.home_ratio_img_03,
				R.id.home_ratio_img_02, R.id.home_ratio_img_01 };
		ImageView img = (ImageView) view.findViewById(ratioId[id]);
		img.setSelected(false);
	}

	private void showNextView() {

		try {
			viewFlipper.setInAnimation(AnimationUtils
					.loadAnimation(this.getActivity().getApplicationContext(),
							R.anim.push_left_in));
			viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this
					.getActivity().getApplicationContext(),
					R.anim.push_left_out));
			viewFlipper.showNext();
			currentPage++;
			if (currentPage == viewFlipper.getChildCount()) {
				displayRatio_normal(currentPage - 1);
				currentPage = 0;
				displayRatio_selelct(currentPage);
			} else {
				displayRatio_selelct(currentPage);
				displayRatio_normal(currentPage - 1);
			}
			Log.e("currentPage", currentPage + "");
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("On a null object", "ERROR! On a null object !!!");
		}

	}

	private void showPreviousView() {
		displayRatio_selelct(currentPage);
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this
				.getActivity().getApplicationContext(), R.anim.push_right_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this
				.getActivity().getApplicationContext(), R.anim.push_right_out));
		viewFlipper.showPrevious();
		currentPage--;
		if (currentPage == -1) {
			displayRatio_normal(currentPage + 1);
			currentPage = viewFlipper.getChildCount() - 1;
			displayRatio_selelct(currentPage);
		} else {
			displayRatio_selelct(currentPage);
			displayRatio_normal(currentPage + 1);
		}
		Log.e("currentPage", currentPage + "");
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Background Async Task to refresh datas
	 * */
	class CreateNewProduct extends AsyncTask<String, String, String> {
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		public void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("。。。");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(true);
			pDialog.hide();

		}

		/**
		 * Creating product
		 */
		public String doInBackground(String... args) {
			try {
				do {
					Thread.sleep(1000);

					if (!getSendMessage().equals("5a0000a5")) {
						setSendDatas();
						mSendMessage = "5a0000a5";
					}
				} while (true);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 **/
		public void onPostExecute(String file_url) {
			pDialog.dismiss();
		}
	}
}
