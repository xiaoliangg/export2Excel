package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bean.DaysOrdersNumber;
import com.bean.PackageUseDetails;
import com.bean.ResolvePackageOrderDays;

/**
 * 厂商密钥表dao接口
 * 
 * @author Administrator
 * 
 */
public interface IResolvePackageOrderDaysDao {

	/**
	 * 各套餐的订单数量，平均使用流量，总使用流量
	 * @param keyType
	 * @return
	 */
	public List<PackageUseDetails> getPackageUseDetails(@Param("channel") String channel);
	/**
	 * 查询实际使用天数的订单数量
	 * @param keyType
	 * @return
	 */
	public List<DaysOrdersNumber> getDaysOrdersNumber(@Param("packageName") String packageName,@Param("channel") String channel);
	/**
	 * 查询未使用天数的订单数量
	 * @param keyType
	 * @return
	 */
	public List<ResolvePackageOrderDays> getUnusedOrdersNumber(@Param("packageName") String packageName,@Param("channel") String channel);

}
