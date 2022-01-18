package fr.neige_i.todoc_kotlin.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.time.Clock
import java.time.ZoneId
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoCoroutineDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainCoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object FrameworkProvidingModule {

    @Singleton
    @Provides
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Singleton
    @IoCoroutineDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @MainCoroutineDispatcher
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Singleton
    @Provides
    fun provideDefaultClock(): Clock = Clock.systemDefaultZone()

    @Singleton
    @Provides
    fun provideDefaultZoneId(): ZoneId = ZoneId.systemDefault()
}