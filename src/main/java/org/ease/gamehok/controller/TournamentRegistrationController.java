package org.ease.gamehok.controller;

import org.ease.gamehok.dto.RegistrationRequest;
import org.ease.gamehok.entity.TournamentRegistration;
import org.ease.gamehok.service.TournamentRegistrationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "*")
public class TournamentRegistrationController {

    private final TournamentRegistrationService service;

    public TournamentRegistrationController(
            TournamentRegistrationService service) {

        this.service = service;
    }

    @PostMapping
    public TournamentRegistration registerTeam(
            @RequestBody RegistrationRequest request) {

        return service.registerTeam(request);
    }

    @GetMapping("/tournament/{tournamentId}")
    public List<TournamentRegistration> getRegisteredTeams(
            @PathVariable Long tournamentId) {

        return service.getRegisteredTeams(tournamentId);
    }

    @DeleteMapping("/{id}")
    public void deleteRegistration(
            @PathVariable Long id) {

        service.deleteRegistration(id);
    }
}