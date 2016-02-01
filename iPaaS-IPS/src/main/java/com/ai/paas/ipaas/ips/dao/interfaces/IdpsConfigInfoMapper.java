package com.ai.paas.ipaas.ips.dao.interfaces;

import com.ai.paas.ipaas.ips.dao.mapper.bo.IdpsConfigInfo;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IdpsConfigInfoCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IdpsConfigInfoMapper {
    int countByExample(IdpsConfigInfoCriteria example);

    int deleteByExample(IdpsConfigInfoCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(IdpsConfigInfo record);

    int insertSelective(IdpsConfigInfo record);

    List<IdpsConfigInfo> selectByExample(IdpsConfigInfoCriteria example);

    IdpsConfigInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") IdpsConfigInfo record, @Param("example") IdpsConfigInfoCriteria example);

    int updateByExample(@Param("record") IdpsConfigInfo record, @Param("example") IdpsConfigInfoCriteria example);

    int updateByPrimaryKeySelective(IdpsConfigInfo record);

    int updateByPrimaryKey(IdpsConfigInfo record);
}