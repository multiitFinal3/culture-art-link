package com.multi.culture_link.admin.exhibition.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExhibitionApiResponseDto {
    public String title;
    @JsonProperty("CNTC_INSTT_NM")
    public String museum;
    public String collected_date;
    public String issued_date;
    public String description;
    public String image_object;
    public String local_id;
    public String url;
    public String view_count;
    public String sub_description;
    public String spatial_coverage;
    public String event_site;
    public String genre;
    public String duration;
    public String number_pages;
    public String table_of_contents;
    public String author;
    public String contact_point;
    public String actor;
    public String contributor;
    public String audience;
    public String charge;
    public String period;
    public String event_period;
}
