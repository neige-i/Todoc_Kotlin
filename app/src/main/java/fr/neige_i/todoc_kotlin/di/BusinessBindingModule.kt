package fr.neige_i.todoc_kotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import fr.neige_i.todoc_kotlin.domain.*

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

    @Binds
    abstract fun bindGetTasksWithProjectsUseCase(
        getTasksWithProjectsUseCaseImpl: GetAllTasksWithProjectsUseCaseImpl,
    ): GetAllTasksWithProjectsUseCase

    @Binds
    abstract fun bindDeleteTaskUseCase(
        deleteTaskUseCaseImpl: DeleteTaskUseCaseImpl,
    ): DeleteTaskUseCase

    @Binds
    abstract fun bindGetSingleTaskWithProjectUseCase(
        getSingleTaskWithProjectUseCaseImpl: GetSingleTaskWithProjectUseCaseImpl,
    ): GetSingleTaskWithProjectUseCase
}
