package com.bean;

public class DataPlan {
	private String packageCode;
	private String packageName;
	
	public DataPlan(String packageCode, String packageName) {
		this.packageCode = packageCode;
		this.packageName = packageName;
	}
	
	public String getPackageCode() {
		return packageCode;
	}
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
}
