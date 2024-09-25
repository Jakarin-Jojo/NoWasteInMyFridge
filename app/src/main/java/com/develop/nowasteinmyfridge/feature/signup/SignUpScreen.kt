package com.develop.nowasteinmyfridge.feature.signup

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.develop.nowasteinmyfridge.R
import com.develop.nowasteinmyfridge.Screen
import com.develop.nowasteinmyfridge.data.model.UserCreate
import com.develop.nowasteinmyfridge.util.Result

@Composable
fun SignupScreen(
    navController: NavHostController,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
) {
    var userCreate by remember { mutableStateOf(UserCreate("", "", "", "", "", "", "")) }
    val state by signUpViewModel.createUserResult.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // firstName
        OutlinedTextField(
            value = userCreate.firstName,
            onValueChange = { userCreate = userCreate.copy(firstName = it) },
            label = { Text(text = stringResource(id = R.string.label_first_name_user_profile)) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        )

        // lastName
        OutlinedTextField(
            value = userCreate.lastName,
            onValueChange = { userCreate = userCreate.copy(lastName = it) },
            label = { Text(text = stringResource(id = R.string.label_last_name_user_profile)) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        )

        // Email
        OutlinedTextField(
            value = userCreate.email,
            onValueChange = { userCreate = userCreate.copy(email = it) },
            label = { Text(text = stringResource(id = R.string.label_email_user_profile)) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = null)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        )

        // password
        OutlinedTextField(
            value = userCreate.password,
            onValueChange = { userCreate = userCreate.copy(password = it) },
            label = { Text(text = stringResource(id = R.string.label_password)) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Password, contentDescription = null)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        )

        // gender
        OutlinedTextField(
            value = userCreate.gender,
            onValueChange = { userCreate = userCreate.copy(gender = it) },
            label = { Text(text = stringResource(id = R.string.label_gender_user_profile)) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Male, contentDescription = null)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        )

        // Birthday
        OutlinedTextField(
            value = userCreate.birthday,
            onValueChange = { userCreate = userCreate.copy(birthday = it) },
            label = { Text(text = stringResource(id = R.string.label_birthday_user_profile)) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Cake, contentDescription = null)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        )

        // Signup Button
        if (state is Result.Loading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) { CircularProgressIndicator() }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(
                        onClick = { signUpViewModel.createUser(userCreate) },
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .height(48.dp),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(id = R.string.btn_signup))
                    }
                }
            }

        }
        if (state is Result.Success) {
            Toast.makeText(context, "Sign up Success", Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.LoginScreenRoute.route)
        } else if (state is Result.Error) {
            val errorMessage = (state as Result.Error).exception.message
            Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    SignupScreen(rememberNavController())
}