package com.example.lecturemanager.ui.timetable

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.lecturemanager.R
import com.example.lecturemanager.databinding.FragmentTimetableBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimetableFragment : Fragment(R.layout.fragment_timetable) {
    private var _binding: FragmentTimetableBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTimetableBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}