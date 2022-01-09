package fr.neige_i.todoc_kotlin.ui.task_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.neige_i.todoc_kotlin.databinding.FragmentTaskListBinding

@AndroidEntryPoint
class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewModel: TaskListViewModel by viewModels()

        val taskAdapter = TaskAdapter { projectId -> viewModel.onTaskRemoved(projectId) }

        setupUi(taskAdapter)
        listenToViewState(viewModel, taskAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun setupUi(taskAdapter: TaskAdapter) {
        binding.taskList.adapter = taskAdapter

        binding.addTaskFab.setOnClickListener {
            findNavController()
                .navigate(TaskListFragmentDirections.actionTaskListFragmentToTaskAddDialog())
        }
    }

    private fun listenToViewState(viewModel: TaskListViewModel, taskAdapter: TaskAdapter) {
        viewModel.viewState.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it.taskViewStates)
            binding.noTaskText.visibility = if (it.isNoTaskTextVisible) View.VISIBLE else View.GONE
        }
    }
}