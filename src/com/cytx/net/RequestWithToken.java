package com.cytx.net;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cheyitianxia.help.GlobalVariable;
import com.mt.sdk.ble.model.ErroCode;

import android.content.Context;

public class RequestWithToken {
	private static RequestWithToken mRequestWithToken;

	public static RequestWithToken getInstance(Context context) {
		if (mRequestWithToken == null) {
			mRequestWithToken = new RequestWithToken(context);
		}

		return mRequestWithToken;
	}

	private GlobalVariable mGlobalVariable;

	private RequestQueue mVolleyQueue;

	private RequestWithToken(Context context) {
		mVolleyQueue = Volley.newRequestQueue(context);
		mGlobalVariable = GlobalVariable.getinstance();
	}
	
	// 添加动作
	public boolean addRequest(Request request){
		mVolleyQueue.add(request);
		return true;
	}
	
	// https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxce193849ddc1d181&secret=a53329f9f86d4b347b65d8358eb3739a
	public boolean gettoken(final GetTokenCallback callback) {
		if (callback == null) {
			return false;
		}
		if ((mGlobalVariable.getMixedvalues().getWxtoken() == null)
				|| (System.currentTimeMillis() / 1000 > mGlobalVariable.getMixedvalues().getWxtokentimeout())) {
			StringRequest loginrequest = new StringRequest(Request.Method.POST, HTTPURL.WXTOKEN_URL,
					new Response.Listener<String>() {
						@Override
						public void onResponse(final String str) {
							System.out.println("gettoken->"+str);
							try {
								JSONObject jsobj = new JSONObject(str);
								String token = jsobj.getString("access_token"); // 获取token值
								int tokenouttime = (int) (System.currentTimeMillis() / 1000
										+ jsobj.getInt("expires_in") * 3 / 4); // 获取过期时间
								
								mGlobalVariable.getMixedvalues().setWxtoken(token);
								mGlobalVariable.getMixedvalues().setWxtokentimeout(tokenouttime);

								callback.onGetOK(token, tokenouttime);
							} catch (JSONException e) {
								e.printStackTrace();
								callback.onGetFail(ErroCode.ERROMAP.get("appiderro"));
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(final VolleyError error) {
							callback.onGetFail(ErroCode.ERROMAP.get("interneterro"));
						}
					}) {
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					System.out.println("getParams");
					Map<String, String> usermap = new HashMap<String, String>();
					usermap.put("grant_type", "client_credential");
					usermap.put("appid", mGlobalVariable.getMixedvalues().getWxappid());
					usermap.put("secret", mGlobalVariable.getMixedvalues().getWxappsecret());

					JSONObject jsobj = new JSONObject(usermap);
					System.out.println("gettoken send->"+jsobj);
					
					return usermap;
				}
			};
			mVolleyQueue.add(loginrequest);
		} else {
			callback.onGetOK(mGlobalVariable.getMixedvalues().getWxtoken(),
					mGlobalVariable.getMixedvalues().getWxtokentimeout());
		}
		return true;
	}

	public static interface GetTokenCallback {
		public void onGetOK(String token, int timeout);

		public void onGetFail(ErroCode erro);
	}
}
