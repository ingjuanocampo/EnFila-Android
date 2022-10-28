package com.ingjuanocampo.enfila.android.home.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.home.profile.domain.ProfileCard
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import com.ingjuanocampo.enfila.android.ui.theme.TextAppStyles.BodyTextMediumStyle
import com.ingjuanocampo.enfila.android.ui.theme.TextAppStyles.BodyTextMediumStyleError
import com.ingjuanocampo.enfila.android.ui.theme.TextAppStyles.HeadLineTextStyle
import com.ingjuanocampo.enfila.android.ui.theme.TextAppStyles.SmallTextStyle


@Composable
fun ProfileView(profile: ProfileCard) {
    AppTheme {

        // Improve theming and load basic info
        ConstraintLayout(Modifier.fillMaxSize()) {

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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_stop),
                contentDescription = null,
            )
            Text(
                text = "Total shifts",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                style = BodyTextMediumStyleError()
            )
        }
    }

}

@Composable
fun ProfileOptions(profile: ProfileCard, modifier: Modifier) {
    Column(
        modifier = modifier,
        Arrangement.Top
    ) {
        Row(
            modifier = Modifier.padding(all = 8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_stop),
                contentDescription = null,
            )
            Text(
                text = "Total shifts",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                style = BodyTextMediumStyle()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_stop),
                contentDescription = null,
            )
            Text(
                text = "Total shifts",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                style = BodyTextMediumStyle()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_stop),
                contentDescription = null,
            )
            Text(
                text = "Total shifts",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                style = BodyTextMediumStyle()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_stop),
                contentDescription = null,
            )
            Text(
                text = "Total shifts",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                style = BodyTextMediumStyle()
            )
        }
    }
}

@Composable
fun ProfileHeader(profile: ProfileCard, modifier: Modifier) = Column(modifier = modifier) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.ic_account),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = profile.companyName,
                style = HeadLineTextStyle()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = profile.phone,
                modifier = Modifier.padding(all = 4.dp),
                style = BodyTextMediumStyle()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = profile.email,
                modifier = Modifier.padding(all = 4.dp),
                style = BodyTextMediumStyle()
            )
        }
    }

    Column(modifier = Modifier.padding(all = 8.dp), Arrangement.Center) {
        Divider(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )

        Row(
            modifier = Modifier.height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .weight(1f, true)
                    .padding(all = 4.dp),
            ) {
                Text(
                    text = "123",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = BodyTextMediumStyle(),
                )

                Text(
                    text = "# Turns",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = SmallTextStyle(),
                )
            }

            Divider(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f, true)
                    .padding(all = 4.dp),
            ) {
                Text(
                    text = "323",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = BodyTextMediumStyle(),
                )

                Text(
                    text = "# Clients",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = SmallTextStyle(),
                )
            }
        }

        Divider(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
    }

}


@Composable
@Preview(device = Devices.PIXEL_4_XL)
fun ProfilePreview() {
    ProfileView(
        profile = ProfileCard(
            "Company Name",
            "31311231312",
            "company@gmail.com",
            "112",
            "100"
        )
    )
}