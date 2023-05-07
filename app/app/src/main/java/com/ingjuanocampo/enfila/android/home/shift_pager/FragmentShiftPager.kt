package com.ingjuanocampo.enfila.android.home.shift_pager
/*

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.TabConfigurationStrategy
import com.google.accompanist.pager.rememberPagerState

@ExperimentalPagerApi
class MyFragment : Fragment() {
    private val nestedFragments = listOf(FirstNestedFragment(), SecondNestedFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (findNavController().currentDestination?.id == R.id.my_fragment) {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })
    }

    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val pagerState = rememberPagerState(pageCount = nestedFragments.size)

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "My Fragment") }
                        )
                    },
                    content = {
                        Box(modifier = Modifier.fillMaxSize()) {
                            TabRow(
                                selectedTabIndex = pagerState.currentPage,
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary,
                                indicator = { tabPositions ->
                                    TabRowDefaults.Indicator(
                                        color = MaterialTheme.colors.secondary,
                                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                                    )
                                }
                            ) {
                                nestedFragments.forEachIndexed { index, nestedFragment ->
                                    Tab(
                                        text = { Text(text = "Tab ${index + 1}") },
                                        selected = pagerState.currentPage == index,
                                        onClick = {
                                            pagerState.currentPage = index
                                        }
                                    )
                                }
                            }

                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize(),
                                dragEnabled = true,
                                verticalAlignment = Alignment.Top,
                                tabConfigurationStrategy = TabConfigurationStrategy
                                    .explicit(tabCount = nestedFragments.size)
                            ) { page ->
                                nestedFragments[page].Content()
                            }
                        }
                    }
                )
            }
        }
    }

    private fun rememberPagerState(pageCount: Any): Any {

    }
}*/
