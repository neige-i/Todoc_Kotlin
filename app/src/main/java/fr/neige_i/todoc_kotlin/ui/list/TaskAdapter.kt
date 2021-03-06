package fr.neige_i.todoc_kotlin.ui.list

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.neige_i.todoc_kotlin.databinding.ItemTaskBinding

class TaskAdapter(private val callback: ItemCallback) :
    ListAdapter<TaskViewState, TaskAdapter.TaskVIewHolder>(TaskDiffUtil()) {

    inner class TaskVIewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TaskViewState) {
            ImageViewCompat
                .setImageTintList(binding.projectImg, ColorStateList.valueOf(item.projectColor))

            binding.taskNameText.text = item.taskName
            binding.projectNameText.text = item.projectName

            binding.root.setOnClickListener { callback.onItemClick(item.taskId) }
            binding.deleteImg.setOnClickListener { callback.onDelete(item.taskId) }
        }
    }

    class TaskDiffUtil : DiffUtil.ItemCallback<TaskViewState>() {
        override fun areItemsTheSame(oldItem: TaskViewState, newItem: TaskViewState): Boolean =
            oldItem.taskId == newItem.taskId

        override fun areContentsTheSame(oldItem: TaskViewState, newItem: TaskViewState): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaskVIewHolder(
        ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TaskVIewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface ItemCallback {
        fun onDelete(taskId: Long)
        fun onItemClick(taskId: Long)
    }
}