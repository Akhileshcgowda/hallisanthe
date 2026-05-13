package com.example.hallisanthe.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GeminiRequest(
    @SerializedName("contents") val contents: List<Content>,
    @SerializedName("systemInstruction") val systemInstruction: Content? = null
) {
    data class Content(
        @SerializedName("role") val role: String? = null,
        @SerializedName("parts") val parts: List<Part>
    ) {
        data class Part(
            @SerializedName("text") val text: String
        )
    }
}

data class GeminiResponse(
    @SerializedName("candidates") val candidates: List<Candidate>? = null,
    @SerializedName("error") val error: ApiError? = null
) {
    data class Candidate(
        @SerializedName("content") val content: Content? = null
    ) {
        data class Content(
            @SerializedName("parts") val parts: List<Part>? = null
        ) {
            data class Part(
                @SerializedName("text") val text: String? = null
            )
        }
    }

    data class ApiError(
        @SerializedName("code") val code: Int? = null,
        @SerializedName("message") val message: String? = null,
        @SerializedName("status") val status: String? = null
    )
}

data class GeminiGenerateDescriptionRequest(
    @SerializedName("contents") val contents: List<GeminiRequest.Content>
)
