package fr.neige_i.todoc_kotlin.ui.list

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.neige_i.todoc_kotlin.R
import fr.neige_i.todoc_kotlin.databinding.FragmentListBinding

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var mutableBinding: FragmentListBinding? = null
    private val binding get() = mutableBinding!!
    private val viewModel: ListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mutableBinding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = TaskAdapter(object : TaskAdapter.ItemCallback {
            override fun onDelete(taskId: Long) {
                viewModel.onTaskRemoved(taskId)
            }

            override fun onItemClick(taskId: Long) {
                findNavController().navigate(ListFragmentDirections.actionListToDetail(taskId))
            }
        })

        setupUi(adapter)
        listenToViewState(adapter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return viewModel.onMenuItemClicked(item.itemId)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mutableBinding = null
    }

    private fun setupUi(taskAdapter: TaskAdapter) {
        setHasOptionsMenu(true)

        binding.taskList.adapter = taskAdapter

        binding.addTaskFab.setOnClickListener {
            findNavController().navigate(ListFragmentDirections.actionListToAdd())
        }
    }

    private fun listenToViewState(taskAdapter: TaskAdapter) {
        viewModel.viewState.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it.taskViewStates)
            binding.noTaskText.isVisible = it.emptyTaskTextVisibility
        }
    }
}