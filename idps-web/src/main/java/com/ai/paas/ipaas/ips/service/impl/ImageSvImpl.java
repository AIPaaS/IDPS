package com.ai.paas.ipaas.ips.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ips.dao.interfaces.IdpsConfigInfoMapper;
import com.ai.paas.ipaas.ips.dao.interfaces.IdpsInstanceBandDssMapper;
import com.ai.paas.ipaas.ips.dao.interfaces.IpaasSysConfigMapper;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IdpsConfigInfo;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IdpsConfigInfoCriteria;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IdpsInstanceBandDss;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IdpsInstanceBandDssCriteria;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IpaasSysConfig;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IpaasSysConfigCriteria;
import com.ai.paas.ipaas.ips.service.IImageService;

@Service
public class ImageSvImpl  implements IImageService{
	
	@Override
	public  List<IdpsConfigInfo> search(){
		
		IdpsConfigInfoMapper idpsMapper = ServiceUtil.getMapper(IdpsConfigInfoMapper.class);
		IdpsConfigInfoCriteria criteria = new IdpsConfigInfoCriteria();
		List<IdpsConfigInfo> list = idpsMapper.selectByExample(criteria);
		return list;
	}
	
	@Override
	public List<IdpsInstanceBandDss> srarchDssInfo(String userId,String serviceId){
		
		IdpsInstanceBandDssMapper mapper = ServiceUtil.getMapper(IdpsInstanceBandDssMapper.class);
		IdpsInstanceBandDssCriteria criteria = new IdpsInstanceBandDssCriteria();
		criteria.createCriteria()
			.andUserIdEqualTo(userId)
			.andServiceIdEqualTo(serviceId);
		List<IdpsInstanceBandDss> list = mapper.selectByExample(criteria);
		return list;
	}
	
	@Override
	public  List<IpaasSysConfig> searchConfig(String tableCode,String fieldCode){
		
		IpaasSysConfigMapper mapper = ServiceUtil.getMapper(IpaasSysConfigMapper.class);
		IpaasSysConfigCriteria criteria = new IpaasSysConfigCriteria();
		criteria.createCriteria()
			.andTableCodeEqualTo(tableCode)
			.andFieldCodeEqualTo(fieldCode);
		List<IpaasSysConfig> list = mapper.selectByExample(criteria);
		return list;
	}
	
}
