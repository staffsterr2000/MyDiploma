package com.stasroshchenko.diploma.entity.request.visit;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeclineVisitRequest {

    @NotNull
    private Long visitId;

}
