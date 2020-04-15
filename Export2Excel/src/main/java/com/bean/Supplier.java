package com.bean;

public class Supplier {

	public Supplier(String supplierCode,String supplierName) {
		this.supplierCode = supplierCode;
		this.supplierName = supplierName;
	}
	private String supplierCode;
	private String supplierName;
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
}
