package com.ntu.sc2006.hawkerlah.controller

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "supabase")
data class SupabaseProperties(
    var url: String = "",
    var anonKey: String = "",
    var serviceRole: String = "",
    var jwtSecret: String = "",
)

@Configuration
@EnableConfigurationProperties(SupabaseProperties::class)
class SupabaseConfiguration {

    @Bean
    fun supabaseBean(properties: SupabaseProperties): SupabaseBean {
        return SupabaseBean(
            supabaseUrl = properties.url,
            supabaseAnonKey = properties.anonKey,
            supabaseServiceRole = properties.serviceRole,
            supabaseJwtSecret = properties.jwtSecret,
        )
    }
}

class SupabaseBean(
    private val supabaseUrl: String,
    private val supabaseAnonKey: String,
    private val supabaseServiceRole: String,
    private val supabaseJwtSecret: String,
) {
    fun supabaseClient() = createSupabaseClient(
        supabaseUrl = supabaseUrl,
        supabaseKey = supabaseServiceRole,
    ) {

        install(Postgrest) {
        }
    }
}