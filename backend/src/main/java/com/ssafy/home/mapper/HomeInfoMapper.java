package com.ssafy.home.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ssafy.home.dto.HomeInfo;

@Mapper
public interface HomeInfoMapper {
	
	List<HomeInfo> getAptListPaged(@Param("dongCode") String dongCode,
					            @Param("aptName") String aptName,
					            @Param("size") int size,
					            @Param("offset") int offset);
	
	int countAptList(@Param("dongCode") String dongCode,
					 @Param("aptName") String aptName);

    List<String> getSidoList();
    
    List<String> getGugunList(@Param("sido") String sido);
    
    List<Map<String, String>> getDongList(String gugun);
}
