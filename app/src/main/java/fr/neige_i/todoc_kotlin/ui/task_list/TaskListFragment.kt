package fr.neige_i.todoc_kotlin.ui.task_list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.neige_i.todoc_kotlin.R
import fr.neige_i.todoc_kotlin.databinding.FragmentTaskListBinding

@AndroidEntryPoint
class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val taskAdapter = TaskAdapter { projectId -> viewModel.onTaskRemoved(projectId) }

        setupUi(taskAdapter)
        listenToViewState(taskAdapter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return viewModel.onMenuItemClicked(item.itemId)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun setupUi(taskAdapter: TaskAdapter) {
        setHasOptionsMenu(true)

        binding.taskList.adapter = taskAdapter

        binding.addTaskFab.setOnClickListener {
            findNavController()
                .navigate(TaskListFragmentDirections.actionTaskListFragmentToTaskAddDialog())
        }
    }

    private fun listenToViewState(taskAdapter: TaskAdapter) {
        viewModel.viewState.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it.taskViewStates)
            binding.noTaskText.visibility = it.emptyTaskTextVisibility
        }
    }
}