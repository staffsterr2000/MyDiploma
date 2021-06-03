package com.stasroshchenko.diploma.entity.request.visit;

import com.stasroshchenko.diploma.util.VisitStatus;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PassVisitRequest {

    @NotNull
    private Long visitId;

    private VisitStatus status;

}
