package xyz.blueowl.ispychallenge.ui.new_challenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewChallengeViewModel() : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is New Challenge Fragment"
    }
    val text: LiveData<String> = _text
}