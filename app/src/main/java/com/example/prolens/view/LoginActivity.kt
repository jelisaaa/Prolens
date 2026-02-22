package com.example.prolens.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
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
import com.example.prolens.repository.UserRepoImpl
import com.example.prolens.ui.theme.Blue
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
    val activity = context as? Activity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A237E), Color(0xFF121212))
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(100.dp))

                // BRANDING SECTION
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = "Logo",
                    modifier = Modifier.size(80.dp),
                    tint = Blue
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "ProLens",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Rent the best gear today",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(48.dp))

                // INPUT FIELDS
                CustomLoginField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    keyboardType = KeyboardType.Email,
                    leadingIcon = R.drawable.baseline_email_24
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomLoginField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    isPassword = true,
                    isPasswordVisible = visibility,
                    onVisibilityToggle = { visibility = !visibility }
                )

                // Wrap the button in a Column or Box so we can use horizontalAlignment or align
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    TextButton(
                        onClick = { /* Navigate to Forget Password */ }
                    ) {
                        Text("Forgot Password?", color = Blue, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // BUTTON
                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            loginViewModel.login(email, password) { success, message ->
                                if (success) {
                                    Toast.makeText(context, "Welcome!", Toast.LENGTH_SHORT).show()
                                    context.startActivity(Intent(context, DashboardActivity::class.java))
                                    activity?.finish()
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue)
                ) {
                    Text("SIGN IN", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // SIGN UP LINK
                Text(
                    text = buildAnnotatedString {
                        append("Don't have an account? ")
                        withStyle(style = SpanStyle(color = Blue, fontWeight = FontWeight.Bold)) {
                            append("Sign Up")
                        }
                    },
                    modifier = Modifier.clickable {
                        context.startActivity(Intent(context, RegistrationActivity::class.java))
                        activity?.finish()
                    },
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@Composable
fun CustomLoginField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onVisibilityToggle: () -> Unit = {},
    leadingIcon: Int? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        leadingIcon = leadingIcon?.let { { Icon(painterResource(it), null, tint = Blue) } },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = onVisibilityToggle) {
                    Icon(
                        painter = painterResource(
                            if (isPasswordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24
                        ),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White.copy(0.05f),
            focusedContainerColor = Color.White.copy(0.05f),
            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White,
            focusedIndicatorColor = Blue,
            unfocusedIndicatorColor = Color.Gray.copy(0.5f)
        )
    )
}

@Preview
@Composable
fun PreviewLogin() {
    LoginBody()
}
