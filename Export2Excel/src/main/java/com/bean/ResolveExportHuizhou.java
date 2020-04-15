package com.bean;

import javax.persistence.*;

@Table(name = "resolve_export_huizhou")
public class ResolveExportHuizhou {
    /**
     * 记录ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 归属的合作伙伴名称
     */
    @Column(name = "partner_name")
    private String partnerName;

    /**
     * 主号ICCID
     */
    private String iccid;

    /**
     * 订单编码
     */
    @Column(name = "order_code")
    private String orderCode;

    /**
     * 资费套餐名称（中文）
     */
    @Column(name = "package_name")
    private String packageName;

    /**
     * 订单实际开始时间
     */
    @Column(name = "actual_start_time")
    private String actualStartTime;

    /**
     * 订单实际结束时间
     */
    @Column(name = "actual_end_time")
    private String actualEndTime;

    /**
     * 副号imsi
     */
    @Column(name = "local_imsi")
    private String localImsi;

    /**
     * 副号imsi
     */
    @Column(name = "supplier_code")
    private String supplierCode;

    /**
     * 副号imsi
     */
    @Column(name = "supplier_name")
    private String supplierName;

    /**
     * cdr时间，精确到天
     */
    @Column(name = "cdr_time")
    private String cdrTime;

    /**
     * 每天流量，单位字节
     */
    @Column(name = "cdr_byte")
    private String cdrByte;

    /**
     * 获取记录ID
     *
     * @return id - 记录ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置记录ID
     *
     * @param id 记录ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取归属的合作伙伴名称
     *
     * @return partner_name - 归属的合作伙伴名称
     */
    public String getPartnerName() {
        return partnerName;
    }

    /**
     * 设置归属的合作伙伴名称
     *
     * @param partnerName 归属的合作伙伴名称
     */
    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName == null ? null : partnerName.trim();
    }

    /**
     * 获取主号ICCID
     *
     * @return iccid - 主号ICCID
     */
    public String getIccid() {
        return iccid;
    }

    /**
     * 设置主号ICCID
     *
     * @param iccid 主号ICCID
     */
    public void setIccid(String iccid) {
        this.iccid = iccid == null ? null : iccid.trim();
    }

    /**
     * 获取订单编码
     *
     * @return order_code - 订单编码
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * 设置订单编码
     *
     * @param orderCode 订单编码
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode == null ? null : orderCode.trim();
    }

    /**
     * 获取资费套餐名称（中文）
     *
     * @return package_name - 资费套餐名称（中文）
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * 设置资费套餐名称（中文）
     *
     * @param packageName 资费套餐名称（中文）
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName == null ? null : packageName.trim();
    }

    /**
     * 获取订单实际开始时间
     *
     * @return actual_start_time - 订单实际开始时间
     */
    public String getActualStartTime() {
        return actualStartTime;
    }

    /**
     * 设置订单实际开始时间
     *
     * @param actualStartTime 订单实际开始时间
     */
    public void setActualStartTime(String actualStartTime) {
        this.actualStartTime = actualStartTime == null ? null : actualStartTime.trim();
    }

    /**
     * 获取订单实际结束时间
     *
     * @return actual_end_time - 订单实际结束时间
     */
    public String getActualEndTime() {
        return actualEndTime;
    }

    /**
     * 设置订单实际结束时间
     *
     * @param actualEndTime 订单实际结束时间
     */
    public void setActualEndTime(String actualEndTime) {
        this.actualEndTime = actualEndTime == null ? null : actualEndTime.trim();
    }

    /**
     * 获取副号imsi
     *
     * @return local_imsi - 副号imsi
     */
    public String getLocalImsi() {
        return localImsi;
    }

    /**
     * 设置副号imsi
     *
     * @param localImsi 副号imsi
     */
    public void setLocalImsi(String localImsi) {
        this.localImsi = localImsi == null ? null : localImsi.trim();
    }

    /**
     * 获取副号imsi
     *
     * @return supplier_code - 副号imsi
     */
    public String getSupplierCode() {
        return supplierCode;
    }

    /**
     * 设置副号imsi
     *
     * @param supplierCode 副号imsi
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode == null ? null : supplierCode.trim();
    }

    /**
     * 获取副号imsi
     *
     * @return supplier_name - 副号imsi
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * 设置副号imsi
     *
     * @param supplierName 副号imsi
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }

    /**
     * 获取cdr时间，精确到天
     *
     * @return cdr_time - cdr时间，精确到天
     */
    public String getCdrTime() {
        return cdrTime;
    }

    /**
     * 设置cdr时间，精确到天
     *
     * @param cdrTime cdr时间，精确到天
     */
    public void setCdrTime(String cdrTime) {
        this.cdrTime = cdrTime == null ? null : cdrTime.trim();
    }

    /**
     * 获取每天流量，单位字节
     *
     * @return cdr_byte - 每天流量，单位字节
     */
    public String getCdrByte() {
        return cdrByte;
    }

    /**
     * 设置每天流量，单位字节
     *
     * @param cdrByte 每天流量，单位字节
     */
    public void setCdrByte(String cdrByte) {
        this.cdrByte = cdrByte == null ? null : cdrByte.trim();
    }
}