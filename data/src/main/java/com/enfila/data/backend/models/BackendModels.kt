package com.enfila.data.backend.models

import kotlinx.serialization.Serializable

// API Response wrapper
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null,
    val timestamp: Long = 0L
)

// User models
@Serializable
data class BackendUser(
    val id: String,
    val phone: String,
    val name: String? = null,
    val companyIds: List<String>? = null
)

@Serializable
data class CreateUserRequest(
    val phone: String,
    val name: String? = null,
    val companyIds: List<String>? = null
)

@Serializable
data class UpdateUserRequest(
    val name: String? = null,
    val companyIds: List<String>? = null
)

// Client models
@Serializable
data class BackendClient(
    val id: String,
    val name: String? = null,
    val shifts: List<String>? = null
)

@Serializable
data class CreateClientRequest(
    val id: String,
    val name: String? = null
)

@Serializable
data class UpdateClientRequest(
    val name: String? = null
)

// Shift models
@Serializable
enum class BackendShiftState {
    WAITING,
    CALLING,
    CANCELLED,
    FINISHED
}

@Serializable
data class BackendShift(
    val id: String,
    val date: Long,
    val parentCompanySite: String,
    val number: Int = 0,
    val contactId: String,
    val notes: String?,
    val state: BackendShiftState,
    val attentionStartDate: Long? = null,
    val endDate: Long? = null
)

@Serializable
data class CreateShiftRequest(
    val parentCompanySite: String,
    val number: Int,
    val contactId: String,
    val notes: String?
)

@Serializable
data class UpdateShiftRequest(
    val number: Int? = null,
    val notes: String? = null,
    val state: BackendShiftState? = null,
    val attentionStartDate: Long? = null,
    val endDate: Long? = null
)

@Serializable
data class AssignShiftRequest(
    val companySiteId: String,
    val contactId: String,
    val notes: String? = null
)

@Serializable
data class ShiftDetails(
    val shift: BackendShift,
    val client: BackendClient?
)

// Company Site models
@Serializable
data class BackendCompanySite(
    val id: String,
    val name: String? = null,
    val shiftsIdList: List<String>? = null
)

@Serializable
data class CreateCompanySiteRequest(
    val name: String
)

@Serializable
data class UpdateCompanySiteRequest(
    val name: String? = null
)

// Message models
@Serializable
data class SendMessageRequest(
    val to: String,
    val from: String,
    val body: String
)

@Serializable
data class MessageResponse(
    val success: Boolean,
    val messageId: String? = null,
    val error: String? = null
)

// Error response for HTTP error status codes
@Serializable
data class ErrorResponse(
    val error: String,
    val code: String? = null,
    val timestamp: Long = 0L
)
