package fr.neige_i.todoc_kotlin.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import fr.neige_i.todoc_kotlin.databinding.DialogDetailBinding

@AndroidEntryPoint
class DetailBottomSheet : BottomSheetDialogFragment() {

    private var mutableBinding: DialogDetailBinding? = null
    private val binding: DialogDetailBinding get() = mutableBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mutableBinding = DialogDetailBinding.inflate(layoutInflater, container, false)
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

        mutableBinding = null
    }
}
