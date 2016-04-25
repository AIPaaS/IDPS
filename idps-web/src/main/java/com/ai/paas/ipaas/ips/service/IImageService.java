package com.ai.paas.ipaas.ips.service;

import java.util.List;
import java.util.Map;

import com.ai.paas.ipaas.ips.dao.mapper.bo.IdpsConfigInfo;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IdpsInstanceBandDss;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IpaasSysConfig;


public interface IImageService {
	
	
	public List<IdpsConfigInfo> search();
	
	
	public List<IdpsInstanceBandDss> srarchDssInfo(String userId,String serviceId);
	
	public List<IpaasSysConfig> searchConfig(String tableCode,String fieldCode);
}
