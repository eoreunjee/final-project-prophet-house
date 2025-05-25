package com.ssafy.home.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ssafy.home.dto.HomeInfo;

@Mapper
public interface HomeInfoMapper {
    List<HomeInfo> getPagedHouses(@Param("page") int page, @Param("pageSize") int pageSize);

    List<HomeInfo> getHousesByDong(@Param("dongCode")  String dongCode);
    
    List<HomeInfo> findAllAptByName(@Param("aptName") String aptName);

    List<String> getSidoList();
    List<String> getGugunList(@Param("sido") String sido);
    
    List<Map<String, String>> getDongList(String gugun);
}
