package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bean.DaysOrdersNumber;
import com.bean.DingWeiExport;
import com.bean.DingWeiMonth;
import com.bean.PackageUseDetails;
import com.bean.ResolvePackageOrderDays;

public interface IM2mCardInfoDao {

	/**
	 * 统计指定imsis在指定时间段的首次使用时间，最后一次使用时间，总使用流量，总消费
	 * @param keyType
	 * @return
	 */
	public List<String> queryAllDingWeiImsi();
	
}
