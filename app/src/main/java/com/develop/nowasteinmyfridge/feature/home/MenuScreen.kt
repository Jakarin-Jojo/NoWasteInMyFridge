package com.develop.nowasteinmyfridge.feature.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.develop.nowasteinmyfridge.R
import com.develop.nowasteinmyfridge.ui.theme.White

@Composable
fun DisplayIngredients(ingredients: List<String>) {
    val formattedIngredients = ingredients
        .joinToString(separator = "\n • ", prefix = " • ") {
            it.trim()
        }

    Text(
        text = formattedIngredients
    )
}

@Composable
fun OvalIconView(icon: ImageVector, text: String) {
    Box(
        modifier = Modifier
            .height(96.dp)
            .width(70.dp)
            .background(Color.Gray, RoundedCornerShape(100.dp)),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .offset(y = (10).dp)
                    .background(Color.White, RoundedCornerShape(100.dp))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .size(34.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = text,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = White,
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MenuScreen(name: String?, image: String?, ingredientList: List<String>) {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.4f)
            ) {
                AsyncImage(
                    model = image,
                    contentDescription = "Image for menu: $name",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-40).dp)
                    .height(900.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                    ),
                contentAlignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 30.dp, end = 30.dp)
                        .padding(top = 50.dp),
                )
                {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        name?.let {
                            Text(
                                text = it,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OvalIconView(
                                    icon = Icons.Default.AccessTimeFilled,
                                    text = "40 mins"
                                )
                                OvalIconView(icon = Icons.Default.Fastfood, text = "40 mins")
                                OvalIconView(
                                    icon = Icons.Default.LocalFireDepartment,
                                    text = "40 mins"
                                )
                                OvalIconView(icon = Icons.Default.Bookmark, text = "40 mins")
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        Column(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            Text(
                                text = stringResource(id = R.string.ingredient),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            LazyColumn(
                                modifier = Modifier.height(200.dp)
                            ) {
                                item {
                                    DisplayIngredients(ingredientList)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}