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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.text.font.FontWeight
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
import com.develop.nowasteinmyfridge.ui.theme.ShadowGray
import com.develop.nowasteinmyfridge.util.Result
import kotlinx.coroutines.delay
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
    var numberofingredients by remember { mutableStateOf(3) }
    val names = displayedIngredients.map { it.first }
    val images = displayedIngredients.map { it.second }
    var foundState by remember { mutableStateOf(false) }

    val sortedDisplayedIngredients = ingredientsList.sortedBy { ingredient ->
        val dateString = ingredient.efd
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
    }
    val ingredientsForSearch3 = sortedDisplayedIngredients
        .take(3).joinToString(", ") { it.name }
    val ingredientsForSearch2 = sortedDisplayedIngredients
        .take(2).joinToString(", ") { it.name }
    val ingredientsForSearch1 = sortedDisplayedIngredients
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

    if (ingredientsForSearch3.isNotEmpty()) {
        LaunchedEffect(Unit) {
            try {
                homeViewModel.searchRecipes(ingredientsForSearch3)
                Log.d("IngredientsForSearch", ingredientsForSearch3)
            } catch (e: Exception) {
                Log.e("API Request Failed", e.message ?: "Unknown error")
            }
        }
    } else {
        Log.e("Empty ingredient", "Ingredients for search is empty")
    }

    val recipesState by homeViewModel.recipesState
    val hits = recipesState.hits

    LaunchedEffect(recipesState) {
        delay(3000)
        if (recipesState != null && hits.isEmpty()) {
            numberofingredients = 2
            Log.d("kiki", "popopkull")
            foundState = true
        }

        if (hits.isEmpty()) {
            if (numberofingredients == 2) {
                homeViewModel.searchRecipes(ingredientsForSearch2)
                Log.d("Using Ingredients", "Ingredients for search: $ingredientsForSearch2")
                if (recipesState != null && hits.isEmpty()) {
                    numberofingredients = 1
                    Log.d("Setting Number of Ingredients", "Number of ingredients set to 1")
                }
            } else if (numberofingredients == 1) {
                homeViewModel.searchRecipes(ingredientsForSearch1)
                Log.d("Using Ingredients", "Ingredients for search: $ingredientsForSearch1")
            }
        }
    }
    if (numberofingredients == 1) {
        homeViewModel.searchRecipes(ingredientsForSearch1)
    }
    Column {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = BaseColor)
        ) {
            Scaffold {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(fraction = 0.25f)
                    ) {
                        Image(
                            painter = painterResource(id = R.mipmap.topper),
                            contentDescription = "",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds
                        )
                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.TopStart)
                                .padding(start = 20.dp, top = 70.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.welcome_message) + ", " + userName,
                                style = TextStyle(
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    shadow = Shadow(
                                        color = Color.Black,
                                        offset = Offset(2f, 2f),
                                        blurRadius = 4f
                                    )
                                ),
                                color = Color.White,
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            Text(
                                text = stringResource(id = R.string.ingredient),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White,
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = BaseColor)
                            .offset(y = (-44).dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Column(
                                modifier = Modifier
                                     .padding(start = 8.dp)
                            ) {
                                SliderBoxComponent(names = names, images = images)
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.label_suggest_menu),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 18.sp,
                                    color = Black
                                )
                                Spacer(modifier = Modifier.height(12.dp))
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
                                    val calories = hits.map { it.recipe.calories }
                                    val totalTime = hits.map { it.recipe.totalTime }
                                    val mealType = hits.flatMap { it.recipe.mealType }
                                    val dishType = hits.flatMap { it.recipe.dishType }
                                    SliderBoxComponentVertical(
                                        names = recipeNames,
                                        images = recipeImages,
                                        ingredientLines = ingredientLinesSent,
                                        calories = calories,
                                        totalTime = totalTime,
                                        mealType = mealType,
                                        dishType = dishType,
                                        onItemClick = { name, image, ingredientLines, calories, totalTime, mealType, dishType ->
                                            navController.navigate(
                                                "menu/${Uri.encode(name)}/${
                                                    Uri.encode(image)
                                                }?ingredients=${
                                                    Uri.encode(
                                                        ingredientLines.joinToString(
                                                            ";"
                                                        )
                                                    )
                                                }&calories=${Uri.encode(calories.toString())}&totalTime=${Uri.encode(totalTime.toString())}&mealType=${Uri.encode(
                                                    mealType
                                                )}&dishType=${Uri.encode(dishType)}"
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
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
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
                            color = ShadowGray.copy(alpha = 0.3f),
                            topLeft = Offset(1f, 1f),
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
    calories: List<Float>,
    totalTime: List<Float>,
    mealType: List<String>,
    dishType: List<String>,
    onItemClick: (String, String, List<String>, Float, Float, String, String) -> Unit
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
                        onItemClick(
                            name,
                            images.getOrNull(index) ?: "",
                            ingredientLines[index],
                            calories.getOrNull(index) ?: 0f,
                            totalTime.getOrNull(index) ?: 0f,
                            mealType[index],
                            dishType[index]
                        )
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
                            .width(230.dp)
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
                            color = BaseGray.copy(alpha = 0.3f),
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
