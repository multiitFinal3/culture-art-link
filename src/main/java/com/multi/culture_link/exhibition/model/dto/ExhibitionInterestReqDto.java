package com.multi.culture_link.exhibition.model.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

@Data
public class ExhibitionInterestReqDto {
    public int exhibitionId;
    public String state;
}
