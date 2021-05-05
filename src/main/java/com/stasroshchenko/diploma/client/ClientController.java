package com.stasroshchenko.diploma.client;

import com.google.common.collect.Lists;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/clients")
public class ClientController {

    private final static List<Client> CLIENTS = Lists.newArrayList(
            new Client(1, "Jessica Parker"),
            new Client(2, "Phoenix Vengeful"),
            new Client(3, "Stashkevich Ihor")
    );

    @GetMapping("{id}")
    public Client getClient(@PathVariable("id") Integer id) {
        return CLIENTS.stream()
                .filter(client -> id.equals(client.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Client with id " + id + " doesn't exist"));
    }

    @GetMapping
    public List<Client> getClients() {
        return CLIENTS;
    }

}
