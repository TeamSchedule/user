package com.schedule.user.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DefaultErrorResponse {
    private Integer errorCount;
    private List<String> errors;
}
