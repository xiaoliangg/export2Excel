package com.bean;

public class Operator {
	private String operatorCode;
	private String operatorName;
	
	public Operator(String operatorCode, String operatorName) {
		this.operatorCode = operatorCode;
		this.operatorName = operatorName;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	
}
