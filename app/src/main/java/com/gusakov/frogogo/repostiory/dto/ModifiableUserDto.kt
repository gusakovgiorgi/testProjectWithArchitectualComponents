package com.gusakov.frogogo.repostiory.dto

import com.google.gson.annotations.SerializedName

open class ModifiableUserDto(
    @SerializedName(SERIALIZED_FIRST_NAME)
    val firstName: String,
    @SerializedName(SERIALIZED_LAST_NAME)
    val lastName: String,
    val email: String,
    @SerializedName(SERIALIZED_AVATAR_URL)
    val avatarUrl: String
)