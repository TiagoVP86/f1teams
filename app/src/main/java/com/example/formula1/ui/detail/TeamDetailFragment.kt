package com.example.formula1.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.formula1.appContainer
import com.example.formula1.databinding.FragmentTeamDetailBinding
import com.example.formula1.ui.common.viewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class TeamDetailFragment : Fragment() {

    private var _binding: FragmentTeamDetailBinding? = null
    private val binding get() = _binding!!

    private val args: TeamDetailFragmentArgs by navArgs()

    private val viewModel: TeamDetailViewModel by viewModels {
        viewModelFactory {
            TeamDetailViewModel(args.teamId, requireContext().appContainer.driverRepository)
        }
    }

    private val adapter = DriversAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.teamName.text = args.teamName.trim()
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { render(it) }
            }
        }
    }

    private fun render(state: DetailUiState) {
        adapter.submitList(state.drivers)
        binding.swipeRefresh.isRefreshing = state.loading && state.drivers.isNotEmpty()
        binding.progress.visibility =
            if (state.loading && state.drivers.isEmpty()) View.VISIBLE else View.GONE

        state.standing?.let { s ->
            binding.teamName.text = s.teamName
            binding.season.text = getString(com.example.formula1.R.string.label_season) + " " + s.season
            binding.points.text = s.points.toString()
            binding.position.text = "P${s.position}"
            binding.wins.text = s.wins.toString()
        }

        if (state.error && state.drivers.isEmpty()) {
            Snackbar.make(binding.root, com.example.formula1.R.string.error_detail, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recycler.adapter = null
        _binding = null
    }
}
