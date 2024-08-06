package com.multi.culture_link.admin.exhibition.model.dto.api;

import lombok.Data;



@Data
public class ExhibitionApiDto {
    public int id;
    public String title;
    public String artist;
    public String museum;
    public String localId;
    public String startDate;
    public String endDate;
    public String price;
    public String image;
    public String description;
    public String subDescription;

    public String url;
}
