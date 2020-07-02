package com.taskboard.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationResponse {
    Long id;
    String boardName;
    String boardDescription;
}
