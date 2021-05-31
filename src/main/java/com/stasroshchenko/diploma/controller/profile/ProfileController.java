package com.stasroshchenko.diploma.controller.profile;

import com.stasroshchenko.diploma.entity.database.Visit;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.service.PersonDataService;
import com.stasroshchenko.diploma.service.VisitService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {

    private final VisitService visitService;
    private final PersonDataService personDataService;

    @GetMapping
    public String getProfileView(Authentication authentication, Model model) {

        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            switch (grantedAuthority.getAuthority()) {
                case "ROLE_CLIENT":
                    return getClientProfileView(authentication, model);
                case "ROLE_DOCTOR":
                    return getDoctorProfileView(authentication, model);
                case "ROLE_ADMIN":
                    return getAdminProfileView(authentication, model);
            }
        }

        throw new IllegalStateException("Unknown role");
    }

    public String getClientProfileView(Authentication authentication, Model model) {
        ApplicationUserClient principal =
                (ApplicationUserClient) authentication.getPrincipal();
        model.addAttribute("principal", principal);

        List<Visit> allVisits = visitService
                .getAllVisitsByClient(principal.getClientData());
        model.addAttribute("allVisits", allVisits);

        List<DoctorData> allDoctors =
                personDataService.getAllDoctors();
        model.addAttribute("allDoctors", allDoctors);

        model.addAttribute("visitToSend", new Visit());
        return "client_profile";
    }

    public String getDoctorProfileView(Authentication authentication, Model model) {
        ApplicationUserDoctor principal =
                (ApplicationUserDoctor) authentication.getPrincipal();
        model.addAttribute("principal", principal);
        List<Visit> allVisits = visitService
                .getAllVisitsByDoctor(principal.getDoctorData());
        model.addAttribute("allVisits", allVisits);
        return "doctor_profile";
    }

    public String getAdminProfileView(Authentication authentication, Model model) {
        return "admin_profile";
    }

}
