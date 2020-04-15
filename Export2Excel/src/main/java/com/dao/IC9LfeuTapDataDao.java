package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bean.C9LfeuTapData;
import com.bean.DaysOrdersNumber;
import com.bean.DingWeiExport;
import com.bean.DingWeiMonth;
import com.bean.PackageUseDetails;
import com.bean.ResolvePackageOrderDays;

public interface IC9LfeuTapDataDao {

	/**
	 * 统计指定imsis在指定时间段的首次使用时间，最后一次使用时间，总使用流量，总消费
	 * @param keyType
	 * @return
	 */
	public List<DingWeiExport> query1(@Param("imsis") String imsis,@Param("startTime") String startTime,@Param("endTime") String endTime);
	
	public List<DingWeiMonth> queryMonth(@Param("imsis") String imsis,@Param("startTime") String startTime,@Param("endTime") String endTime);
	
	public List<C9LfeuTapData> queryAll();
	
	public List<C9LfeuTapData> queryAllDingWei(@Param("imsis") String imsis,@Param("startTime") String startTime,@Param("endTime") String endTime);

}
