package com.taskboard.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubTaskResponse {
    private Long id;
    private String name;
    private String description;
    private boolean done;
}
