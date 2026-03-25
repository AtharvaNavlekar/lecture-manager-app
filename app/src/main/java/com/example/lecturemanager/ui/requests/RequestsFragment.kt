package com.example.lecturemanager.ui.requests

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.lecturemanager.R
import com.example.lecturemanager.databinding.FragmentRequestsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestsFragment : Fragment(R.layout.fragment_requests) {
    private var _binding: FragmentRequestsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRequestsBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}