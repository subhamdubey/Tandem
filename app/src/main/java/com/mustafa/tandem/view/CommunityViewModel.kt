package com.mustafa.tandem.view

import androidx.lifecycle.*
import com.mustafa.tandem.repository.CommunityRepository
import com.mustafa.tandem.testing.OpenForTesting
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@OpenForTesting
class CommunityViewModel @Inject constructor(
    private val repository: CommunityRepository,
    private val dispatcherIO: CoroutineDispatcher
) :
    ViewModel() {

    private val pageLiveData: MutableLiveData<Int> = MutableLiveData()

    private var pageNumber = 1

    init {
        pageLiveData.postValue(1)
    }

    val membersListLiveData = pageLiveData.switchMap { pageNumber ->
        liveData(viewModelScope.coroutineContext + dispatcherIO) {
            val members = repository.getCommunityMembers(pageNumber).asLiveData()
            emitSource(members)
        }
    }

    fun loadMore() {
        pageNumber++
        pageLiveData.postValue(pageNumber)
    }

    fun refresh() {
        pageLiveData.value?.let {
            pageLiveData.value = it
        }
    }

}
