package xyz.blueowl.ispychallenge.ui.near_me

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import xyz.blueowl.ispychallenge.data.repository.DataRepository
import xyz.blueowl.ispychallenge.databinding.FragmentNearMeBinding
import xyz.blueowl.ispychallenge.extensions.requireISpyApplication
import xyz.blueowl.ispychallenge.ui.near_me.adapters.ChallengeAdapter
import java.lang.IllegalArgumentException

class NearMeFragment : Fragment() {

    private var _binding: FragmentNearMeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by viewModels<NearMeViewModel> {
        Factory(requireISpyApplication().dataRepository)
    }
    private val challengeAdapter by lazy {
        ChallengeAdapter(this::onChallengeSelected)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNearMeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            recyclerViewNearMe.adapter = challengeAdapter
            viewModel.state.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is NearMeViewModel.UiState.Loading -> {
                        progressCircular.isVisible = true
                    }
                    is NearMeViewModel.UiState.Success -> {
                        progressCircular.isVisible = false
                        challengeAdapter.submitList(state.data)
                    }
                    is NearMeViewModel.UiState.Error -> {
                        progressCircular.isVisible = false
                        Toast.makeText(requireContext(), state.stringRes, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun onChallengeSelected(id: String) {
        findNavController().navigate(
            NearMeFragmentDirections
                .actionNavigationFragmentNearMeToNavigationNearMeChallengeDetails(id)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class Factory(
        private val dataRepository: DataRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NearMeViewModel::class.java)) {
                return NearMeViewModel(dataRepository) as T
            }
            throw IllegalArgumentException("Cannot create instance of viewmodel.")
        }
    }
}