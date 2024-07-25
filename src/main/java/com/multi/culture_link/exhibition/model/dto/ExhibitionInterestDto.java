package com.multi.culture_link.exhibition.model.dto;

import lombok.Data;


@Data
public class ExhibitionInterestDto {
    public int id;
    public int userId;
    public int exhibitionId;
    public String state;
}
