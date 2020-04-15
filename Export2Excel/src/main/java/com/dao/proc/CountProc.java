package com.dao.proc;

import java.util.Map;

/**
 * 统计
 * 
 * @author Administrator
 * 
 */
public interface CountProc {

	public void countResolveProc(Map<String, String> paramMap);
	public void countPartnerProc(Map<String, String> paramMap);
	public void countM2mcardZhunerProc(Map<String, String> paramMap);
	public void countProcChunmo(Map<String, String> paramMap);
}
