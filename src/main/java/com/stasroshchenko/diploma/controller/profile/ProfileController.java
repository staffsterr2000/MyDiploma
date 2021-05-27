package com.stasroshchenko.diploma.controller.profile;

import com.google.common.collect.Lists;
import com.stasroshchenko.diploma.entity.Client;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @GetMapping
    public String getProfileView(Authentication authentication, Model model) {

        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            switch (grantedAuthority.getAuthority()) {
                case "ROLE_CLIENT":
                    return getClientProfileView();
                case "ROLE_DOCTOR":
                    return getDoctorProfileView(model);
                case "ROLE_ADMIN":
                    return getAdminProfileView();
            }
        }

        throw new IllegalStateException("Unknown role");
    }

    public String getClientProfileView() {
        return "client_profile";
    }

    public String getDoctorProfileView(Model model) {
        model.addAttribute(new Client());
        return "doctor_profile";
    }

    public String getAdminProfileView() {
        return "admin_profile";
    }

    // *********************************************

    private final static List<Client> CLIENTS = Lists.newArrayList(
            new Client(1, "Jessica Parker"),
            new Client(2, "Phoenix Vengeful"),
            new Client(3, "Stashkevich Ihor")
    );

    private Client findById(Integer id) {
        return CLIENTS.stream()
                .filter(client -> id.equals(client.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Client with id " + id + " doesn't exist"));
    }

    @ModelAttribute("allClients")
    public List<Client> getAllClients() {
        return CLIENTS;
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/save")
    public String setClientName(
            @RequestParam("id") Integer id,
            @RequestParam("name") String name) {

        Client client = findById(id);
        client.setName(name);
        return "redirect:/profile";
    }

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @PostMapping("/add")
    public String addClient(
            @ModelAttribute Client client) {

        CLIENTS.add(client);
        return "redirect:/profile";
    }

}
