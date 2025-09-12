package com.ingjuanocampo.enfila.backend.routes

import com.ingjuanocampo.enfila.backend.services.MigrationService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.migrationRoutes() {
    val migrationService by inject<MigrationService>()
    
    route("/migration") {
        // Migrate data from Firebase
        post("/from-firebase") {
            try {
                migrationService.migrateFromFirebase()
                call.respond(HttpStatusCode.OK, mapOf(
                    "success" to true,
                    "message" to "Migration completed successfully",
                    "timestamp" to System.currentTimeMillis()
                ))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf(
                    "success" to false,
                    "error" to (e.message ?: "Migration failed"),
                    "timestamp" to System.currentTimeMillis()
                ))
            }
        }
        
        // Check migration status
        get("/status") {
            val status = migrationService.getMigrationStatus()
            call.respond(status)
        }
    }
}
