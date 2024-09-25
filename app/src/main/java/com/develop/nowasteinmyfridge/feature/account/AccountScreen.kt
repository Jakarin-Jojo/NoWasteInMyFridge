package com.develop.nowasteinmyfridge.feature.account

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.develop.nowasteinmyfridge.BACK_TO_APP_ROUTE
import com.develop.nowasteinmyfridge.R
import com.develop.nowasteinmyfridge.data.model.UserProfile
import com.develop.nowasteinmyfridge.util.Result

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountScreen(
    accountViewModel: AccountViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val userInfoState by accountViewModel.userProfileInfoState.collectAsState()
    val painter = when (val result = userInfoState) {
        is Result.Success -> {
            val profileImageUrl = result.data.profileImageUrl
            if (profileImageUrl != "") {
                rememberAsyncImagePainter(model = profileImageUrl)
            } else {
                painterResource(id = R.drawable.ic_launcher_foreground)
            }
        }

        else -> painterResource(id = R.drawable.ic_launcher_foreground)
    }
    val userProfile = when (val result = userInfoState) {
        is Result.Success -> {
            result.data
        }

        else -> {
            return
        }
    }
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            UserProfileHeader(imageProfile = painter)
            Spacer(modifier = Modifier.height(16.dp))
            UserProfileContent(userProfile = userProfile)
            OutlinedButton(
                onClick = {
                    accountViewModel.logout()
                    navController.navigate(BACK_TO_APP_ROUTE)
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(id = R.string.logout))
            }
        }
    }
}


@Composable
fun UserProfileHeader(imageProfile: Painter) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(shape = MaterialTheme.shapes.medium)
    ) {
        Image(
            painter = imageProfile,
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        )
    }
}

@Composable
fun UserProfileContent(userProfile: UserProfile) {
    LazyColumn {
        item {
            UserDetailItem(
                icon = Icons.Default.Person,
                label = stringResource(id = R.string.label_name_user_profile),
                value = userProfile.firstName + " " + userProfile.lastName
            )
        }
        item {
            UserDetailItem(
                icon = Icons.Default.Email,
                label = stringResource(id = R.string.label_email_user_profile),
                value = userProfile.email,
            )
        }
        item {
            UserDetailItem(
                icon = Icons.Default.Man,
                label = stringResource(id = R.string.label_gender_user_profile),
                value = userProfile.gender,
            )
        }
        item {
            UserDetailItem(
                icon = Icons.Default.Cake,
                label = stringResource(id = R.string.label_birthday_user_profile),
                value = userProfile.birthday,
            )
        }
    }
}

@Composable
fun UserDetailItem(
    icon: ImageVector,
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Text(text = value, style = MaterialTheme.typography.bodySmall)
        }
    }
}

