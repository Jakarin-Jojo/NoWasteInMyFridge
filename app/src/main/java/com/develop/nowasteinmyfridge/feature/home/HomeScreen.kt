package com.develop.nowasteinmyfridge.feature.home

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.develop.nowasteinmyfridge.R
import com.develop.nowasteinmyfridge.feature.account.AccountViewModel
import com.develop.nowasteinmyfridge.feature.inventory.InventoryViewModel
import com.develop.nowasteinmyfridge.ui.theme.BaseColor
import com.develop.nowasteinmyfridge.ui.theme.BaseGray
import com.develop.nowasteinmyfridge.ui.theme.Black
import com.develop.nowasteinmyfridge.util.Result
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("SuspiciousIndentation", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    inventoryViewModel: InventoryViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    accountViewModel: AccountViewModel = hiltViewModel(),
    navController: NavController,
) {
    val userInfoState by accountViewModel.userProfileInfoState.collectAsState()
    val ingredientsList by inventoryViewModel.ingredientsState
    val displayedIngredients = ingredientsList.map { ingredient ->
        ingredient.name to ingredient.image
    }

    val names = displayedIngredients.map { it.first }
    val images = displayedIngredients.map { it.second }

    val sortedDisplayedIngredients = ingredientsList.sortedBy { ingredient ->
        val dateString = ingredient.efd
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
    }
    val ingredientsForSearch = sortedDisplayedIngredients
        .take(1).joinToString(", ") { it.name }

    val userProfile = when (val result = userInfoState) {
        is Result.Success -> {
            result.data
        }

        else -> {
            null
        }
    }
    val userName = userProfile?.firstName ?: stringResource(id = R.string.loading)

    if (ingredientsForSearch.isNotEmpty()) {
        LaunchedEffect(Unit) {
            try {
                homeViewModel.searchRecipes(ingredientsForSearch)
                Log.d("IngredientsForSearch", ingredientsForSearch)
            } catch (e: Exception) {
                Log.e("API Request Failed", e.message ?: "Unknown error")
            }
        }
    } else {
        Log.e("Empty ingredient", "Ingredients for search is empty")
    }

    val recipesState by homeViewModel.recipesState
    val hits = recipesState.hits

    Column {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp)
                .background(color = BaseColor)
        ) {
            Scaffold {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(fraction = 0.2f)
                    ) {
                        Image(
                            painter = painterResource(id = R.mipmap.home_topper),
                            contentDescription = "",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds
                        )
                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.CenterStart)
                                .padding(start = 20.dp, top = 16.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.welcome_message) + ", " + userName,
                                style = TextStyle(
                                    fontSize = 28.sp,
                                    shadow = Shadow(
                                        color = Color.Black,
                                        offset = Offset(2f, 2f),
                                        blurRadius = 4f
                                    )
                                ),
                                color = Color.White,
                            )
                        }
                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.BottomStart)
                                .padding(start = 20.dp, bottom = 10.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.ingredient),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Black,
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = BaseColor)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            SliderBoxComponent(names = names, images = images)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(id = R.string.label_suggest_menu),
                                style = MaterialTheme.typography.titleMedium,
                                color = Black
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            if (hits.isEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.no_suggest_menu),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Black
                                )
                            } else {
                                val recipeNames = hits.map { it.recipe.label }
                                val recipeImages = hits.map { it.recipe.image }
                                val ingredientLinesSent = hits.map { recipeHit ->
                                    recipeHit.recipe.ingredientLines.let {
                                        listOfNotNull(it)
                                    }
                                }.flatten()
                                SliderBoxComponentVertical(
                                    names = recipeNames,
                                    images = recipeImages,
                                    ingredientLines = ingredientLinesSent,
                                    onItemClick = { name, image, ingredientLines ->
                                        navController.navigate(
                                            "menu/${Uri.encode(name)}/${
                                                Uri.encode(
                                                    image
                                                )
                                            }?ingredients=${
                                                Uri.encode(
                                                    ingredientLines.joinToString(
                                                        ";"
                                                    )
                                                )
                                            }"
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun SliderBoxComponent(
    names: List<String>,
    images: List<String>,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp)
    ) {
        itemsIndexed(names) { index, name ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .clickable {
                        selectedIndex = index
                    }
                    .width(200.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = images.getOrNull(index),
                        contentDescription = "Image for $name",
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.FillBounds
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    drawIntoCanvas {
                        drawRect(
                            color = BaseGray.copy(alpha = 0.25f),
                            topLeft = Offset(0f, 0f),
                            size = Size(size.width, size.height)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SliderBoxComponentVertical(
    names: List<String>,
    images: List<String>,
    ingredientLines: List<List<String>>,
    modifier: Modifier = Modifier,
    onItemClick: (String, String, List<String>) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        itemsIndexed(names) { index, name ->
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .clickable {
                        selectedIndex = index
                        onItemClick(name, images.getOrNull(index) ?: "", ingredientLines[index])
                    }
                    .height(100.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(250.dp)
                            .padding(end = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                    ) {
                        AsyncImage(
                            model = images.getOrNull(index),
                            contentDescription = "Image for $name",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds,
                        )
                    }

                }
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    drawIntoCanvas {
                        drawRect(
                            color = BaseGray.copy(alpha = 0.25f),
                            topLeft = Offset(0f, 0f),
                            size = Size(size.width, size.height),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}
