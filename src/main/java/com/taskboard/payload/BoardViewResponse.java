package com.taskboard.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class BoardViewResponse {
    private Long id;
    private String name;
    private String description;
}
