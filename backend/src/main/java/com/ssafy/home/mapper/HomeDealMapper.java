package com.ssafy.home.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ssafy.home.dto.HomeDeal;

@Mapper
public interface HomeDealMapper {
    List<HomeDeal> getDealsByHouse(@Param("aptSeq") String aptSeq);
}
