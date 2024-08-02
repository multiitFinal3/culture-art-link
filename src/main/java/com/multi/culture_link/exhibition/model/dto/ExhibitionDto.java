package com.multi.culture_link.exhibition.model.dto;

import com.multi.culture_link.admin.exhibition.model.dto.api.ExhibitionApiDto;
import lombok.Data;


@Data
public class ExhibitionDto extends ExhibitionApiDto {
    public String state;
    public int starsAVG;
}
