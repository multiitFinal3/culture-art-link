package com.multi.culture_link.admin.exhibition.model.dto.api;

import lombok.Data;

@Data
public class ExhibitionApiDto {
    @Data
    static class exhibition{
        public int id;
        public String title;
        public String artist;
        public String museum;
        public String start_date;
        public String end_date;
        public int price;
        public String image;
        public String description;
        public String sub_description;
        public String url;
    }

}
