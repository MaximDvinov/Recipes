package com.dvinov.recipes.screens.recipes

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.dvinov.recipes.databinding.FragmentInfoBinding
import com.dvinov.recipes.screens.recipes.models.Recipe

class InformationDialog : DialogFragment() {
    private lateinit var binding: FragmentInfoBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater

        binding = FragmentInfoBinding.inflate(inflater)
        val view = binding.root
        builder.setView(view)

        val bundle = arguments

        val value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getSerializable("recipe", Recipe::class.java)
        } else {
            bundle?.getSerializable("recipe") as Recipe
        }

        binding.apply {
            if (!value?.description.isNullOrBlank()) {
                descriprion.text = value?.description ?: ""
            } else {
                descriprion.visibility = View.GONE
            }
            if (!value?.carbos.isNullOrBlank()) {
                carbos.text = value?.carbos ?: ""
            } else {
                carbosLayout.visibility = View.GONE
            }
            if (!value?.fats.isNullOrBlank()) {
                fats.text = value?.fats ?: ""
            } else {
                fatsLayout.visibility = View.GONE
            }
            if (!value?.proteins.isNullOrBlank()) {
                proteins.text = value?.proteins ?: ""
            } else {
                proteinsLayout.visibility = View.GONE
            }

        }

        return builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view: View = binding.root

        // скрываем стандартный фон диалога
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }

        return view
    }
}