package com.coupang.recruitingportalbackend.vault.controller;

import com.coupang.recruitingportalbackend.vault.controller.service.VaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vault")
public class VaultController {

    private final VaultService vaultService;

    @Autowired
    public VaultController(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @PostMapping("/tokenize")
    public String tokenize(@RequestParam String role, @RequestParam String value) {
        return vaultService.tokenize(role, value);
    }

    @PostMapping("/detokenize")
    public String detokenize(@RequestParam String role, @RequestParam String tokenizedValue) {
        return vaultService.detokenize(role, tokenizedValue);
    }
}