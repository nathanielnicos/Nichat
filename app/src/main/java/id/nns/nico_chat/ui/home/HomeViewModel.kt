package id.nns.nico_chat.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import id.nns.nico_chat.utils.NichatRepository
import id.nns.nico_chat.data.entity.UserEntity

class HomeViewModel(private val nichatRepository: NichatRepository) : ViewModel() {
    fun getAllUser(): LiveData<PagingData<UserEntity>> {
        return nichatRepository.getAllUser().cachedIn(viewModelScope)
    }
}
