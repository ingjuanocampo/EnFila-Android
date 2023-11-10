package com.ingjuanocampo.enfila.android.navigation

import androidx.core.os.bundleOf
import com.ingjuanocampo.common.composable.NavigationEffect
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.home.list.FragmentListItems
import com.ingjuanocampo.enfila.android.home.list.details.FragmentShiftDetail
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

interface NavigationDestinations {

    fun toShiftByClient(clientId: String): NavigationEffect

    fun navigateToShiftDetails(id: String): NavigationEffect
}


class NavigationDestinationsListImp @Inject constructor() : NavigationDestinations {
    override fun toShiftByClient(clientId: String): NavigationEffect {
        return NavigationEffect(
            id = R.id.shifts_by_client,
            label = "Shift by client",
            fragment = FragmentListItems::class.qualifiedName.orEmpty(),
            bundle = bundleOf("ClientId" to clientId)
        )
    }

    override fun navigateToShiftDetails(id: String): NavigationEffect {
        return NavigationEffect(
            id = R.id.shifts_detail,
            label = "Shift details",
            fragment = FragmentShiftDetail::class.qualifiedName.orEmpty(),
            bundle = bundleOf("id" to id)
        )
    }

}

@InstallIn(SingletonComponent::class)
@Module
interface NavigationModule {
    @Singleton
    @Binds
    fun binds(navigation: NavigationDestinationsListImp): NavigationDestinations

}
