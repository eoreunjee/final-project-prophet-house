package com.ssafy.home.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.home.dto.HomeDeal;
import com.ssafy.home.dto.HomeInfo;

@Mapper
public interface SearchMapper {
	
	List<HomeInfo> searchAptsByName(@Param("aptName") String aptName);
	List<HomeDeal> selectDealsByAptSeq(@Param("aptSeqList") List<String> aptSeqList);

}
