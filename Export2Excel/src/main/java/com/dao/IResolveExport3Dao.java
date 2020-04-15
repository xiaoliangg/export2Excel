package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bean.DaysOrdersNumber;
import com.bean.PackageUseDetails;
import com.bean.ResolveExport3;
import com.bean.ResolvePackageOrderDays;

/**
 * 厂商密钥表dao接口
 * 
 * @author Administrator
 * 
 */
public interface IResolveExport3Dao {

	/**
	 * TODO
	 * @param keyType
	 * @return
	 */
	public List<ResolveExport3> getResolveExport(@Param("channel") String channel);

}
