package fr.neige_i.todoc_kotlin.util

import androidx.lifecycle.LiveData

object TestLifecycleRule {
    fun <T> getValueForTesting(liveData: LiveData<T>): T? {
        liveData.observeForever { }
        return liveData.value
    }

    fun <T> isLiveDataTriggered(liveData: LiveData<T>): Boolean {
        var isCalled = false
        liveData.observeForever {
            isCalled = true
        }
        return isCalled
    }
}