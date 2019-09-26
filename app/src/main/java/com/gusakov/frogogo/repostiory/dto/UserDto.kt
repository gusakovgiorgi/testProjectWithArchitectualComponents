package com.gusakov.frogogo.repostiory.dto

import com.google.gson.annotations.SerializedName

class UserDto(
    val id: Long,
    firstName: String,
    lastName: String,
    email: String,
    avatarUrl: String,
    @SerializedName(SERIALIZED_CREATED_AT)
    val createdAt: String,
    @SerializedName(SERIALIZED_UPDATED_AT)
    var updatedAt: String,
    var url: String
) : ModifiableUserDto(firstName, lastName, email, avatarUrl)