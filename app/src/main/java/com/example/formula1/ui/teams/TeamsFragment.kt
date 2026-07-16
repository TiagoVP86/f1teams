package com.example.formula1.ui.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.formula1.R
import com.example.formula1.appContainer
import com.example.formula1.databinding.FragmentTeamsBinding
import com.example.formula1.domain.model.Team
import com.example.formula1.ui.common.TeamsEvent
import com.example.formula1.ui.common.viewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class TeamsFragment : Fragment() {

    private var _binding: FragmentTeamsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TeamsViewModel by viewModels {
        viewModelFactory {
            val container = requireContext().appContainer
            TeamsViewModel(container.teamRepository, container.driverRepository)
        }
    }

    private lateinit var adapter: TeamsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TeamsAdapter(
            onClick = ::openDetail,
            onFavorite = viewModel::onFavoriteClick
        )
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
        binding.retry.setOnClickListener { viewModel.refresh() }

        observeState()
        observeEvents()
    }

    private fun openDetail(team: Team) {
        val action = TeamsFragmentDirections
            .actionTeamsToDetail(teamId = team.teamId, teamName = team.teamName)
        findNavController().navigate(action)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    adapter.submitList(state.teams)
                    binding.swipeRefresh.isRefreshing = state.loading && state.teams.isNotEmpty()
                    binding.progress.visibility =
                        if (state.loading && state.teams.isEmpty()) View.VISIBLE else View.GONE
                    binding.emptyState.visibility =
                        if (state.errorEmpty && state.teams.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is TeamsEvent.FavoriteChanged -> {
                            val msg = if (event.nowFavorite) R.string.favorite_added
                            else R.string.favorite_removed
                            Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
                        }
                        TeamsEvent.RefreshError ->
                            Snackbar.make(binding.root, R.string.error_detail, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recycler.adapter = null
        _binding = null
    }
}
