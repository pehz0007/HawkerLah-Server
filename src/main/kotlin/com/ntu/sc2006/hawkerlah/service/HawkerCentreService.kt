package com.ntu.sc2006.hawkerlah.service

import com.ntu.sc2006.hawkerlah.controller.SupabaseBean
import com.ntu.sc2006.hawkerlah.model.HawkerCentre
import com.ntu.sc2006.hawkerlah.model.HawkerOwner
import com.ntu.sc2006.hawkerlah.model.HawkerStall
import com.ntu.sc2006.hawkerlah.utils.SUUID
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import org.springframework.stereotype.Service

@Service
class HawkerCentreService(
    val supabaseBean: SupabaseBean
) {

    suspend fun retrieveHawkerStall(userId: SUUID): HawkerStall {
        val client = supabaseBean.supabaseClient()
        return client.from("hawker_stall").select() {
            filter {
                eq("id", userId)
            }
        }.decodeSingle<HawkerStall>()
    }

    suspend fun retrieveHawkerCentres(): List<HawkerCentre> {
        val client = supabaseBean.supabaseClient()
        return client.from("hawker_centre").select().decodeList<HawkerCentre>()
    }

}