package com.develop.nowasteinmyfridge.feature.inventory.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.develop.nowasteinmyfridge.data.model.Ingredient
import com.develop.nowasteinmyfridge.ui.theme.BaseRed
import com.develop.nowasteinmyfridge.ui.theme.GreenExd
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ShowingBox(
    ingredient: Ingredient,
    onDeleteClicked: () -> Unit
) {
    var isDeleteVisible by remember { mutableStateOf(false) }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val efdDate = dateFormat.parse(ingredient.efd)
    val today = Calendar.getInstance().time
    val daysUntilExpiry = (((efdDate?.time ?: 0) - today.time) / (1000 * 60 * 60 * 24)).toInt() + 1

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
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .align(Alignment.TopStart)
                .zIndex(2f)
                .background(if (daysUntilExpiry <= 3) BaseRed else GreenExd),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(shape = CircleShape)
                    .zIndex(1f)
                    .background(color = Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$daysUntilExpiry",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                )
            }
        }

        Image(
            painter = rememberAsyncImagePainter(ingredient.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(10.dp))
        )
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(start = 6.dp, top = 6.dp)
        ) {
            Text(
                text = ingredient.name,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(top = 6.dp, end = 10.dp, start = 6.dp)
                    .shadow(4.dp, shape = CircleShape)
                    .zIndex(1f),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
