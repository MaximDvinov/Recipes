package com.dvinov.recipes.screens.add_recipe

import android.R
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import com.dvinov.recipes.databinding.FragmentAddRecipeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileOutputStream

enum class Difficulty(val index: Int, val title: String) {
    EASY(0, "easy"), MIDDLE(2, "middle"), HARD(3, "hard");

    override fun toString(): String {
        return this.title
    }
}

val difficultyList = listOf(Difficulty.EASY, Difficulty.MIDDLE, Difficulty.HARD)

@AndroidEntryPoint
class AddRecipeFragment : Fragment() {
    private var _binding: FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddRecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRecipeBinding.inflate(inflater, container, false)
        val view = binding.root

        initImageView()
        initDifficultyLayout()

        binding.apply {

            name.editText?.setOnChangeText {
                viewModel.setName(it)
                if (name.editText?.text?.isNotBlank() == true) {
                    name.error = ""
                } else {
                    name.error = "Enter a name"
                }
            }
            headline.editText?.setOnChangeText(viewModel::setHeadline)
            description.editText?.setOnChangeText(viewModel::setDescription)
            calories.editText?.setOnChangeText(viewModel::setCalories)
            proteins.editText?.setOnChangeText(viewModel::setProteins)
            carbos.editText?.setOnChangeText(viewModel::setCarbos)
            fats.editText?.setOnChangeText(viewModel::setFats)
            time.editText?.setOnChangeText(viewModel::setTime)

            add.setOnClickListener {
                if (name.editText?.text?.isNotBlank() == true) {
                    viewModel.addRecipe()
                    findNavController().popBackStack()
                } else {
                    name.error = "Enter a name"
                }

            }

            back.setOnClickListener {
                findNavController().popBackStack()
            }

        }

        return view
    }

    // метод с логикой работы меню выбора сложности
    private fun initDifficultyLayout() {
        val adapter: ArrayAdapter<Difficulty> = ArrayAdapter<Difficulty>(
            binding.root.context,
            R.layout.simple_dropdown_item_1line,
            difficultyList
        )

        val difficulty = binding.difficulty.editText as AutoCompleteTextView
        difficulty.setAdapter(adapter)
        difficulty.setText(difficultyList.first().title, false)

        difficulty.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    viewModel.setDifficulty(difficultyList[position].index)
                }
                1 -> {
                    viewModel.setDifficulty(difficultyList[position].index)
                }
                else -> {
                    viewModel.setDifficulty(difficultyList[position].index)
                }
            }
        }
    }

    // метод с логикой работы с изображением (добавление, демонстрация)
    private fun initImageView() {

        val path = context?.getExternalFilesDir(null)?.path

        val launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK
                && result.data != null
            ) {
                val photoUri: Uri? = result.data!!.data

                if (photoUri != null) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bitmap = if (Build.VERSION.SDK_INT < 28) {
                            MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver,
                                photoUri
                            )
                        } else {
                            val source = ImageDecoder.createSource(
                                requireContext().contentResolver,
                                photoUri
                            )
                            ImageDecoder.decodeBitmap(source)
                        }


                        val pathCurrent = "${path}/${System.currentTimeMillis()}"

                        val outputStream = FileOutputStream(pathCurrent)

                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

                        outputStream.flush()
                        outputStream.close()

                        viewModel.setImageUrl(pathCurrent)
                    }
                }

                viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                    viewModel.recipe.collect {
                        Glide
                            .with(binding.root)
                            .load(it.image)
                            .centerCrop()
                            .into(binding.imageView)
                    }
                }

                binding.imageView.visibility = View.VISIBLE
            }
        }

        binding.addImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            launcher.launch(intent)
        }
    }
}


// extensions - функция, для более удобной работы с вводом пользователя
fun EditText.setOnChangeText(onChange: (text: String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {

        }

        override fun onTextChanged(
            s: CharSequence, start: Int,
            before: Int, count: Int
        ) {
            onChange(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {

        }
    })
}