package com.ai.paas.ipaas.ips.dao.interfaces;

import com.ai.paas.ipaas.ips.dao.mapper.bo.IdpsInstanceBandDss;
import com.ai.paas.ipaas.ips.dao.mapper.bo.IdpsInstanceBandDssCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IdpsInstanceBandDssMapper {
    int countByExample(IdpsInstanceBandDssCriteria example);

    int deleteByExample(IdpsInstanceBandDssCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(IdpsInstanceBandDss record);

    int insertSelective(IdpsInstanceBandDss record);

    List<IdpsInstanceBandDss> selectByExample(IdpsInstanceBandDssCriteria example);

    IdpsInstanceBandDss selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") IdpsInstanceBandDss record, @Param("example") IdpsInstanceBandDssCriteria example);

    int updateByExample(@Param("record") IdpsInstanceBandDss record, @Param("example") IdpsInstanceBandDssCriteria example);

    int updateByPrimaryKeySelective(IdpsInstanceBandDss record);

    int updateByPrimaryKey(IdpsInstanceBandDss record);
}