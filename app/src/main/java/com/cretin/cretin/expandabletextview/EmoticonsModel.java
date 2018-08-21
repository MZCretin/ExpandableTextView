package com.cretin.cretin.expandabletextview;

public class EmoticonsModel {

	public static final int DEFAULT_TYPE = 0; //PNG


	private int type;
	private String name;   //存储传输中的名字 一般为 [em_11]
	private int imageRes;
	private String imageRemoteUrl;

	public EmoticonsModel(String name, int imageRes) {
		super();
		this.type = DEFAULT_TYPE;
		this.name = name;
		this.imageRes = imageRes;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getImageRes() {
		return imageRes;
	}

	public void setImageRes(int imageRes) {
		this.imageRes = imageRes;
	}
}
