package com.ingjuanocampo.enfila.android.home.list.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.ingjuanocampo.common.composable.BaseComposableFragment
import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.enfila.android.home.list.model.ShiftItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentShiftDetail : BaseComposableFragment<ShiftItem>() {
    private val detailViewModel: ShiftDetailsViewModel by viewModels()
    override val viewModel: MviBaseViewModel<ShiftItem>
        get() = detailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailViewModel.init(requireArguments())
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @Composable
    override fun render(state: ShiftItem) {
        ShiftDetailScreen(
            state,
            onCancel = {
                detailViewModel.onCancel()
            },
            onActive = {
                detailViewModel.onActive()
            },
            onFinish = {
                detailViewModel.onFinish()
            }
        )
    }

    companion object {
        fun newInstance(id: String): FragmentShiftDetail {
            val args = Bundle()
            args.putString("id", id)
            val fragment = FragmentShiftDetail()
            fragment.arguments = args
            return fragment
        }
    }
}
