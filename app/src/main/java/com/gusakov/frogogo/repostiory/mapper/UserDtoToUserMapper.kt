package com.gusakov.frogogo.repostiory.mapper

import android.net.Uri
import com.gusakov.frogogo.model.User
import com.gusakov.frogogo.repostiory.dto.UserDto
import java.text.SimpleDateFormat
import java.util.*

class UserDtoToUserMapper : NullableInputListMapper<UserDto, User> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault())
    override fun map(input: List<UserDto>?): List<User> {
        return if (input == null) {
            emptyList()
        } else {
            val result = mutableListOf<User>()
            input.forEach {
                result.add(convert(it))
            }
            result
        }
    }

    fun convert(input: UserDto): User {
        val id = input.id
        val firstName = input.firstName
        val lastName = input.lastName
        val email = input.email
        val avatarUri = try {
            Uri.parse(input.avatarUrl)
        } catch (e: NullPointerException) {
            Uri.parse("")
        }

        val created = dateFormat.parse(input.createdAt)!!
        val updated = dateFormat.parse(input.updatedAt)!!
        val url = Uri.parse(input.url)
        return User(id, firstName, lastName, email, avatarUri, created, updated, url)
    }
}