package com.ingjuanocampo.enfila.android.home.tips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ingjuanocampo.enfila.android.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentTips : Fragment() {
    companion object {
        fun newInstance() = FragmentTips()
    }

    private val viewModel: ViewModelTips by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tips, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }
}
