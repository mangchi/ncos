package com.coupang.recruitingportalbackend.vault.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;

import java.util.Map;

@Service
public class VaultService {

    private final VaultTemplate vaultTemplate;

    @Autowired
    public VaultService(VaultTemplate vaultTemplate) {
        this.vaultTemplate = vaultTemplate;
    }

    // 토큰화 메서드
    public String tokenize(String role, String value) {
        Map<String, String> data = new HashMap<>();
        data.put("value", value);
        data.put("role", role);

        VaultResponse response = vaultTemplate.write("transform/encode/my-card-transform", data);
        return (String) response.getData().get("encoded_value");
    }

    // 디토큰화 메서드
    public String detokenize(String role, String tokenizedValue) {
        Map<String, String> data = new HashMap<>();
        data.put("value", tokenizedValue);
        data.put("role", role);

        VaultResponse response = vaultTemplate.write("transform/decode/my-card-transform", data);
        return (String) response.getData().get("decoded_value");
    }
}