package com.ntu.sc2006.hawkerlah.service

import com.ntu.sc2006.hawkerlah.controller.SupabaseBean
import com.ntu.sc2006.hawkerlah.model.HawkerCentre
import io.github.jan.supabase.postgrest.from
import org.springframework.stereotype.Service

@Service
class HawkerCentreService(
    val supabaseBean: SupabaseBean
) {

    suspend fun retrieveHawkerCentres(): List<HawkerCentre> {
        val client = supabaseBean.supabaseClient()
        return client.from("hawker_centre").select().decodeList<HawkerCentre>()
    }

}