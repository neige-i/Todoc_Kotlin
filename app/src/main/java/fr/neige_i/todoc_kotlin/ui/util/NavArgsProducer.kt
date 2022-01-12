package fr.neige_i.todoc_kotlin.ui.util

import androidx.collection.ArrayMap
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.reflect.Method
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * Created for testing purpose.
 */
class NavArgsProducer @Inject constructor(private val savedStateHandle: SavedStateHandle) {

    companion object {
        // Saves references to reflected Methods, because reflection can be costly
        private val methodMap = ArrayMap<String, Method>()
    }

    /**
     * Inspired from [androidx.navigation.NavArgsLazy]. Should its implementation change,
     * this method should change accordingly.
     */
    fun <T : NavArgs> getNavArgs(navArgsClass: KClass<T>): T {
        val method = methodMap.getOrPut(navArgsClass.simpleName) {
            navArgsClass.java.getMethod("fromSavedStateHandle", SavedStateHandle::class.java)
        }

        @Suppress("UNCHECKED_CAST")
        return method.invoke(null, savedStateHandle) as T
    }
}