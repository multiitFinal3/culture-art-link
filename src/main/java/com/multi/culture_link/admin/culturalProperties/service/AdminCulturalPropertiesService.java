package com.multi.culture_link.admin.culturalProperties.service;


import com.multi.culture_link.admin.culturalProperties.dto.CulturalPropertiesDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminCulturalPropertiesService {


    List<CulturalPropertiesDTO> fetchApiData();
}
