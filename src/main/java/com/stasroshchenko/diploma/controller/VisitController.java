package com.stasroshchenko.diploma.controller;

import com.stasroshchenko.diploma.entity.database.person.ClientData;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.entity.request.visit.*;
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

@Controller
@RequestMapping("/visit")
@AllArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping("/send")
    public String sendVisit(
            @Valid @ModelAttribute("sendRequest") SendVisitRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("sendRequest", request);
            redirectAttributes.addFlashAttribute("bindingResult", result);
            return "redirect:/profile";
        }

        ApplicationUserClient principal = (ApplicationUserClient)
                authentication.getPrincipal();
        ClientData clientUser = principal.getClientData();

        try {
            visitService.sendVisit(clientUser, request);

        } catch (IllegalStateException ex) {
            result.addError(new ObjectError("sendVisitError", ex.getMessage()));
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("sendRequest", request);
            redirectAttributes.addFlashAttribute("bindingResult", result);
        }

        return "redirect:/profile";

    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping("/cancel")
    public String cancelVisit(
            @Valid @ModelAttribute("cancelRequest") CancelVisitRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("cancelRequest", request);
            redirectAttributes.addFlashAttribute("bindingResult", result);
            return "redirect:/profile";
        }

        ApplicationUserClient principal = (ApplicationUserClient)
                authentication.getPrincipal();
        ClientData clientUser = principal.getClientData();

        visitService.cancelVisit(clientUser, request);

        return "redirect:/profile";

    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/create")
    public String createVisit(
            @Valid @ModelAttribute("createRequest") CreateVisitRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("createRequest", request);
            redirectAttributes.addFlashAttribute("bindingResult", result);
            return "redirect:/profile";
        }

        ApplicationUserDoctor principal = (ApplicationUserDoctor)
                authentication.getPrincipal();
        DoctorData doctorUser = principal.getDoctorData();

        try {
            visitService.createVisit(doctorUser, request);

        } catch (IllegalStateException ex) {
            result.addError(new ObjectError("createDateValidError", ex.getMessage()));
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("createRequest", request);
            redirectAttributes.addFlashAttribute("bindingResult", result);
        }

        return "redirect:/profile";
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/accept")
    public String acceptVisit(
            @Valid @ModelAttribute("acceptRequest") AcceptVisitRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("acceptRequest", request);
            redirectAttributes.addFlashAttribute("bindingResult", result);
            return "redirect:/profile";
        }

        ApplicationUserDoctor principal = (ApplicationUserDoctor)
                authentication.getPrincipal();
        DoctorData doctorUser = principal.getDoctorData();

        try {
            visitService.acceptVisit(doctorUser, request);

        } catch (IllegalStateException ex) {
            result.addError(new ObjectError("acceptDateValidError", ex.getMessage()));
        }

        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute("acceptRequest", request);
            redirectAttributes.addFlashAttribute("bindingResult", result);
        }

        return "redirect:/profile";
    }


    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/decline")
    public String declineVisit(
            @Valid @ModelAttribute("declineRequest") DeclineVisitRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", result);
            return "redirect:/profile";
        }

        ApplicationUserDoctor principal =
                (ApplicationUserDoctor) authentication.getPrincipal();
        DoctorData doctorUser = principal.getDoctorData();

        visitService.declineVisit(doctorUser, request);

        return "redirect:/profile";
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/pass")
    public String passVisit(
            @RequestParam("status") VisitStatus status,
            @Valid @ModelAttribute("passRequest") PassVisitRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", result);
            return "redirect:/profile";
        }

        ApplicationUserDoctor principal =
                (ApplicationUserDoctor) authentication.getPrincipal();
        DoctorData doctorUser = principal.getDoctorData();

        request.setStatus(status);

        try {
            visitService.passVisit(doctorUser, request);

        } catch (IllegalStateException ex) {
            result.addError(new ObjectError("passVisitError", ex.getMessage()));
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", result);
        }

        return "redirect:/profile";
    }

}
