package com.ingjuanocampo.enfila.android.home.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.home.profile.domain.ProfileCard
import com.ingjuanocampo.enfila.android.home.profile.viewmodel.ProfileViewModel
import com.ingjuanocampo.enfila.android.ui.common.AnimatedComposeButton
import com.ingjuanocampo.enfila.android.ui.common.ProgressableButtonState
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme


@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel = viewModel()) {
    AppTheme {
        val profile: ProfileCard by profileViewModel.state.collectAsState()
        ProfileView(profile = profile)
    }

}


@Composable
fun ProfileView(profile: ProfileCard) {
    ConstraintLayout(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)) {
        val (headerRef, bodyRef, logoutRef) = createRefs()

        ProfileHeader(
            profile,
            Modifier.constrainAs(headerRef) {
                top.linkTo(parent.top)
            }
        )
        ProfileOptions(
            profile,
            Modifier.constrainAs(bodyRef) {
                top.linkTo(headerRef.bottom)
                bottom.linkTo(logoutRef.top)
                height = Dimension.fillToConstraints
            }
        )
        Logout(
            Modifier.constrainAs(logoutRef) {
                bottom.linkTo(parent.bottom)
            }
        )
    }

}

@Composable
fun Logout(modifier: Modifier) {

    Column(modifier = modifier, Arrangement.Top) {
        Divider(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )

        var state by remember { mutableStateOf(ProgressableButtonState.IDLE) }
        AnimatedComposeButton(  Modifier
            .size(30.dp).padding(2.dp),
            state,
          ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth().clickable(enabled = true, onClick = {
                        state = ProgressableButtonState.PROGRESS
                    })
                    .padding(all = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.logout),
                    contentDescription = null,
                )
                Text(
                    text = "Log out",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }


    }
}

@Composable
fun ProfileOptions(profile: ProfileCard, modifier: Modifier) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        Arrangement.Top,

        ) {
        profile.options.forEach {
            Row(
                modifier = Modifier.padding(all = 8.dp)
            ) {
                Image(
                    painter = painterResource(it.icon),
                    contentDescription = null,
                )
                Text(
                    text = it.title,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ProfileHeader(profile: ProfileCard, modifier: Modifier) = Column(modifier = modifier) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        /*   Image(
               painter = painterResource(R.drawable.ic_account),
               contentDescription = null,
               modifier = Modifier
                   .size(80.dp)
                   .clip(CircleShape)
           )
           Spacer(modifier = Modifier.width(8.dp))
   */


    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(2.dp),
        elevation = 2.dp

    ) {
        val textColor = MaterialTheme.colorScheme.onPrimaryContainer
        Column(modifier = Modifier.padding(5.dp)) {
            Text(
                text = profile.companyName,
                style = MaterialTheme.typography.titleLarge,
                color = textColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = profile.phone,
                modifier = Modifier.padding(all = 4.dp),
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = profile.email,
                modifier = Modifier.padding(all = 4.dp),
                style = MaterialTheme.typography.titleMedium,
                color = textColor,
            )
        }
    }

    Column(modifier = Modifier.padding(all = 8.dp), Arrangement.Center) {

        StatictisView(profile)

    }

}

@Composable
fun StatictisView(profile: ProfileCard) {

    Surface(color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = RoundedCornerShape(2.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {


            Column(
                modifier = Modifier
                    .weight(1f, true)
                    .padding(horizontal = 20.dp),
            ) {

                Text(
                    text = "December Results",
                    textAlign = TextAlign.Left,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )

            }


        }
    }

    StatiticsSection(listOf(StadisticSectionUI(
        "# Turns", profile.totalShifts,
        "# Clients", profile.numberClients,
    ), StadisticSectionUI(
        "Shifts by day", profile.shiftByDay,
        "Clients by day", profile.clientsByDay
    )
    ))

}

data class StadisticSectionUI(val title: String, val value: String,
                              val title2: String, val value2: String, )

@Composable
fun StatiticsSection(listOf: List<StadisticSectionUI>) {

    listOf.forEach { row ->
        Surface(color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(2.dp),
            elevation = 2.dp
        ) {
            Row(
                modifier = Modifier.height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {


                Column(
                    modifier = Modifier
                        .weight(1f, true)
                        .padding(horizontal = 20.dp),
                ) {

                    Text(
                        text = row.title,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = row.value,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )


                }


                Column(
                    modifier = Modifier
                        .weight(1f, true)
                        .padding(all = 4.dp),
                ) {

                    Text(
                        text = row.title2,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = row.value2,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }

}


@Composable
@Preview(device = Devices.PIXEL_4_XL,
    showBackground = true,)
fun ProfilePreview() {
    AppTheme {
        ProfileView(
            ProfileCard("Company", "31231313", "indawa.com",
                "123", "33", "32", "12", "1Mins" )
        )
    }
}