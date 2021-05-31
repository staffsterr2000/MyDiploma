package com.stasroshchenko.diploma.controller;

import com.stasroshchenko.diploma.entity.database.Visit;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.service.PersonDataService;
import com.stasroshchenko.diploma.service.VisitService;
import com.stasroshchenko.diploma.util.VisitStatus;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/visit")
@AllArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final PersonDataService personDataService;

    @PostMapping("/as-client/create")
    public String createVisitAsClient(
            @RequestParam("doctorDataId") Long doctorDataId,
            @ModelAttribute("visitToSend") Visit visit,
            Authentication authentication) {
        ApplicationUserClient principal = (ApplicationUserClient)
                authentication.getPrincipal();

        visit.setClientData(principal.getClientData());

        DoctorData doctorData =
                personDataService.getDoctorById(doctorDataId);
        visit.setDoctorData(doctorData);

        visit.setStatus(VisitStatus.SENT);

        visitService.saveVisit(visit);
        return "redirect:/profile";
    }

    @PostMapping("/as-doctor/accept")
    public String acceptVisitAsDoctor(
            @RequestParam("visitId") Long visitId,
            @Valid @ModelAttribute("visitToAccept") Visit visitToAccept,
            BindingResult result) {

        if (!visitService.isTimeFree(visitToAccept.getAppointsAt())) {
            result.addError(new ObjectError("error",
                    "This time has already been taken for another visit. Try another time"));
        }

        if(result.hasErrors()) {
            return "doctor_profile";
        }

        Visit visitFromDatabase =
                visitService.getVisitById(visitId);

        visitFromDatabase.setAppointsAt(visitToAccept.getAppointsAt());
        visitFromDatabase.setAcceptedAt(LocalDateTime.now());
        visitFromDatabase.setStatus(VisitStatus.ACTIVE);

        visitService.saveVisit(visitFromDatabase);

        return "redirect:/profile";
    }

}
