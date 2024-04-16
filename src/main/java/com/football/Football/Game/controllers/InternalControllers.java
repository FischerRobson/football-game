package com.football.Football.Game.controllers;

import com.football.Football.Game.utils.SlugGenerator;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/internal")
public class InternalControllers {

    @PostMapping("/slug")
    public String generateSlug(@RequestParam String value) {
        return SlugGenerator.generateSlug(value);
    }

}
