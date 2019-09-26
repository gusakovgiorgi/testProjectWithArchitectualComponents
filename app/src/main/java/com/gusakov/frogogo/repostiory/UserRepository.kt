package com.gusakov.frogogo.repostiory

import com.gusakov.frogogo.model.User
import com.gusakov.frogogo.repostiory.dto.ModifiableUserDto
import com.gusakov.frogogo.repostiory.dto.UserDto
import com.gusakov.frogogo.repostiory.mapper.NullableInputListMapper

interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
    suspend fun creteUser(
        firstName: String,
        lastName: String,
        email: String,
        avatarUrl: String
    ): Result<Boolean>

    suspend fun updateUser(user: User): Result<Boolean>
}

class UserRepositoryImpl(
    private val userApiService: UserApiService,
    private val userDataMapper: NullableInputListMapper<UserDto, User>
) : UserRepository {
    override suspend fun getUsers(): Result<List<User>> {
        val apiResult = userApiService.getUsersAsync()
        return if (apiResult.isSuccessful) {
            Result.Success(userDataMapper.map(apiResult.body()))
        } else {
            Result.Failure(ApiException(apiResult.errorBody()?.string() ?: ""))
        }
    }

    override suspend fun creteUser(
        firstName: String,
        lastName: String,
        email: String,
        avatarUrl: String
    ): Result<Boolean> {
        val apiResult =
            userApiService.createUserAsync(ModifiableUserDto(firstName, lastName, email, avatarUrl))
        return if (apiResult.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Failure(ApiException(apiResult.errorBody()?.string() ?: ""))
        }
    }

    override suspend fun updateUser(user: User): Result<Boolean> {
        val apiResult = userApiService.updateUserAsync(
            ModifiableUserDto(
                user.firstName,
                user.lastName,
                user.email,
                user.avatarUri.toString()
            )
        )
        return if (apiResult.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Failure(ApiException(apiResult.errorBody()?.string() ?: ""))
        }
    }
}