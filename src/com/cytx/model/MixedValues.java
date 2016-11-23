package com.cytx.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "mixedvalues")
public class MixedValues {
	@Column(name = "id", isId = true)
	private int id;
	
	@Column(name = "usermodel")
	private int usermodel = SMARTUSER;
	
	@Column(name = "pwd")
	private String pwd = "123456";
	
	@Column(name = "wxappid")
	private String wxappid = "123456";
	
	@Column(name = "wxappsecret")
	private String wxappsecret = "123456";
	
	@Column(name = "productid")
	private String productid = "1234";
	
	private String wxtoken = null;  // 微信token
	private int wxtokentimeout = 0;  // token过期时间
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUsermodel() {
		return usermodel;
	}

	public void setUsermodel(int usermodel) {
		this.usermodel = usermodel;
	}
	
	public String getWxappid() {
		return wxappid;
	}

	public void setWxappid(String wxappid) {
		this.wxappid = wxappid;
	}

	public String getWxappsecret() {
		return wxappsecret;
	}

	public void setWxappsecret(String wxappsecret) {
		this.wxappsecret = wxappsecret;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getWxtoken() {
		return wxtoken;
	}

	public void setWxtoken(String wxtoken) {
		this.wxtoken = wxtoken;
	}

	public int getWxtokentimeout() {
		return wxtokentimeout;
	}

	public void setWxtokentimeout(int wxtokentimeout) {
		this.wxtokentimeout = wxtokentimeout;
	}

	public static final int SMARTUSER = 0;
	public static final int MTBEACONUSER = 1;
	public static final int MTSERIUSER = 2;
	public static final int MTWXUSER = 3;
	public static final int DEVELOPUSER = 4;
}
