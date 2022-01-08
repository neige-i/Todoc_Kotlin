package fr.neige_i.todoc_kotlin.ui.task_add

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.neige_i.todoc_kotlin.R
import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.databinding.DialogAddTaskBinding

@AndroidEntryPoint
class TaskAddDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // TODO: is not setting binding to null mandatory for Garbage Collector
        val binding = DialogAddTaskBinding.inflate(layoutInflater, null, false)
        val viewModel: TaskAddViewModel by viewModels()

        // Use the NavBackStackEntry as LifecycleOwner in a DialogFragment using Jetpack Navigation
        // TODO: is question mark considered as "logic"
        val backStackEntry = findNavController().getBackStackEntry(R.id.taskAddDialog)

        setupUi(binding, viewModel)
        listenToViewState(binding, viewModel, backStackEntry)
        listenToViewEvents(viewModel, backStackEntry)

        return initializeDialog(binding, viewModel)
    }

    /**
     * [DialogInterface.OnClickListener] automatically dismisses the dialog when the button is clicked.
     * This is not the desired behaviour. Instead, use [View.OnClickListener][android.view.View.OnClickListener].
     */
    private fun initializeDialog(binding: DialogAddTaskBinding, viewModel: TaskAddViewModel) =
        AlertDialog
            .Builder(context)
            .setTitle(R.string.add_task)
            .setView(binding.root)
            .setPositiveButton(R.string.add, null)
            .create().also { alertDialog ->
                alertDialog.setOnShowListener {
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                        viewModel.onPositiveButtonClicked()
                    }
                }
            }

    private fun setupUi(binding: DialogAddTaskBinding, viewModel: TaskAddViewModel) {
        binding.taskNameInput.doAfterTextChanged { viewModel.afterTaskNameChanged(it.toString()) }

        binding.projectNameAutocomplete.setOnItemClickListener { parent, _, position, _ ->
            viewModel.onProjectSelected(parent.getItemAtPosition(position) as Project)
        }
    }

    private fun listenToViewState(
        binding: DialogAddTaskBinding,
        viewModel: TaskAddViewModel,
        lifecycleOwner: LifecycleOwner,
    ) {
        viewModel.viewState.observe(lifecycleOwner) { viewState ->

            binding.projectNameAutocomplete.setAdapter(ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                viewState.projectList
            ))

            binding.taskNameInputLayout.error = viewState.taskError
            binding.projectNameInputLayout.error = viewState.projectError
        }
    }

    private fun listenToViewEvents(viewModel: TaskAddViewModel, lifecycleOwner: LifecycleOwner) {
        viewModel.dismissDialogEvent.observe(lifecycleOwner) { dismiss() }
    }
}
