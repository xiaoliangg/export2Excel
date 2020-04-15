package com.bean;

public class DingWeiMonth {

	private String imsi1;
	private String month;
	private String totalUsage;
	private String totalCost;
	public String getTotalUsage() {
		return totalUsage;
	}
	public void setTotalUsage(String totalUsage) {
		this.totalUsage = totalUsage;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}
	public String getImsi1() {
		return imsi1;
	}
	public void setImsi1(String imsi1) {
		this.imsi1 = imsi1;
	}
	@Override
	public String toString() {
		return "DingWeiMonth [imsi1=" + imsi1 + ", month=" + month + ", totalUsage=" + totalUsage + ", totalCost="
				+ totalCost + "]";
	}
}
