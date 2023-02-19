package com.dvinov.recipes.screens.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dvinov.recipes.R
import com.dvinov.recipes.databinding.FragmentRecipesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val view = binding.root

        val dialog = InformationDialog()
        val bundle = Bundle()

        val layoutManager = LinearLayoutManager(binding.root.context)
        val adapterList = RecipesRecyclerListAdapter(onImageClick = {
            view.findNavController().navigate(R.id.imageFullscreenFragment, bundleOf("url" to it))
        }) {
            bundle.putSerializable("recipe", it)
            dialog.arguments = bundle
            dialog.show(childFragmentManager, "info dialog")
        }

        binding.recycler.apply {
            this.layoutManager = layoutManager
            adapter = adapterList
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.recipeList.collect {
                adapterList.submitList(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.syncState.collect {
                binding.progress.visibility = when (it) {
                    SynchronizationStatus.SUCCESS -> View.GONE
                    SynchronizationStatus.ERROR -> {
                        Toast.makeText(
                            context,
                            "An error occurred while loading the data",
                            Toast.LENGTH_SHORT
                        ).show()
                        View.GONE
                    }
                    SynchronizationStatus.SYNCING -> View.VISIBLE
                }
            }
        }

        binding.addRecipe.setOnClickListener {
            findNavController().navigate(R.id.addRecipeFragment)
        }

        return view
    }
}