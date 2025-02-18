package com.ntu.sc2006.hawkerlah

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain
import javax.crypto.spec.SecretKeySpec

@Configuration
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .requiresChannel{channel ->
            channel.anyRequest().requiresSecure()}
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/customer/**").authenticated()
                it.requestMatchers("/hawker-owners/**").authenticated()
            }
            .oauth2ResourceServer { oauth2 -> oauth2.jwt { } }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
        return http.build()
    }

    @Value("\${supabase.jwt-secret}")
    lateinit var jwtSecret: String

    @Bean
    fun jwtDecoder(): NimbusJwtDecoder {
        val secretKey = SecretKeySpec(jwtSecret.toByteArray(), "HmacSHA256")
        return NimbusJwtDecoder.withSecretKey(secretKey).build()
    }

}