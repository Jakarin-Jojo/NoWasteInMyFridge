package com.develop.nowasteinmyfridge.feature.setting

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.develop.nowasteinmyfridge.R
import com.develop.nowasteinmyfridge.ui.theme.BaseColor
import com.develop.nowasteinmyfridge.ui.theme.GrayPrimary
import com.develop.nowasteinmyfridge.util.Result

@Composable
fun BoxNavigation(
    menuSettingScreen: MenuSettingScreen,
    isFirstItem: Boolean,
    isLastItem: Boolean,
    navController: NavHostController,
    content: @Composable (Modifier) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .clip(
                when {
                    isFirstItem && isLastItem -> RoundedCornerShape(16.dp)
                    isFirstItem -> RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    isLastItem -> RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                    else -> RoundedCornerShape(0.dp)
                }
            )
            .background(Color.White)
            .clickable {
                navController.navigate(menuSettingScreen.route)
            }
            .drawBehind {
                val lineHeight = 2.dp.toPx()
                if (!isLastItem) {
                    drawLine(
                        start = Offset(10f, size.height),
                        end = Offset(size.width - 10f, size.height),
                        color = GrayPrimary,
                        strokeWidth = lineHeight
                    )
                }
            }


    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = menuSettingScreen.icon,
                    contentDescription = null,
                    tint = GrayPrimary,
                    modifier = Modifier
                        .size(34.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = menuSettingScreen.nameResID),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = GrayPrimary,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = GrayPrimary,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
            content(Modifier.padding(16.dp))
        }
    }
}

@Composable
fun SliderBoxComponentVertical(
    menuSettingScreens: List<MenuSettingScreen>,
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        itemsIndexed(menuSettingScreens) { index, menuItem ->
            BoxNavigation(
                menuSettingScreen = menuItem,
                isFirstItem = index == 0,
                isLastItem = index == menuSettingScreens.size - 1,
                navController = navController,
            ) {

            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingScreen(
    navController: NavHostController,
    settingViewModel: SettingViewModel = hiltViewModel(),
) {
    val menuSettingScreens = listOf(
        MenuSettingScreen.Account,
    )

    val menuSettingScreens2 = listOf(
        MenuSettingScreen.Grocery,
        MenuSettingScreen.WasteReport,
        MenuSettingScreen.TestNotification,
    )

    val userInfoState by settingViewModel.userProfileInfoState.collectAsState()
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
    val userName = when (val result = userInfoState) {
        is Result.Success -> {
            val user = result.data
            user.firstName + " " + user.lastName
        }

        else -> {
            stringResource(id = R.string.loading)
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BaseColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.4f)
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.user_background),
                    contentDescription = "Header Login",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = userName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }
            SliderBoxComponentVertical(
                menuSettingScreens = menuSettingScreens,
                modifier = Modifier
                    .offset(y = (-60).dp)
                    .padding(20.dp),
                navController = navController,
            )
            SliderBoxComponentVertical(
                menuSettingScreens = menuSettingScreens2,
                modifier = Modifier
                    .offset(y = (-60).dp)
                    .padding(20.dp),
                navController = navController,
            )
        }
    }
}

@Preview
@Composable
fun SettingScreenPreview() {
    val navController = rememberNavController()
    SettingScreen(navController)
}