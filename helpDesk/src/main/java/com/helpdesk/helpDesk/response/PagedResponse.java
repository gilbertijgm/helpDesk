package com.helpdesk.helpDesk.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {

    private int statusCode;
    private String message;
    private List<T> data;
    private Pagination pagination;
    private Map<String, String> links;
}
