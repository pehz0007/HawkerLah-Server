package com.ntu.sc2006.hawkerlah.controller

import io.ktor.client.*
import io.ktor.server.cio.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HttpClientConfiguration {
    @Bean
    fun httpClientBean(): HttpClient{
        return HttpClient()
    }
}