package com.ai.paas.ipaas.ips.dao.interfaces;

import com.ai.paas.ipaas.ips.dao.mapper.bo.IpaasSysConfig;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IpaasSysConfigCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IpaasSysConfigMapper {
    int countByExample(IpaasSysConfigCriteria example);

    int deleteByExample(IpaasSysConfigCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(IpaasSysConfig record);

    int insertSelective(IpaasSysConfig record);

    List<IpaasSysConfig> selectByExample(IpaasSysConfigCriteria example);

    IpaasSysConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") IpaasSysConfig record, @Param("example") IpaasSysConfigCriteria example);

    int updateByExample(@Param("record") IpaasSysConfig record, @Param("example") IpaasSysConfigCriteria example);

    int updateByPrimaryKeySelective(IpaasSysConfig record);

    int updateByPrimaryKey(IpaasSysConfig record);
}