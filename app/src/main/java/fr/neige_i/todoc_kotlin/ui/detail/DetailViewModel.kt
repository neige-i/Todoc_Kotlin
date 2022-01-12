package fr.neige_i.todoc_kotlin.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.neige_i.todoc_kotlin.domain.GetSingleTaskWithProjectUseCase
import fr.neige_i.todoc_kotlin.ui.util.NavArgsProducer
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    navArgsProducer: NavArgsProducer,
    getSingleTaskWithProjectUseCase: GetSingleTaskWithProjectUseCase,
    defaultZoneId: ZoneId,
) : ViewModel() {

    private val dateTimeFormatter = DateTimeFormatter
        .ofPattern("dd/MM/yyyy HH:mm:ss")
        .withZone(defaultZoneId)

    private val args = navArgsProducer.getNavArgs(DetailBottomSheetArgs::class)
    private val taskWithProjectLiveData = getSingleTaskWithProjectUseCase(args.taskId).asLiveData()

    val viewState: LiveData<DetailViewState> = Transformations.map(taskWithProjectLiveData) {
        val (task, project) = it

        DetailViewState(
            task.id.toString(),
            task.name,
            project.name,
            dateTimeFormatter.format(Instant.ofEpochMilli(task.creationTimestamp)),
        )
    }
}
