package xyz.blueowl.ispychallenge.ui.near_me.challenge

data class ChallengeItem(
    val id: String,
    val rating: Double,
    val wins: Int,
    val distance: Float,
    val description: String,
    val username: String
)