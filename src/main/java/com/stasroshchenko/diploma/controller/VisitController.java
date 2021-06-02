package com.stasroshchenko.diploma.controller;

import com.stasroshchenko.diploma.entity.database.Visit;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.service.PersonDataService;
import com.stasroshchenko.diploma.service.VisitService;
import com.stasroshchenko.diploma.util.VisitStatus;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/visit")
@AllArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final PersonDataService personDataService;

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping("/as-client/create")
    public String createVisitAsClient(
            @RequestParam("doctorDataId") Long doctorDataId,
            @ModelAttribute("visitToSend") Visit visit,
            BindingResult result,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        ApplicationUserClient principal = (ApplicationUserClient)
                authentication.getPrincipal();

        visit.setClientData(principal.getClientData());

        DoctorData doctorData =
                personDataService.getDoctorById(doctorDataId);
        visit.setDoctorData(doctorData);

        visit.setStatus(VisitStatus.SENT);

        try {
            visitService.saveVisit(visit);
        } catch (IllegalStateException ex) {
            result.addError(new ObjectError("complaintError", ex.getMessage()));
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", result);
            return "redirect:/profile-error";
        }

        return "redirect:/profile";
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/accept")
    public String acceptVisit(
            @RequestParam("visitId") Long visitId,
            @Valid @ModelAttribute("visitToAccept") Visit visitToAccept,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        LocalDateTime visitDate = visitToAccept.getAppointsAt();

        if (!visitService.isDateValid(visitDate)) {
            result.addError(new ObjectError("dateValidError",
                    "Date is invalid. Try using \"0\" before another number. Example (08:09 11/01/2000)"));
        }

        if (!visitService.isTimeFree(visitDate)) {
            result.addError(new ObjectError("timeAlreadyTakenError",
                    "This time has already been taken for another visit. Try another time"));
        }

        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", result);
            return "redirect:/profile-error";
        }

        Visit visitFromDatabase =
                visitService.getVisitById(visitId);

        visitFromDatabase.setAppointsAt(visitToAccept.getAppointsAt());
        visitFromDatabase.setAcceptedAt(LocalDateTime.now());
        visitFromDatabase.setStatus(VisitStatus.ACTIVE);

        visitService.saveVisit(visitFromDatabase);

        return "redirect:/profile";
    }


    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/decline")
    public String declineVisit(
            @RequestParam("visitId") Long visitId,
            Authentication authentication) {

        ApplicationUserDoctor applicationUserDoctor =
                (ApplicationUserDoctor) authentication.getPrincipal();

        visitService.declineVisit(visitId, applicationUserDoctor.getDoctorData());

        return "redirect:/profile";
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/pass")
    public String passVisit(
            @RequestParam("visitId") Long visitId,
            @RequestParam("status") String status,
            Authentication authentication) {

        VisitStatus visitStatus = VisitStatus.valueOf(status);

        ApplicationUserDoctor applicationUserDoctor =
                (ApplicationUserDoctor) authentication.getPrincipal();

        visitService.passVisit(visitId, visitStatus, applicationUserDoctor.getDoctorData());

        return "redirect:/profile";
    }

}
