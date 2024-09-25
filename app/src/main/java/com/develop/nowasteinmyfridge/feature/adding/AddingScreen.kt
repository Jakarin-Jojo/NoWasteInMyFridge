package com.develop.nowasteinmyfridge.feature.adding

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.develop.nowasteinmyfridge.BottomBarItem
import com.develop.nowasteinmyfridge.R
import com.develop.nowasteinmyfridge.data.model.IngredientCreate
import com.develop.nowasteinmyfridge.ui.theme.BaseColor
import com.develop.nowasteinmyfridge.ui.theme.Black
import com.develop.nowasteinmyfridge.ui.theme.GrayPrimary
import com.develop.nowasteinmyfridge.ui.theme.GreenButton
import com.develop.nowasteinmyfridge.ui.theme.GreenPrimary
import com.develop.nowasteinmyfridge.ui.theme.White
import com.develop.nowasteinmyfridge.util.Result
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddingScreen(
    navController: NavController,
    addingViewModel: AddingViewModel = hiltViewModel(),
) {
    var name by remember { mutableStateOf(TextFieldValue()) }
    var quantity by remember { mutableStateOf(TextFieldValue()) }
    var mfgDate by remember { mutableStateOf(Calendar.getInstance()) }
    var efdDate by remember { mutableStateOf(Calendar.getInstance()) }
    var selectImageUri by remember { mutableStateOf<Uri?>(null) }
    var isChecked by remember { mutableStateOf(false) }
    val brands by addingViewModel.brands.collectAsState()
    val image by addingViewModel.imageUrl.collectAsState()
    val isFoundProduct by addingViewModel.isFoundProduct.collectAsState()
    val getIngredientByBarcodeResult by addingViewModel.getIngredientByBarcodeResult.collectAsStateWithLifecycle()
    var brandsName by remember { mutableStateOf("") }
    var isShowDatePicker by remember { mutableStateOf(false) }
    var isFound by remember { mutableStateOf(true) }
    var imageUrl by remember { mutableStateOf("") }
    var scanName by remember {
        mutableStateOf(
            brands?.let { TextFieldValue(it) } ?: TextFieldValue()
        )
    }
    val isAddFromBarcode by addingViewModel.isAddFromBarcode.collectAsStateWithLifecycle()
    val useNameInsteadOfScanName = scanName.text.isEmpty()
    LaunchedEffect(brands) {
        brandsName = brands ?: ""
        scanName = brands?.let { TextFieldValue(it) } ?: TextFieldValue()
        imageUrl = image
        Log.d("ScanQRCode", "$selectImageUri, $brandsName")
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> selectImageUri = uri }
    )

    val addIngredientResult by addingViewModel.addIngredientResult.collectAsStateWithLifecycle()
    val context = LocalContext.current

    fun suggestDate() {
        val isInFridge = isChecked
        if (name.text.isEmpty() || mfgDate.timeInMillis == 0L) {
            Toast.makeText(context, "Please fill in name first", Toast.LENGTH_SHORT).show()
        } else {
            val recommendedEfdDate =
                addingViewModel.checkAndAutoFillEfdDate(name.text, mfgDate, isInFridge)
            if (recommendedEfdDate != null) {
                efdDate = recommendedEfdDate
            } else {
                isFound = false
                Toast.makeText(
                    context,
                    "No recommended expiration date. Please fill in by yourself.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

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
                        .fillMaxHeight(fraction = 0.35f)
                ) {
                    if (selectImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(model = selectImageUri),
                            contentDescription = "",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        if (imageUrl != "") {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Image for $imageUrl",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.mipmap.add_photo),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.CenterStart)
                            .padding(start = 20.dp, top = 16.dp)
                    ) {
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = (-24).dp)
                        .background(
                            color = White,
                            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 40.dp, start = 20.dp, end = 20.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(id = R.string.inventory),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Black,
                            )
                            Switch(
                                checked = isChecked,
                                onCheckedChange = {
                                    isChecked = it
                                }
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(id = R.string.inventory_info),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = GrayPrimary,
                            )
                            Text(
                                text = stringResource(id = R.string.is_in_freezer),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = GrayPrimary,
                            )
                        }


                        Text(
                            text = stringResource(id = R.string.ingredient_name),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Black,
                            modifier = Modifier.padding(top = 40.dp, bottom = 10.dp)
                        )
                        InputFieldWithPlaceholder(
                            placeholder = stringResource(id = R.string.name_placeholder),
                            textValue = if (useNameInsteadOfScanName) name else scanName,
                        ) {
                            if (useNameInsteadOfScanName) {
                                name = it
                            } else {
                                scanName = it
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(id = R.string.quantity),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Black,
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                        )
                        InputFieldWithPlaceholderWithBorder(
                            placeholder = stringResource(id = R.string.quantity_placeholder),
                            textValue = quantity,
                        ) {
                            quantity = it
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.Start,
                            ) {
                                Text(
                                    text = stringResource(id = R.string.manufacturing_date),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Black,
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                )
                                ClickableTextWithPlaceholder(
                                    placeholder = stringResource(id = R.string.mfg_placeholder),
                                    date = mfgDate,
                                    onDateSelected = { mfgDate = it }
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = stringResource(id = R.string.expiration_date),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Black,
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                )
                                ClickableTextWithPlaceholderWithNoValue(
                                    placeholder = stringResource(id = R.string.efd_placeholder),
                                    date = if (isShowDatePicker && name.text != "" && isFound) efdDate else null,
                                    onDateSelected = { efdDate = it }
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            horizontalArrangement = Arrangement.End,

                            ) {
                            Button(
                                onClick = {
                                    isShowDatePicker = true
                                    suggestDate()

                                },
                            ) {
                                Text(
                                    text = stringResource(id = R.string.suggest_date),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (addIngredientResult is Result.Loading || getIngredientByBarcodeResult is Result.Loading) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                Button(
                                    onClick = {
                                        photoPickerLauncher.launch("image/*")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = GreenButton),
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.import_image_ingredient),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                ScanBarcodeButton(addingViewModel)
                                val nameValue = brandsName.ifEmpty { name.text }
                                val quantityValue = quantity.text.toIntOrNull()
                                val hasValidName = nameValue.isNotBlank()
                                val hasValidQuantity = quantityValue != null && quantityValue >= 1
                                val hasValidEfd = efdDate.timeInMillis > System.currentTimeMillis()
                                Button(
                                    onClick = {
                                        Log.d(
                                            "addingViewModel",
                                            "$brandsName, $name.text, ${imageUrl.javaClass}, $imageUrl, $selectImageUri)"
                                        )
                                        addingViewModel.addIngredient(
                                            IngredientCreate(
                                                name = nameValue,
                                                quantity = quantityValue ?: 0,
                                                image = imageUrl.ifEmpty {
                                                    selectImageUri ?: ""
                                                },
                                                mfg = SimpleDateFormat(
                                                    "yyyy-MM-dd",
                                                    Locale.getDefault()
                                                ).format(mfgDate.time),
                                                efd = SimpleDateFormat(
                                                    "yyyy-MM-dd",
                                                    Locale.getDefault()
                                                ).format(efdDate.time),
                                                isInFreeze = isChecked,
                                                isAddFromBarcode = isAddFromBarcode,
                                            )
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = GreenButton),
                                    enabled = hasValidName && hasValidQuantity && hasValidEfd
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.add_ingredient),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                            }
                            if (addIngredientResult is Result.Success) {
                                Toast.makeText(context, "Adding Success", Toast.LENGTH_SHORT).show()
                                navController.navigate(BottomBarItem.Inventory.route)
                            } else if (addIngredientResult is Result.Error) {
                                val error = (addIngredientResult as Result.Error).exception
                                Toast.makeText(
                                    context,
                                    "ERROR: ${error.message.toString()}",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                            if (getIngredientByBarcodeResult is Result.Success) {
                                if (isFoundProduct == 1) {
                                    Toast.makeText(context, "Product Found", Toast.LENGTH_LONG)
                                        .show()
                                } else {
                                    Toast.makeText(context, "Product not found", Toast.LENGTH_LONG)
                                        .show()
                                }
                            } else if (getIngredientByBarcodeResult is Result.Error) {
                                val error = (addIngredientResult as Result.Error).exception
                                Toast.makeText(
                                    context,
                                    "ERROR: ${error.message.toString()}",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InputFieldWithPlaceholder(
    placeholder: String,
    textValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(10.dp)
            )
            .height(40.dp)
    ) {
        BasicTextField(
            value = textValue,
            onValueChange = { onValueChange(it) },
            textStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .align(Alignment.Center)
                .padding(start = 20.dp, end = 20.dp, top = 10.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        )

        if (textValue.text.isEmpty()) {
            Text(
                text = placeholder,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    .background(Color.Transparent)
                    .align(Alignment.Center),
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun InputFieldWithPlaceholderWithBorder(
    placeholder: String,
    textValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .height(40.dp)
            .background(GreenPrimary, RoundedCornerShape(10.dp))
    ) {
        BasicTextField(
            value = textValue,
            onValueChange = {
                onValueChange(it)
            },
            textStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .align(Alignment.Center)
                .padding(start = 20.dp, end = 20.dp, top = 10.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        )

        if (textValue.text.isEmpty()) {
            Text(
                text = placeholder,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    .background(Color.Transparent)
                    .align(Alignment.Center),
                textAlign = TextAlign.Start
            )
        }
    }
}


@Composable
fun ClickableTextWithPlaceholder(
    placeholder: String,
    date: Calendar,
    onDateSelected: (Calendar) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(10.dp)
            )
            .height(40.dp)
            .clickable {
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(year, month, dayOfMonth)
                        onDateSelected(selectedDate)
                    },
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = date.let {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it.time)
                } ?: placeholder,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp)
                    .background(Color.Transparent)
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.Start
            )

            Icon(
                imageVector = Icons.Filled.CalendarToday,
                contentDescription = "Calendar",
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
                    .clickable {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                val selectedDate = Calendar.getInstance()
                                selectedDate.set(year, month, dayOfMonth)
                                onDateSelected(selectedDate)
                            },
                            date.get(Calendar.YEAR),
                            date.get(Calendar.MONTH),
                            date.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
            )
        }
    }
}

@Composable
fun ClickableTextWithPlaceholderWithNoValue(
    placeholder: String,
    date: Calendar?,
    onDateSelected: (Calendar) -> Unit
) {
    val context = LocalContext.current
    val formattedDate = remember { mutableStateOf(placeholder) }

    // Update formattedDate when date changes
    if (date != null) {
        formattedDate.value = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.time)
    }

    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(10.dp)
            )
            .height(40.dp)
            .clickable {
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(year, month, dayOfMonth)
                        formattedDate.value = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).format(selectedDate.time)
                        onDateSelected(selectedDate)
                    },
                    date?.get(Calendar.YEAR) ?: Calendar
                        .getInstance()
                        .get(Calendar.YEAR),
                    date?.get(Calendar.MONTH) ?: Calendar
                        .getInstance()
                        .get(Calendar.MONTH),
                    date?.get(Calendar.DAY_OF_MONTH) ?: Calendar
                        .getInstance()
                        .get(Calendar.DAY_OF_MONTH)
                ).show()
            }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formattedDate.value,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp)
                    .background(Color.Transparent)
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.Start
            )
            Icon(
                imageVector = Icons.Filled.CalendarToday,
                contentDescription = "Calendar",
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
                    .clickable {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                val selectedDate = Calendar.getInstance()
                                selectedDate.set(year, month, dayOfMonth)
                                formattedDate.value =
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                        selectedDate.time
                                    )
                                onDateSelected(selectedDate)
                            },
                            date?.get(Calendar.YEAR) ?: Calendar
                                .getInstance()
                                .get(Calendar.YEAR),
                            date?.get(Calendar.MONTH) ?: Calendar
                                .getInstance()
                                .get(Calendar.MONTH),
                            date?.get(Calendar.DAY_OF_MONTH) ?: Calendar
                                .getInstance()
                                .get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
            )
        }
    }
}

@Composable
fun ScanBarcodeButton(addingViewModel: AddingViewModel) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract = ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            addingViewModel.getIngredientByBarcode(result.contents)
        }
    }
    Button(
        onClick = {
            launcher.launch(ScanOptions())
        },
    ) {
        Text(
            text = stringResource(id = R.string.btn_scan_qr),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddingScreenPreview() {
    AddingScreen(rememberNavController())
}
