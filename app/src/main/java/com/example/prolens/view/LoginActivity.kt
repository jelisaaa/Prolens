package com.example.prolens.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prolens.R
import com.example.prolens.model.UserModel
import com.example.prolens.repository.UserRepoImpl
import com.example.prolens.viewmodel.UserViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginBody()
        }
    }
}

@Composable
fun LoginBody() {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }

    val loginViewModel = remember { UserViewModel(UserRepoImpl()) }
    val context = LocalContext.current
    val activity = context as Activity

    val backgroundGray = Color(0xFFF2F2F2)
    val cardGray = Color(0xFFFFFFFF)
    val primaryGray = Color(0xFF444444)
    val lightGray = Color(0xFF888888)
    val buttonGray = Color(0xFF5E5E5E)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGray),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = cardGray),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Welcome to ProLens",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGray
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Photography Equipment Rental",
                    fontSize = 14.sp,
                    color = lightGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    placeholder = { Text("Enter Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryGray,
                        unfocusedBorderColor = lightGray
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    visualTransformation = if (visibility)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = {
                            visibility = !visibility
                        }) {
                            Icon(
                                painter = if (visibility)
                                    painterResource(R.drawable.baseline_visibility_24)
                                else
                                    painterResource(R.drawable.baseline_visibility_off_24),
                                contentDescription = null,
                                tint = primaryGray
                            )
                        }
                    },
                    placeholder = { Text("Enter Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryGray,
                        unfocusedBorderColor = lightGray
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Forgot Password?",
                    color = primaryGray,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
                            val intent =
                                Intent(context, ForgetPasswordActivity::class.java)
                            context.startActivity(intent)
                        }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        loginViewModel.login(email, password) { success, message ->
                            if (success) {
                                val model = UserModel(email = email)
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                val intent =
                                    Intent(context, DashboardActivity::class.java)
                                context.startActivity(intent)
                                activity.finish()
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonGray
                    )
                ) {
                    Text("Log In", color = Color.White)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    buildAnnotatedString {
                        append("Don’t have an account? ")
                        withStyle(style = SpanStyle(color = primaryGray)) {
                            append("Sign Up")
                        }
                    },
                    color = lightGray,
                    modifier = Modifier.clickable {
                        val intent =
                            Intent(context, RegistrationActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewLogin() {
    LoginBody()
}