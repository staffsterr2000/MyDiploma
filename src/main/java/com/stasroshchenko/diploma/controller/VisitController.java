package com.stasroshchenko.diploma.controller;

import com.stasroshchenko.diploma.entity.database.ApplicationUser;
import com.stasroshchenko.diploma.entity.database.Visit;
import com.stasroshchenko.diploma.entity.database.person.ClientData;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.service.PersonDataService;
import com.stasroshchenko.diploma.service.VisitService;
import com.stasroshchenko.diploma.util.VisitStatus;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/visit")
@AllArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final PersonDataService personDataService;

    @PostMapping("/create-as-client")
    public String createVisitAsClient(
            @RequestParam("doctorDataId") Long doctorDataId,
            @ModelAttribute("visitToSend") Visit visit,
            Authentication authentication) {
        ApplicationUser principal = (ApplicationUser) authentication.getPrincipal();

        visit.setClientData((ClientData) principal.getPersonData());

        DoctorData doctorData = (DoctorData)
                personDataService.getPersonById(doctorDataId);
        visit.setDoctorData(doctorData);

        visit.setStatus(VisitStatus.SENT);

        visitService.saveVisit(visit);
        return "redirect:/profile";
    }

//    @PostMapping("/create-as-doctor")
//    public String createVisitAsDoctor(
//            @RequestParam("clientDataId") Long clientDataId,
//            @ModelAttribute("visitToSend") Visit visit,
//            Authentication authentication) {
//        ApplicationUser principal = (ApplicationUser) authentication.getPrincipal();
//
//        visit.setClientData((ClientData) principal.getPersonData());
//
//        DoctorData doctorData = (DoctorData)
//                personDataService.getPersonById(doctorDataId);
//        visit.setDoctorData(doctorData);
//
//        visit.setStatus(VisitStatus.SENT);
//
//        visitService.saveVisit(visit);
//        return "redirect:/profile";
//    }

}
