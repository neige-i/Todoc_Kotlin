package fr.neige_i.todoc_kotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import fr.neige_i.todoc_kotlin.domain.AddNewTaskUseCase
import fr.neige_i.todoc_kotlin.domain.AddNewTaskUseCaseImpl
import fr.neige_i.todoc_kotlin.domain.GetAllProjectsUseCase
import fr.neige_i.todoc_kotlin.domain.GetAllProjectsUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class BusinessBindingModule {

    @Binds
    abstract fun bindGetAllProjectUseCase(
        getAllProjectsUseCaseImpl: GetAllProjectsUseCaseImpl,
    ): GetAllProjectsUseCase

    @Binds
    abstract fun bindAddNewTaskUseCase(
        addNewTaskUseCaseImpl: AddNewTaskUseCaseImpl,
    ): AddNewTaskUseCase
}