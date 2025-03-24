package com.ntu.sc2006.hawkerlah

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RequestMapping

@SpringBootApplication
@RequestMapping("/api")
class HawkerLahServerApplication


fun main(args: Array<String>) {
    runApplication<HawkerLahServerApplication>(*args)
}
