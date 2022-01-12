package fr.neige_i.todoc_kotlin.util

import androidx.lifecycle.LiveData

object TestLifecycleRule {
    fun <T> getValueForTesting(liveData: LiveData<T>): T? {
        liveData.observeForever { }
        return liveData.value
    }

    fun <T> getLiveDataTriggerCount(liveData: LiveData<T>): Int {
        var called = 0
        liveData.observeForever {
            called++
        }
        return called
    }
}