package fr.neige_i.todoc_kotlin.ui.task_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import fr.neige_i.todoc_kotlin.databinding.FragmentTaskListBinding

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
        binding.addTaskFab.setOnClickListener {
            findNavController()
                .navigate(TaskListFragmentDirections.actionTaskListFragmentToTaskAddDialog())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}