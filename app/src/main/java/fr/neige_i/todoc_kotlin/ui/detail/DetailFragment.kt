package fr.neige_i.todoc_kotlin.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import fr.neige_i.todoc_kotlin.databinding.FragmentDetailBinding

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ViewModelProvider(this)
            .get(DetailViewModel::class.java)
            .viewState
            .observe(viewLifecycleOwner) {
                binding.taskIdValue.text = it.id
                binding.taskNameValue.text = it.task
                binding.projectNameValue.text = it.project
                binding.creationDateValue.text = it.date
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
