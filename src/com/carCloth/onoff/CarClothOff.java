package com.carCloth.onoff;

import java.util.Calendar;
import java.util.Date;

import com.cheyitianxia.bletools.MyScrollView;
import com.cheyitianxia.bletools.TalkFragment;
import com.mt.mtbletools.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class CarClothOff extends Activity implements OnGestureListener,
		OnTouchListener, OnClickListener {

	private TalkFragment talkfragment = new TalkFragment();
	
	// 车衣收起时的状态
	private ImageView image_off;
	private TextView tv_offState_Chang;

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


	// 主页车辆主要信息显示（转速、油量、油耗、电量）
	private TextView tvSpeed;
	private TextView tvOilmass;
	private TextView tvOilwear;
	private TextView tvElect;

	// 车衣收起操作按钮
	private Button btn_carCloth_Off;
	private Button btn_carCloth_adjust;
	private Button btn_carCloth_pause;
	private Button btn_carCloth_left;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.carclothoff);

		image_off = (ImageView) findViewById(R.id.image_change_off);
		tv_offState_Chang = (TextView) findViewById(R.id.tv_change_off);

		btn_carCloth_Off = (Button) findViewById(R.id.btn_ClothOff);
		btn_carCloth_adjust = (Button) findViewById(R.id.btn_off_adjust);
		btn_carCloth_pause = (Button) findViewById(R.id.btn_off_ClothPause);
		btn_carCloth_left = (Button) findViewById(R.id.btn_left);

		btn_carCloth_Off.setOnClickListener(this);
		btn_carCloth_adjust.setOnClickListener(this);
		btn_carCloth_pause.setOnClickListener(this);
		btn_carCloth_left.setOnClickListener(this);

		tvSpeed = (TextView) findViewById(R.id.tv_speed);
		tvOilmass = (TextView) findViewById(R.id.tv_oilmass);
		tvOilwear = (TextView) findViewById(R.id.tv_oilwear);
		tvElect = (TextView) findViewById(R.id.tv_elect);

		date_TextView = (TextView) findViewById(R.id.home_date_tv);
		date_TextView.setText(getDate());

		viewFlipper = (ViewFlipper) findViewById(R.id.mViewFlipper_vf);
		mGestureDetector = new GestureDetector(this);
		viewFlipper.setLongClickable(false);
		viewFlipper.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (viewFlipper.getDisplayedChild()) {
				case 0:
					toastInfo("00000000000");
					break;
				case 1:
					toastInfo("11111111111");
					break;
				case 2:
					toastInfo("22222222222");
					break;
				case 3:
					toastInfo("33333333333");
					break;
				default:
					break;
				}
			}
		});
		displayRatio_selelct(currentPage);

		MyScrollView myScrollView = (MyScrollView) findViewById(R.id.viewflipper_scrollview);
		myScrollView.setGestureDetector(mGestureDetector);

		new Thread(mRunnable).start();
		thread.start();

	}

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

			setText(); // 在setText方法中更新视图上
		}

	};

	// 车辆主要参数显示
	public void setText() {
//		if (mainactivity.getMess().regionMatches(0, "$OBD+RTD=", 0, 9)) {
//			String ReceiveBuffer = mainactivity.getMess() + ",null,null";
//			String receiveBufferSubString = ReceiveBuffer.substring(9);
//			String strArray[] = receiveBufferSubString.split(",");
//
//			tvSpeed.setText(strArray[0] + " " + "km/h");
//			tvOilmass.setText(strArray[18] + " " + "%");
//			tvOilwear.setText(strArray[16] + " " + "L/KM");
//			tvElect.setText(strArray[2] + " " + "V");
//			receiveBufferSubString = null;
//			ReceiveBuffer = null;
//		}
	}

	// 图片切换选择
	private void displayRatio_selelct(int id) {
		int[] ratioId = { R.id.home_ratio_img_04, R.id.home_ratio_img_03,
				R.id.home_ratio_img_02, R.id.home_ratio_img_01 };
		ImageView img = (ImageView) findViewById(ratioId[id]);
		img.setSelected(true);
	}

	private void displayRatio_normal(int id) {
		int[] ratioId = { R.id.home_ratio_img_04, R.id.home_ratio_img_03,
				R.id.home_ratio_img_02, R.id.home_ratio_img_01 };
		ImageView img = (ImageView) findViewById(ratioId[id]);
		img.setSelected(false);
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

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}

	Thread thread = new Thread() {

		@Override
		public void run() {
			// 图片展示run
			while (isRun) {
				try {
					Thread.sleep(1000 * 8);
					Message msg = new Message();
					msg.what = SHOW_NEXT;
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
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

	private void showNextView() {

		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_left_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
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

	}

	private void showPreviousView() {
		displayRatio_selelct(currentPage);
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_right_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_right_out));
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isRun = false;
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void toastInfo(String string) {
		Toast.makeText(CarClothOff.this, string, Toast.LENGTH_SHORT).show();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ClothOff:
			talkfragment.setSendMessage("5a0321a5");
//			CRC32 crc32 = new CRC32();
//			crc32.update("5".getBytes());
//			System.out.println(crc32.getValue()+"///////////////////////////");
			image_off.setImageDrawable(getResources().getDrawable(
					R.drawable.default_pic_2));
			tv_offState_Chang.setText("提示：\n       车衣正在收起！");
			new Thread(bRunnable).start();
			break;
		case R.id.btn_off_adjust:
			talkfragment.setSendMessage("5a0322a5");
			break;
		case R.id.btn_off_ClothPause:
			talkfragment.setSendMessage("5a032ba5");
			break;
		case R.id.btn_left:
			talkfragment.setSendMessage("5a0323a5");
			break;
		default:
			break;
		}
	}

	Runnable bRunnable = new Runnable() {
		public void run() {

			while (true) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}

				bHandler.sendMessage(bHandler.obtainMessage());

			}
		}
	};
	@SuppressLint("HandlerLeak")
	Handler bHandler = new Handler() {

		public void handleMessage(Message msg) {

			setTime(); // 在setText方法中更新视图上
		}

		private void setTime() {
//			String state_off = mainactivity.getMess();
			/*if (state_off.equals("5A035510A5")) {
				tv_offState_Chang.setText("提示：\n      车衣顶盖抬起！");
			}else if(state_off.equals("5A03EE10A5")){
				tv_offState_Chang.setText("警告：\n      车衣顶盖未正常抬起！！！");
			}
			if (state_off.equals("5A035510A5")) {
				tv_offState_Chang.setText("提示：\n      车衣正在展开！");
			}else if(state_off.equals("5A03EE10A5")){
				tv_offState_Chang.setText("警告：\n      车衣未正常展开！！！");
			}
			if (state_off.equals("5A035510A5")) {
				tv_offState_Chang.setText("提示：\n      车衣顶盖合拢！");
			}else if(state_off.equals("5A03EE10A5")){
				tv_offState_Chang.setText("警告：\n      车衣顶盖未正常合拢！！！");
			}
			if (state_off.equals("5A035510A5")) {
				tv_offState_Chang.setText("提示：\n      车衣已完全展开！");
			}else if(state_off.equals("5A03EE10A5")){
				tv_offState_Chang.setText("提示：\n      车衣未完全展开！！！");
			}*/
		}
	};
}
