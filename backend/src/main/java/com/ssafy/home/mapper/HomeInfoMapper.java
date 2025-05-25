package com.ssafy.home.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ssafy.home.dto.HomeInfo;

@Mapper
public interface HomeInfoMapper {
    List<HomeInfo> getPagedHouses(@Param("page") int page, @Param("pageSize") int pageSize);

    List<HomeInfo> getHousesByDong(@Param("sido") String sido,
                                   @Param("gugun") String gugun,
                                   @Param("dong")  String dong);
    
    List<HomeInfo> findAllAptByName(@Param("aptName") String aptName);

    List<String> getSidoList();
    List<String> getGugunList(@Param("sido") String sido);
    List<String> getDongList(@Param("gugun") String gugun);
}
