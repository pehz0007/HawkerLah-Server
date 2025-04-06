package com.ntu.sc2006.hawkerlah.controller

import com.ntu.sc2006.hawkerlah.service.*
import com.ntu.sc2006.hawkerlah.utils.SUUID
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/common")
class CommonController(
    private val commonService: CommonService,
    private val hawkerCentreService: HawkerCentreService
) {

    @GetMapping("/hawkers-stall")
    suspend fun getHawkerStall(hawkerStallId: SUUID): ResponseEntity<String> {
        val hawkerStall = hawkerCentreService.retrieveHawkerStall(hawkerStallId)
        return SuccessResult(hawkerStall).toResponseEntity()
    }

    @PostMapping("/profile-image", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun updateProfileImage(
        @RequestParam("file") file: MultipartFile,
        authentication: Authentication
    ): ResponseEntity<String> {
        return try {
            val imageBytes = file.bytes
            val userId = authentication.name
            commonService.updateProfileImage(userId, imageBytes, userId)
            SuccessResult("Profile image updated successfully").toResponseEntity()
        } catch (e: Exception) {
            GenericErrorResult("Failed to update profile image: ${e.message}").toResponseEntity()
        }
    }
}
