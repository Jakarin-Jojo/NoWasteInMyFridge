package com.develop.nowasteinmyfridge.feature.inventory

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.develop.nowasteinmyfridge.R
import com.develop.nowasteinmyfridge.feature.inventory.component.ShowingBox
import com.develop.nowasteinmyfridge.ui.theme.YellowBtn
import com.develop.nowasteinmyfridge.util.Result

@SuppressLint("SuspiciousIndentation", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InventoryScreen(
    inventoryViewModel: InventoryViewModel = hiltViewModel(),
) {
    val ingredientsList by inventoryViewModel.ingredientsState
    var showDialog by remember { mutableStateOf(false) }
    var selectedIngredientIndex by remember { mutableIntStateOf(-1) }
    var quantityText by remember { mutableStateOf(TextFieldValue()) }
    val updateIngredientState by inventoryViewModel.updateIngredientQuantityState.collectAsStateWithLifecycle()
    val deleteIngredientSate by inventoryViewModel.deleteIngredientState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(80.dp),
                Alignment.BottomCenter
            ) {
                Text(
                    text = "Inventory",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
            }
            Column(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(16.dp),
                ) {
                    if (ingredientsList.isNotEmpty()) {
                        items(ingredientsList.size) { index ->
                            val ingredient = ingredientsList[index]
                            ShowingBox(
                                ingredient = ingredient,
                                onDeleteClicked = {
                                    selectedIngredientIndex = index
                                    showDialog = true
                                }
                            )
                            selectedIngredientIndex = index
                        }
                    } else {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(stringResource(id = R.string.no_ingredients_available))
                            }
                        }
                    }
                }
            }
            if (showDialog && selectedIngredientIndex != -1) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = { showDialog = false },
                                modifier = Modifier
//                                    .padding(end = 8.dp, top = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Dangerous,
                                    contentDescription = "Cancel"
                                )
                            }
                        }
//                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = "Update Quantity",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Column {
                            Text(
                                text = "Ingredient: ${ingredientsList[selectedIngredientIndex].name}\n" +
                                        "Quantity: ${ingredientsList[selectedIngredientIndex].quantity}"
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(
                                    colors = ButtonDefaults.buttonColors(YellowBtn),
                                    onClick = {
                                        showDialog = false
                                        val initialQuantity =
                                            ingredientsList[selectedIngredientIndex].quantity
                                        val updatedQuantity = initialQuantity * 0.75
                                        inventoryViewModel.updateIngredientQuantity(
                                            ingredientID = ingredientsList[selectedIngredientIndex].id,
                                            newQuantity = updatedQuantity.toInt(),
                                        )
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                ) {
                                    Text(text = "25%")
                                }
                                Button(
                                    colors = ButtonDefaults.buttonColors(YellowBtn),
                                    onClick = {
                                        showDialog = false
                                        val initialQuantity =
                                            ingredientsList[selectedIngredientIndex].quantity
                                        val updatedQuantity = initialQuantity * 0.5
                                        inventoryViewModel.updateIngredientQuantity(
                                            ingredientID = ingredientsList[selectedIngredientIndex].id,
                                            newQuantity = updatedQuantity.toInt(),
                                        )
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                ) {
                                    Text(text = "50%")
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(
                                    colors = ButtonDefaults.buttonColors(YellowBtn),
                                    onClick = {
                                        showDialog = false
                                        val initialQuantity =
                                            ingredientsList[selectedIngredientIndex].quantity
                                        val updatedQuantity = initialQuantity * 0.25
                                        inventoryViewModel.updateIngredientQuantity(
                                            ingredientID = ingredientsList[selectedIngredientIndex].id,
                                            newQuantity = updatedQuantity.toInt(),
                                        )
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                ) {
                                    Text(text = "75%")
                                }
                                Button(
                                    colors = ButtonDefaults.buttonColors(YellowBtn),
                                    onClick = {
                                        showDialog = false
                                        inventoryViewModel.deleteIngredient(
                                            ingredientID = ingredientsList[selectedIngredientIndex].id,
                                        )
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                ) {
                                    Text(text = "100%")
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                value = quantityText,
                                onValueChange = { quantityText = it },
                                label = { Text("How much that you use?") },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        showDialog = false
                                        inventoryViewModel.deleteIngredient(ingredientsList[selectedIngredientIndex].id)
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = 6.dp)
                                ) {
                                    Text(text = "Delete")
                                }
                                Button(
                                    onClick = {
                                        showDialog = false
                                        val initialQuantity =
                                            ingredientsList[selectedIngredientIndex].quantity
                                        val newQuantity =
                                            (initialQuantity - quantityText.text.toIntOrNull()!!)
                                        inventoryViewModel.updateIngredientQuantity(
                                            ingredientsList[selectedIngredientIndex].id,
                                            newQuantity
                                        )
                                    },
                                    modifier = Modifier.padding(horizontal = 6.dp)
                                ) {
                                    Text(text = "Update")
                                }
                            }
                        }
                    },
                    confirmButton = { },
                    dismissButton = { }
                )
            }
        }
    }
    if (updateIngredientState is Result.Success) {
        Toast.makeText(context, "update Ingredient Success", Toast.LENGTH_SHORT).show()
    } else if (updateIngredientState is Result.Error) {
        val error = (updateIngredientState as Result.Error).exception
        Toast.makeText(
            context,
            "ERROR: ${error.message.toString()}",
            Toast.LENGTH_SHORT,
        ).show()
    }
    if (deleteIngredientSate is Result.Success) {
        Toast.makeText(context, "delete Ingredient Success", Toast.LENGTH_SHORT).show()
    } else if (deleteIngredientSate is Result.Error) {
        val error = (deleteIngredientSate as Result.Error).exception
        Toast.makeText(
            context,
            "ERROR: ${error.message.toString()}",
            Toast.LENGTH_SHORT,
        ).show()
    }
}

