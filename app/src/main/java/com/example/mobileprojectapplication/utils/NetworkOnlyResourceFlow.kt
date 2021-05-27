package com.example.mobileprojectapplication.utils

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.example.mobileprojectapplication.models.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import retrofit2.Response




@ExperimentalCoroutinesApi
abstract class NetworkOnlyResourceFlow<RESULT> {

    fun asFlow() = flow<State<RESULT>> {
        emit(State.loading())
        val apiResponse = createCall()
        if (apiResponse.isSuccessful){
            processResponse(apiResponse)?.let {  emit(State.success(it)) }
        } else {
            onFetchFailed()
            emit(State.error(apiResponse.message()))
        }
    }.catch { e ->
        // Exception occurred! Emit error
        emit(State.error("Network error"))
        e.printStackTrace()
    }
    protected open fun onFetchFailed() {
    }

    @WorkerThread
    protected open fun processResponse(response: Response<RESULT>) = response.body()

    @MainThread
    protected abstract suspend fun createCall(): Response<RESULT>
}