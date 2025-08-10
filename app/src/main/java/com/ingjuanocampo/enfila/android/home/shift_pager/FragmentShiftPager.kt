package com.ingjuanocampo.enfila.android.home.shift_pager

import com.ingjuanocampo.common.tab.FragmentBaseTab
import com.ingjuanocampo.common.tab.FragmentTabItem
import com.ingjuanocampo.enfila.android.home.history.FragmentHistory
import com.ingjuanocampo.enfila.android.home.list.FragmentListItems

class FragmentShiftPager : FragmentBaseTab() {
    override val listItems: List<FragmentTabItem>
        get() =
            listOf(
                FragmentTabItem(
                    title = "List",
                    fragmentInstance = { FragmentListItems.newInstance() },
                ),
                FragmentTabItem(
                    title = "History",
                    fragmentInstance = { FragmentHistory.newInstance() },
                ),
            )

    companion object {
        fun newInstance(): FragmentShiftPager {
            return FragmentShiftPager()
        }
    }
}

/*

@Preview
@Composable
fun previewTabd() {
    TwoTabsScreen()
}

@Composable
fun TwoTabsScreen() {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Home", "About", "Settings")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        when (tabIndex) {
            0 -> FirstFragment()
            1 -> SecondFragment()
        }
    }
}

@Composable
fun FirstFragment(fragmentManager: FragmentManager) {

    Text("This is the first fragment.")
}

@Composable
fun SecondFragment() {
    Text("This is the second fragment.")
}
*/
