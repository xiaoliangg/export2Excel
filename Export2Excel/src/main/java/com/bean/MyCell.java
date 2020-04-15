package com.bean;

import org.apache.poi.ss.usermodel.CellStyle;

public class MyCell {

	private String value;
	private int type;
	private CellStyle cellStyle;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public CellStyle getCellStyle() {
		return cellStyle;
	}
	public void setCellStyle(CellStyle cellStyle) {
		this.cellStyle = cellStyle;
	}
}
