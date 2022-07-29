package xyz.blueowl.ispychallenge.ui.near_me

import android.location.Location
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.blueowl.ispychallenge.data.models.Challenge
import xyz.blueowl.ispychallenge.data.models.Rating
import xyz.blueowl.ispychallenge.data.repository.DataRepository
import xyz.blueowl.ispychallenge.ui.near_me.challenge.ChallengeItem

class NearMeViewModel(
    private val dataRepository: DataRepository
) : ViewModel() {

    private val _state = MutableLiveData<UiState>()
    val state: LiveData<UiState> get() = _state

    init {
        getChallenges()
    }

    fun getChallenges() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            delay(5000L)
            val response = dataRepository.getAllChallenges()
            if (response.isEmpty()) {
                _state.value = UiState.Error(0)
            } else {
                val challengeItems = mapToChallengeItemList(response)
                _state.value = UiState.Success(challengeItems)
            }
        }
    }

    private fun mapToChallengeItemList(challenges: List<Challenge>): List<ChallengeItem> {
        return challenges
            .map { challenge -> challengeToChallengeItem(challenge) }
            .sortedBy { it.distance }
    }

    private fun challengeToChallengeItem(challenge: Challenge): ChallengeItem {
        val user = dataRepository.getUserByUserId(challenge.userId)
        return ChallengeItem(
            id = challenge.id,
            rating = getAverageRating(challenge.ratings),
            distance = getDistance(challenge.latitude, challenge.longitude),
            wins = challenge.matches.map { it.verified }.size,
            description = challenge.hint,
            username = user?.username ?: "n/a"
        )
    }

    private fun getAverageRating(ratings: List<Rating>): Double {
        val sum = ratings.sumOf { it.stars }.toDouble()
        return sum.div(ratings.size)
    }

    private fun getDistance(lat: Double, long: Double): Float {
        val distanceArray = FloatArray(2)
        Location.distanceBetween(lat, long, 37.7904356, -122.403455, distanceArray)
        return distanceArray[0]
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val data: List<ChallengeItem>) : UiState()
        data class Error(@StringRes val stringRes: Int) : UiState()
    }
}