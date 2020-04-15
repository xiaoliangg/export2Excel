package com.dao;

import java.util.List;

import com.bean.ManufacturerKey;

/**
 * 厂商密钥表dao接口
 * 
 * @author Administrator
 * 
 */
public interface IManufacturerKeyDao {

	/**
	 * 根据密钥类型查询密钥信息
	 * 
	 * @param keyType
	 * @return
	 */
	public List<ManufacturerKey> getKeyByType(String keyType);

}
