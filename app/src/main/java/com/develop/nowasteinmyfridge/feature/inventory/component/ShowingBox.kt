package com.develop.nowasteinmyfridge.feature.inventory.component

//import androidx.compose.material3.icons.MaterialIcons
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.develop.nowasteinmyfridge.data.model.Ingredient

@Composable
fun ShowingBox(
    ingredient: Ingredient,
    onDeleteClicked: () -> Unit
) {
    var isDeleteVisible by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .padding(10.dp)
            .clickable {
                isDeleteVisible = !isDeleteVisible
            },
        contentAlignment = Alignment.BottomEnd
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(10.dp))
                .drawWithCache {
                    val gradient = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Gray),
                        startY = size.height / 4,
                        endY = size.height,
                    )
                    onDrawWithContent {
                        drawContent()
                        drawRect(gradient)
                    }
                }
                .clickable {
                    isDeleteVisible = !isDeleteVisible
                }
        )

        if (isDeleteVisible) {
            Icon(
                imageVector = Icons.Default.Dangerous,
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier
                    .padding(end = 8.dp, top = 8.dp)
                    .size(24.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .align(Alignment.TopEnd)
                    .clickable { onDeleteClicked() }
                    .zIndex(1f)
            )
        }
        Image(
            painter = rememberAsyncImagePainter(ingredient.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(10.dp))
        )
        Text(
            text = ingredient.name,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(top = 6.dp, end = 10.dp, start = 6.dp)
                .zIndex(1f),
            color = Color.White,
        )
    }
}
