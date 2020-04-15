package com.bean;

import javax.persistence.*;

@Table(name = "resolve_package_order_days")
public class ResolvePackageOrderDays {
    /**
     * 记录ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 资费套餐名称（中文）
     */
    @Column(name = "package_name")
    private String packageName;

    /**
     * 订单编码
     */
    @Column(name = "order_code")
    private String orderCode;

    /**
     * 使用天数
     */
    private String days;

    /**
     * 流量，单位字节
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
     * 获取使用天数
     *
     * @return days - 使用天数
     */
    public String getDays() {
        return days;
    }

    /**
     * 设置使用天数
     *
     * @param days 使用天数
     */
    public void setDays(String days) {
        this.days = days == null ? null : days.trim();
    }

    /**
     * 获取流量，单位字节
     *
     * @return cdr_byte - 流量，单位字节
     */
    public String getCdrByte() {
        return cdrByte;
    }

    /**
     * 设置流量，单位字节
     *
     * @param cdrByte 流量，单位字节
     */
    public void setCdrByte(String cdrByte) {
        this.cdrByte = cdrByte == null ? null : cdrByte.trim();
    }
}