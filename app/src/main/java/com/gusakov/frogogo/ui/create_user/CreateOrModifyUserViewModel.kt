package com.gusakov.frogogo.ui.create_user

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gusakov.frogogo.R
import com.gusakov.frogogo.model.User
import com.gusakov.frogogo.repostiory.Result
import com.gusakov.frogogo.repostiory.UserRepository
import kotlinx.coroutines.launch

sealed class CreateUserState {
    object Loading : CreateUserState()
    object Success : CreateUserState()
    data class Error(
        @StringRes val firstNameError: Int? = null,
        @StringRes val lastNameError: Int? = null,
        @StringRes val emailError: Int? = null,
        val serverConnectionError: Boolean? = null
    ) : CreateUserState()
}

class CreateOrModifyUserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _createUserStateLiveData = MutableLiveData<CreateUserState>()

    val createUserStateLiveData: LiveData<CreateUserState> = _createUserStateLiveData


    fun createUser(name: String, surname: String, email: String) {
        when {
            name.isEmpty() -> _createUserStateLiveData.value =
                CreateUserState.Error(firstNameError = R.string.first_name_is_empty)
            surname.isEmpty() -> _createUserStateLiveData.value =
                CreateUserState.Error(lastNameError = R.string.surname_is_empty)
            !EmailValidator(email).isValid() -> _createUserStateLiveData.value =
                CreateUserState.Error(emailError = R.string.email_is_incorrect)
            else -> {
                viewModelScope.launch {
                    _createUserStateLiveData.value = CreateUserState.Loading
                    when (val apiResult = userRepository.creteUser(name, surname, email, "")) {
                        is Result.Success -> _createUserStateLiveData.value =
                            CreateUserState.Success
                        is Result.Failure -> _createUserStateLiveData.value =
                            CreateUserState.Error(serverConnectionError = true)
                    }
                }
            }
        }
    }

    fun updateUser(user: User) {
        when {
            user.firstName.isEmpty() -> _createUserStateLiveData.value =
                CreateUserState.Error(firstNameError = R.string.first_name_is_empty)
            user.lastName.isEmpty() -> _createUserStateLiveData.value =
                CreateUserState.Error(lastNameError = R.string.surname_is_empty)
            !EmailValidator(user.email).isValid() -> _createUserStateLiveData.value =
                CreateUserState.Error(emailError = R.string.email_is_incorrect)
            else -> {
                viewModelScope.launch {
                    _createUserStateLiveData.value = CreateUserState.Loading
                    when (val apiResult = userRepository.updateUser(user)) {
                        is Result.Success -> _createUserStateLiveData.value =
                            CreateUserState.Success
                        is Result.Failure -> _createUserStateLiveData.value =
                            CreateUserState.Error(serverConnectionError = true)
                    }
                }
            }
        }
    }
}
