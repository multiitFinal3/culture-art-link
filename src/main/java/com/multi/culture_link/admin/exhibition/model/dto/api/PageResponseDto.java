package com.multi.culture_link.admin.exhibition.model.dto.api;

import lombok.Data;

import java.util.List;

@Data
public class PageResponseDto<T> {
    private List<T> data;
    private String nextCursor;
}