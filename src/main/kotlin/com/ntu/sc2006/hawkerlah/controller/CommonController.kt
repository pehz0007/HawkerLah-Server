package com.ntu.sc2006.hawkerlah.controller

import com.ntu.sc2006.hawkerlah.service.CommonService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/common")
class CommonController(
    private val commonService: CommonService
) {

    @PostMapping("/profile-image", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun updateProfileImage(@RequestParam("file") file: MultipartFile, authentication: Authentication): ResponseEntity<String> {
        return try {
            val imageBytes = file.bytes
            val userId = authentication.name
            commonService.updateProfileImage(userId, imageBytes, userId)
            ResponseEntity.ok("Profile image updated successfully")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to update profile image: ${e.message}")
        }
    }
}
