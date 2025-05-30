package com.ntu.sc2006.hawkerlah.service

import com.ntu.sc2006.hawkerlah.controller.SupabaseBean
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import org.springframework.stereotype.Service

@Service
class CommonService(
    val supabaseBean: SupabaseBean
) {

    suspend fun updateProfileImage(userId: String, imageBytes: ByteArray, fileName: String): String {
        val client = supabaseBean.supabaseClient()

        return try {
            val bucketName = "profile_photo"
            val filePath = "users/$fileName.jpg"

            client.storage.from(bucketName).upload(
                path = filePath,
                data = imageBytes,
            ) {
                upsert = true
            }

            val publicUrl = client.storage.from(bucketName).publicUrl(filePath)
            println("Image uploaded successfully: $publicUrl")
            setProfileImageUrl(userId, publicUrl)
            publicUrl
        } catch (e: Exception) {
            println("Failed to upload image: ${e.message}")
            throw e
        }
    }

    suspend fun setProfileImageUrl(userId: String, url: String) {
        val client = supabaseBean.supabaseClient()
        client
            .from("profiles")
            .update(mapOf("profile_image_url" to url)) {
                filter {
                    eq("id", userId)
                }
            }
    }


}