package com.example.prolens.view

import android.app.Activity
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prolens.repository.UserRepoImpl
import com.example.prolens.ui.theme.SoftGrey
import com.example.prolens.ui.theme.SoftWhite
import com.example.prolens.viewmodel.UserViewModel

class ForgetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ForgetPasswordBody()
        }
    }
}

@Composable
fun ForgetPasswordBody() {
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    val context = LocalContext.current
    val activity = context as Activity

    var email by remember { mutableStateOf("") }

    val backgroundGray = SoftGrey
    val cardGray = SoftWhite
    val primaryGray = Color(0xFF444444)
    val lightGray = Color(0xFF888888)
    val buttonGray = Color(0xFF5E5E5E) // Gray button

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
                    text = "Forgot Password",
                    fontSize = 22.sp,
                    color = primaryGray,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enter your email to reset your password",
                    fontSize = 14.sp,
                    color = lightGray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("abc@gmail.com") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryGray,
                        unfocusedBorderColor = lightGray,
                        focusedContainerColor = cardGray,
                        unfocusedContainerColor = cardGray,
                        focusedTextColor = primaryGray,
                        unfocusedTextColor = primaryGray
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        userViewModel.forgetPassword(email) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonGray)
                ) {
                    Text("Send Reset Email", color = Color.White, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Clickable Log In text
                Text(
                    text = "Remembered your password? Log In",
                    color = primaryGray,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clickable {
                            context.startActivity(Intent(context, LoginActivity::class.java))
                            activity.finish()
                        }
                        .padding(top = 16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}




