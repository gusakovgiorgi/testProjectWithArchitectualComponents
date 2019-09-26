package com.gusakov.frogogo.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gusakov.frogogo.model.User
import com.gusakov.frogogo.repostiory.Result
import com.gusakov.frogogo.repostiory.UserRepository
import kotlinx.coroutines.launch

sealed class UsersState {
    object Loading : UsersState()
    data class Success(val users: List<User>) : UsersState()
    object Error : UsersState()
}

class UsersViewModel(private val usersRepository: UserRepository) : ViewModel() {
    private val _usersStateLiveData = MutableLiveData<UsersState>()

    val usersStateLiveData: LiveData<UsersState> = _usersStateLiveData

    fun loadUsers() {
        viewModelScope.launch {
            _usersStateLiveData.value = UsersState.Loading
            when (val result = usersRepository.getUsers()) {
                is Result.Success -> _usersStateLiveData.value = UsersState.Success(result.value)
                else -> _usersStateLiveData.value = UsersState.Error
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
