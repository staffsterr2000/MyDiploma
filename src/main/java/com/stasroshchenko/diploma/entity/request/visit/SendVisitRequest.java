package com.stasroshchenko.diploma.entity.request.visit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SendVisitRequest {

    @NotNull
    private Long doctorDataId;

    @NotBlank
    private String complaint;

}
