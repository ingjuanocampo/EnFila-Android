package com.ingjuanocampo.enfila.android.utils

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import com.ingjuanocampo.common.composable.NavigationEffect
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.home.list.FragmentListItems


fun NavController.navigateToCustomDest(navigationEffect: NavigationEffect) {

    val navigator: FragmentNavigator =
        this.navigatorProvider.getNavigator(FragmentNavigator::class.java)

    val destination: FragmentNavigator.Destination = navigator.createDestination()
        .setClassName(navigationEffect.fragment)
    destination.id = navigationEffect.id
    destination.label = navigationEffect.label

    this.graph.addDestination(destination)

    //val bundle = bundleOf("ClientId" to it)
    this.navigate(R.id.new_fragment, navigationEffect.bundle)

}