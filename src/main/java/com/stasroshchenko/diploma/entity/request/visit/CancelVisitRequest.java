package com.stasroshchenko.diploma.entity.request.visit;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelVisitRequest {

    @NotNull
    private Long visitId;

}
