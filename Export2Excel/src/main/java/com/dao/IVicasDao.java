package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bean.VicasActiveNumber;
import com.bean.VicasSum;

/**
 * 厂商密钥表dao接口
 * 
 * @author Administrator
 * 
 */
public interface IVicasDao {

	/**
	 * 获取当前时间段的总用量及首次激活时间
	 * @param imsis 
	 * @param endTime 
	 * @param startTime 
	 * @param keyType
	 * @return
	 */
	public List<VicasSum> getSumAndFirstUseTime(@Param("startTime")String startTime, @Param("endTime")String endTime, @Param("imsis")String imsis);
	/**
	 * 查询本月激活的码号数量
	 * @param imsis 
	 * @param endTime 
	 * @param startTime 
	 * @param keyType
	 * @return
	 */
	public List<VicasActiveNumber> getActiveNumbers(@Param("startTime")String startTime, @Param("endTime")String endTime, @Param("imsis")String imsis);

}
