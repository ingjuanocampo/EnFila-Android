package com.ingjuanocampo.enfila.android.lobby.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.lobby.profile.domain.ProfileCard


@Composable
fun ProfileView(profile: ProfileCard) {
    AppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
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
                        text = profile.userName,
                        color = MaterialTheme.colors.secondary,
                        style = MaterialTheme.typography.subtitle2
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = profile.companyName,
                        modifier = Modifier.padding(all = 4.dp),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }

    }

}


@Composable
@Preview(device = Devices.PIXEL_4_XL, )
fun ProfilePreview() {
    ProfileView(
        profile = ProfileCard(
            "User",
            "31311231312",
            "Company",
            "112",
            "100"
        )
    )
}