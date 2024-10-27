package com.coupang.recruitingportalbackend.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.config.AbstractVaultConfiguration;

//@Configuration
//@EnableConfigurationProperties(VaultProps.class)
//@RequiredArgsConstructor
public class VaultConfig extends AbstractVaultConfiguration {
    private String username;
    private String password;
    private String jdbcurl;
}
