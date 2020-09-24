package com.example.tvshows.ui.showslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tvshows.dagger.MyApplication
import com.example.tvshows.databinding.FragmentShowDetailsBinding
import javax.inject.Inject

class ShowDetailsFragment : Fragment() {

    private var binding: FragmentShowDetailsBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ShowsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        setUpViewModel()
        return binding.root
    }

    private fun setUpViewModel() {

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}