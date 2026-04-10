package id.nns.nico_chat.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import id.nns.nico_chat.data.entity.UserEntity
import id.nns.nico_chat.databinding.ItemUserBinding

class HomeAdapter : PagingDataAdapter<UserEntity, HomeAdapter.HomeViewHolder>(DIFF_CALLBACK) {
    inner class HomeViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserEntity) {
            binding.civUserPhoto.load(user.photoUrl)
            binding.tvUserName.text = user.name
            binding.tvLatestMessage.text = "Hello World!"
            binding.tvLatestTime.text = "16:43"

//            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
//            val outputFormat = SimpleDateFormat("dd MMMM yyyy\nHH:mm:ss", Locale.getDefault())
//            val oldDateAndTime = inputFormat.parse(user.latestTime)
//            if (oldDateAndTime != null) {
//                val newDateAndTime = outputFormat.format(oldDateAndTime)
//                binding.tvLatestTime.text = newDateAndTime
//            }

            binding.cvUser.setOnClickListener {
//                val intent = Intent(itemView.context, ChatActivity::class.java)
//                intent.putExtra(ChatActivity.KEY_CHAT, user)
//
//                val optionsCompat: ActivityOptionsCompat =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        itemView.context as Activity,
//                        Pair(binding.civUserPhoto, "image"),
//                        Pair(binding.tvUserName, "name")
//                    )
//
//                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserEntity>() {
            override fun areItemsTheSame(
                oldItem: UserEntity,
                newItem: UserEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UserEntity,
                newItem: UserEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
