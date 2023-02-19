package com.dvinov.recipes.screens.fullscreen_image

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.dvinov.recipes.databinding.FragmentImageFullscreenBinding

class ImageFullscreenFragment : Fragment() {
    private var _binding: FragmentImageFullscreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageFullscreenBinding.inflate(inflater, container, false)
        val view = binding.root

        val url = arguments?.getString("url")

        val circularProgressDrawable = CircularProgressDrawable(view.context)
        circularProgressDrawable.strokeWidth = 3f
        circularProgressDrawable.centerRadius = 50f
        circularProgressDrawable.start()

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        Glide
            .with(view.context)
            .load(url)
            .centerInside()
            .skipMemoryCache(true)
            .placeholder(circularProgressDrawable)
            .into(binding.imageView)

        return view
    }
}