package cr.ac.utn.census.ui.personlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.census.data.local.PersonEntity
import cr.ac.utn.census.databinding.ItemPersonBinding

class PersonListAdapter(
    private val onPersonSelected: (PersonEntity) -> Unit
) : ListAdapter<PersonEntity, PersonListAdapter.PersonViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = ItemPersonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PersonViewHolder(private val binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val person = binding.root.tag as? PersonEntity ?: return@setOnClickListener
                onPersonSelected(person)
            }
        }

        fun bind(person: PersonEntity) {
            binding.textPersonName.text = person.fullName
            binding.textPersonEmail.text = person.email
            binding.textPersonPhone.text = person.phone
            binding.root.tag = person
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<PersonEntity>() {
        override fun areItemsTheSame(oldItem: PersonEntity, newItem: PersonEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PersonEntity, newItem: PersonEntity): Boolean =
            oldItem == newItem
    }
}
