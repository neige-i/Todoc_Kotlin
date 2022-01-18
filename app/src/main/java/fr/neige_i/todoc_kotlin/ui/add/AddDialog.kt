package fr.neige_i.todoc_kotlin.ui.add

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
import fr.neige_i.todoc_kotlin.databinding.DialogAddBinding

@AndroidEntryPoint
class AddDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogAddBinding.inflate(layoutInflater, null, false)
        val viewModel: AddViewModel by viewModels()

        // Use the NavBackStackEntry as LifecycleOwner in a DialogFragment using Jetpack Navigation
        val backStackEntry = findNavController().getBackStackEntry(R.id.add_dialog)

        setupUi(binding, viewModel)
        listenToViewState(binding, viewModel, backStackEntry)
        listenToViewEvents(viewModel, backStackEntry)

        return initializeDialog(binding, viewModel)
    }

    /**
     * [DialogInterface.OnClickListener] automatically dismisses the dialog when the button is clicked.
     * This is not the desired behaviour. Instead, use [View.OnClickListener][android.view.View.OnClickListener].
     */
    private fun initializeDialog(binding: DialogAddBinding, viewModel: AddViewModel) =
        AlertDialog
            .Builder(context)
            .setTitle(R.string.add_task)
            .setView(binding.root)
            .setPositiveButton(R.string.add, null)
            .create().apply {
                setOnShowListener {
                    getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                        viewModel.onPositiveButtonClicked()
                    }
                }
            }

    private fun setupUi(binding: DialogAddBinding, viewModel: AddViewModel) {
        binding.taskNameInput.doAfterTextChanged { viewModel.afterTaskNameChanged(it?.toString()) }

        binding.projectNameAutocomplete.setOnItemClickListener { parent, _, position, _ ->
            viewModel.onProjectSelected(parent.getItemAtPosition(position) as Project)
        }
    }

    private fun listenToViewState(
        binding: DialogAddBinding,
        viewModel: AddViewModel,
        lifecycleOwner: LifecycleOwner,
    ) {
        viewModel.viewStateLiveData.observe(lifecycleOwner) { viewState ->

            binding.projectNameAutocomplete.setAdapter(ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                viewState.projectList
            ))

            binding.taskNameInputLayout.error = viewState.taskError
            binding.projectNameInputLayout.error = viewState.projectError
        }
    }

    private fun listenToViewEvents(viewModel: AddViewModel, lifecycleOwner: LifecycleOwner) {
        viewModel.dismissDialogLiveData.observe(lifecycleOwner) { dismiss() }
    }
}
