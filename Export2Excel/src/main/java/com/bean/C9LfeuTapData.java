package com.bean;

import java.sql.Timestamp;

public class C9LfeuTapData {
	private String operator;
	private String profileTadig;
	private String billingId;
	private String type;
	private String teleService;
	private String callTime;
	private String callDuration;
	private String calledOrCallingNumber;
	private String dialledDigits;
	private String destinatioNumber;
	private String imsi;
	private String msisdn;
	private String imei;
	private String chargableUnits;
	private String chargedUnits;
	private String charge;	
	
	private Timestamp recordTime;
	private String cdrFileName;
	private String srcTapData;
	
	private Long id;

	
	
	//新增	
	private String perMbCost;	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getProfileTadig() {
		return profileTadig;
	}
	public void setProfileTadig(String profileTadig) {
		this.profileTadig = profileTadig;
	}
	public String getBillingId() {
		return billingId;
	}
	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTeleService() {
		return teleService;
	}
	public void setTeleService(String teleService) {
		this.teleService = teleService;
	}
	public String getCallTime() {
		return callTime;
	}
	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}
	public String getCallDuration() {
		return callDuration;
	}
	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}
	public String getCalledOrCallingNumber() {
		return calledOrCallingNumber;
	}
	public void setCalledOrCallingNumber(String calledOrCallingNumber) {
		this.calledOrCallingNumber = calledOrCallingNumber;
	}
	public String getDialledDigits() {
		return dialledDigits;
	}
	public void setDialledDigits(String dialledDigits) {
		this.dialledDigits = dialledDigits;
	}
	public String getDestinatioNumber() {
		return destinatioNumber;
	}
	public void setDestinatioNumber(String destinatioNumber) {
		this.destinatioNumber = destinatioNumber;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getChargableUnits() {
		return chargableUnits;
	}
	public String getPerMbCost() {
		return perMbCost;
	}
	public void setPerMbCost(String perMbCost) {
		this.perMbCost = perMbCost;
	}
	public void setChargableUnits(String chargableUnits) {
		this.chargableUnits = chargableUnits;
	}
	public String getChargedUnits() {
		return chargedUnits;
	}
	public void setChargedUnits(String chargedUnits) {
		this.chargedUnits = chargedUnits;
	}
	public String getCharge() {
		return charge;
	}
	public void setCharge(String charge) {
		this.charge = charge;
	}
	public Timestamp getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Timestamp recordTime) {
		this.recordTime = recordTime;
	}
	public String getCdrFileName() {
		return cdrFileName;
	}
	public void setCdrFileName(String cdrFileName) {
		this.cdrFileName = cdrFileName;
	}
	public String getSrcTapData() {
		return srcTapData;
	}
	public void setSrcTapData(String srcTapData) {
		this.srcTapData = srcTapData;
	}
	
}
