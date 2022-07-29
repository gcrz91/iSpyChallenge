package xyz.blueowl.ispychallenge.ui.near_me.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.blueowl.ispychallenge.R
import xyz.blueowl.ispychallenge.databinding.ItemChallengeBinding
import xyz.blueowl.ispychallenge.ui.near_me.challenge.ChallengeItem

class ChallengeAdapter(
    private val onChallengeClick: (String) -> Unit
) : ListAdapter<ChallengeItem, ChallengeAdapter.ChallengeViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        return ChallengeViewHolder(
            ItemChallengeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).also { viewHolder ->
            viewHolder.itemView.setOnClickListener {
                onChallengeClick(currentList[viewHolder.adapterPosition].id)
            }
        }
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ChallengeViewHolder(
        private val binding: ItemChallengeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(challenge: ChallengeItem) {
            val context = binding.root.context
            with(binding) {
                textStars.text =
                    context.getString(R.string.challenge_stars).format(challenge.rating)
                textChallenge.text = challenge.description
                textUsername.text = context.getString(R.string.challenge_by_username)
                    .format(challenge.username)
                textWins.text = context.getString(R.string.challenge_wins)
                    .format(challenge.wins.toString())
                textDistance.text = context.getString(R.string.challenge_miles).format(challenge.distance)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ChallengeItem>() {
        override fun areItemsTheSame(oldItem: ChallengeItem, newItem: ChallengeItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChallengeItem, newItem: ChallengeItem): Boolean {
            return oldItem == newItem
        }

    }
}