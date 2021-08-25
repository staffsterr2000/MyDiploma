package com.stasroshchenko.diploma.controller;

import com.stasroshchenko.diploma.entity.person.ClientData;
import com.stasroshchenko.diploma.entity.person.DoctorData;
import com.stasroshchenko.diploma.entity.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.request.visit.*;
import com.stasroshchenko.diploma.model.service.VisitService;
import com.stasroshchenko.diploma.util.VisitStatus;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Works with all the POST requests incoming in "localhost:8080/visit".
 * @author staffsterr2000
 * @version 1.0
 */
@Controller
@RequestMapping("/visit")
@AllArgsConstructor
public class VisitController {

    /**
     * Connection with visit service
     */
    private final VisitService visitService;



    /**
     * If request has no validation and binding errors, and user authorized
     * as a client user, the method tries to send the request to service
     * and to redirect user back to the previous page.
     * @param redirectLink previous link to redirect later
     * @param request the filled-by-client-user SEND visit request, which is validated
     * @param result result of validation and binding (contains errors)
     * @param redirectAttributes attributes that will be redirected to next view
     * @param authentication current user's authentication
     * @return link where user will be redirected
     * @since 1.0
     * @see SendVisitRequest
     */
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping("/send")
    public String sendVisit(
            @RequestParam("redirectLink") String redirectLink,
            @Valid @ModelAttribute("sendRequest") SendVisitRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("sendRequest", request);
            redirectAttributes.addFlashAttribute("bindingResult", result);
            return "redirect:/" + redirectLink;
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

        return "redirect:/" + redirectLink;

    }



    /**
     * If request has no validation and binding errors, and user authorized
     * as a client user, the method sends request to service and redirects
     * user back to the previous page.
     * @param redirectLink previous link to redirect later
     * @param request the filled-by-client-user CANCEL visit request, which is validated
     * @param result result of validation and binding (contains errors)
     * @param redirectAttributes attributes that will be redirected to next view
     * @param authentication current user's authentication
     * @return link where user will be redirected
     * @since 1.0
     * @see CancelVisitRequest
     */
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping("/cancel")
    public String cancelVisit(
            @RequestParam("redirectLink") String redirectLink,
            @Valid @ModelAttribute("cancelRequest") CancelVisitRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("cancelRequest", request);
            redirectAttributes.addFlashAttribute("bindingResult", result);
            return "redirect:/" + redirectLink;
        }

        ApplicationUserClient principal = (ApplicationUserClient)
                authentication.getPrincipal();
        ClientData clientUser = principal.getClientData();

        visitService.cancelVisit(clientUser, request);

        return "redirect:/" + redirectLink;

    }



    /**
     * If request has no validation and binding errors, and user authorized
     * as a doctor user, the method tries to send the request to service and
     * to redirect user back to the previous page.
     * @param redirectLink previous link to redirect later
     * @param request the filled-by-doctor-user CREATE visit request, which is validated
     * @param result result of validation and binding (contains errors)
     * @param redirectAttributes attributes that will be redirected to next view
     * @param authentication current user's authentication
     * @return link where user will be redirected
     * @since 1.0
     * @see CreateVisitRequest
     */
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/create")
    public String createVisit(
            @RequestParam("redirectLink") String redirectLink,
            @Valid @ModelAttribute("createRequest") CreateVisitRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("createRequest", request);
            redirectAttributes.addFlashAttribute("bindingResult", result);
            return "redirect:/" + redirectLink;
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

        return "redirect:/" + redirectLink;
    }



    /**
     * If request has no validation and binding errors, and user authorized
     * as a doctor user, the method tries to send the request to service and
     * to redirect user back to the previous page.
     * @param redirectLink previous link to redirect later
     * @param request the filled-by-doctor-user ACCEPT visit request, which is validated
     * @param result result of validation and binding (contains errors)
     * @param redirectAttributes attributes that will be redirected to next view
     * @param authentication current user's authentication
     * @return link where user will be redirected
     * @since 1.0
     * @see AcceptVisitRequest
     */
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/accept")
    public String acceptVisit(
            @RequestParam("redirectLink") String redirectLink,
            @Valid @ModelAttribute("acceptRequest") AcceptVisitRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("acceptRequest", request);
            redirectAttributes.addFlashAttribute("bindingResult", result);
            return "redirect:/" + redirectLink;
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

        return "redirect:/" + redirectLink;
    }



    /**
     * If request has no validation and binding errors, and user authorized
     * as a doctor user, the method sends the request to service and redirects
     * user back to the previous page.
     * @param redirectLink previous link to redirect later
     * @param request the filled-by-doctor-user DECLINE visit request, which is validated
     * @param result result of validation and binding (contains errors)
     * @param redirectAttributes attributes that will be redirected to next view
     * @param authentication current user's authentication
     * @return link where user will be redirected
     * @since 1.0
     * @see DeclineVisitRequest
     */
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/decline")
    public String declineVisit(
            @RequestParam("redirectLink") String redirectLink,
            @Valid @ModelAttribute("declineRequest") DeclineVisitRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", result);
            return "redirect:/" + redirectLink;
        }

        ApplicationUserDoctor principal =
                (ApplicationUserDoctor) authentication.getPrincipal();
        DoctorData doctorUser = principal.getDoctorData();

        visitService.declineVisit(doctorUser, request);

        return "redirect:/" + redirectLink;
    }



    /**
     * If request has no validation and binding errors, and user authorized
     * as a doctor user, the method tries to send the request to service and
     * to redirect user back to the previous page.
     * @param redirectLink previous link to redirect later
     * @param status new visit status that user want to change certain visit's status to
     * @param request the filled-by-doctor-user PASS visit request, which is validated
     * @param result result of validation and binding (contains errors)
     * @param redirectAttributes attributes that will be redirected to next view
     * @param authentication current user's authentication
     * @return link where user will be redirected
     * @since 1.0
     * @see PassVisitRequest
     */
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/pass")
    public String passVisit(
            @RequestParam("redirectLink") String redirectLink,
            @RequestParam("status") VisitStatus status,
            @Valid @ModelAttribute("passRequest") PassVisitRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", result);
            return "redirect:/" + redirectLink;
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

        return "redirect:/" + redirectLink;
    }

}
