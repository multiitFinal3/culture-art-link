package com.multi.culture_link.admin.performance.mapper;

import com.multi.culture_link.admin.performance.model.dto.PerformanceDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PerformanceMapper {
    List<PerformanceDTO> getAllPerformances();

    void insertPerformance(PerformanceDTO performance);

    int deletePerformances(List<String> selectedIds);
}
