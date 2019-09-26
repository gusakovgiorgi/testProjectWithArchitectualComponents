package com.gusakov.frogogo.repostiory

import com.gusakov.frogogo.repostiory.dto.ModifiableUserDto
import com.gusakov.frogogo.repostiory.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserApiService {
    @GET("users.json")
    suspend fun getUsersAsync(): Response<List<UserDto>>

    @POST("users.json")
    suspend fun createUserAsync(@Body modifiableUserDto: ModifiableUserDto): Response<Void>

    @PATCH("users.json")
    suspend fun updateUserAsync(@Body modifiableUserDto: ModifiableUserDto): Response<Void>
}