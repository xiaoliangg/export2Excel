package com.bean;

import java.util.ArrayList;
import java.util.List;

public class DingWeiExport {

	private String imsi1;//234打头，对应tap文件里的imsi
	private String imsi2;//222打头,对应物联网卡信息表里的imsi
	private String firstCdr;
	private String lastCdr;
	private String totalUsage;
	private String totalCost;
	private List<DingWeiMonth> monthsUsage = new ArrayList<DingWeiMonth>();
	public String getImsi1() {
		return imsi1;
	}
	public void setImsi1(String imsi1) {
		this.imsi1 = imsi1;
	}
	public String getImsi2() {
		return imsi2;
	}
	public void setImsi2(String imsi2) {
		this.imsi2 = imsi2;
	}
	public String getFirstCdr() {
		return firstCdr;
	}
	public void setFirstCdr(String firstCdr) {
		this.firstCdr = firstCdr;
	}
	public String getLastCdr() {
		return lastCdr;
	}
	public void setLastCdr(String lastCdr) {
		this.lastCdr = lastCdr;
	}
	public String getTotalUsage() {
		return totalUsage;
	}
	public void setTotalUsage(String totalUsage) {
		this.totalUsage = totalUsage;
	}
	public List<DingWeiMonth> getMonthsUsage() {
		return monthsUsage;
	}
	public void setMonthsUsage(List<DingWeiMonth> monthsUsage) {
		this.monthsUsage = monthsUsage;
	}
	public String getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}
	@Override
	public String toString() {
		return "DingWeiExport [imsi1=" + imsi1 + ", imsi2=" + imsi2 + ", firstCdr=" + firstCdr + ", lastCdr=" + lastCdr
				+ ", totalUsage=" + totalUsage + ", totalCost=" + totalCost + ", monthsUsage=" + monthsUsage + "]";
	}
}
