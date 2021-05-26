package com.stasroshchenko.diploma.controller;

import com.google.common.collect.Lists;
import com.stasroshchenko.diploma.entity.Client;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/clients")
public class ClientController {

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


    @GetMapping
    public String getView(
            @ModelAttribute Client client) {

        return "clientsTemp";
    }


    @PostMapping("/save")
    public String setClientName(
            @RequestParam("id") Integer id,
            @RequestParam("name") String name) {

        Client client = findById(id);
        client.setName(name);
        return "redirect:/clients";
    }

    @PostMapping("/add")
    public String addClient(
            @ModelAttribute Client client) {

        CLIENTS.add(client);
        return "redirect:/clients";
    }

}
