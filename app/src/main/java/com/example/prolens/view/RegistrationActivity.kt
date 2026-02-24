package com.example.prolens.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prolens.R
import com.example.prolens.model.UserModel
import com.example.prolens.repository.UserRepoImpl
import com.example.prolens.viewmodel.UserViewModel
import java.util.Calendar

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegisterBody()
        }
    }
}

@Composable
fun RegisterBody() {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }

    var visibility by remember { mutableStateOf(false) }
    var terms by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }

    val primaryGray = Color(0xFF444444)
    val lightGray = Color(0xFF888888)
    val buttonGray = Color(0xFF5E5E5E)

    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context,
        { _, y, m, d -> selectedDate = "$d/${m + 1}/$y" },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF2F2F2), Color(0xFFE0E0E0))
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

                Spacer(modifier = Modifier.height(60.dp))

                Text(
                    text = "Create Account",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryGray
                )

                Text(
                    text = "Join ProLens Photography Rental",
                    fontSize = 14.sp,
                    color = lightGray
                )

                Spacer(modifier = Modifier.height(30.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            CustomInputFieldGray(
                                value = firstName,
                                onValueChange = { firstName = it },
                                label = "First Name",
                                modifier = Modifier.weight(1f)
                            )

                            CustomInputFieldGray(
                                value = lastName,
                                onValueChange = { lastName = it },
                                label = "Last Name",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        CustomInputFieldGray(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email",
                            keyboardType = KeyboardType.Email
                        )

                        // Birthday Picker
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { datePicker.show() }
                        ) {
                            OutlinedTextField(
                                value = selectedDate,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Birthday") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { datePicker.show() },
                                shape = RoundedCornerShape(16.dp),
                                trailingIcon = {
                                    IconButton(onClick = { datePicker.show() }) {
                                        Icon(
                                            painter = painterResource(R.drawable.baseline_calendar_today_24),
                                            contentDescription = "Select Date",
                                            tint = primaryGray
                                        )
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = primaryGray,
                                    unfocusedBorderColor = lightGray
                                )
                            )
                        }

                        CustomInputFieldGray(
                            value = password,
                            onValueChange = { password = it },
                            label = "Password",
                            isPassword = true,
                            isPasswordVisible = visibility
                        ) {
                            visibility = !visibility
                        }

                        CustomInputFieldGray(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = "Confirm Password",
                            isPassword = true,
                            isPasswordVisible = visibility
                        ) {
                            visibility = !visibility
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = terms,
                        onCheckedChange = { terms = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = primaryGray,
                            uncheckedColor = lightGray
                        )
                    )

                    Text(
                        buildAnnotatedString {
                            append("I agree to the ")
                            withStyle(
                                style = SpanStyle(
                                    color = primaryGray,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Terms & Conditions")
                            }
                        },
                        fontSize = 13.sp,
                        color = primaryGray
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                if (isLoading) {
                    CircularProgressIndicator(color = primaryGray)
                } else {
                    Button(
                        onClick = {
                            if (email.isEmpty() || password.isEmpty() || firstName.isEmpty()) {
                                Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                            } else if (password != confirmPassword) {
                                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                            } else if (!terms) {
                                Toast.makeText(context, "Please accept Terms & Conditions", Toast.LENGTH_SHORT).show()
                            } else {
                                isLoading = true
                                userViewModel.register(email, password) { success, message, userId ->
                                    if (success) {
                                        val model = UserModel(userId, email, firstName, lastName, selectedDate)
                                        userViewModel.addUserToDatabase(userId, model) { dbSuccess, dbMsg ->
                                            isLoading = false
                                            Toast.makeText(context, dbMsg, Toast.LENGTH_LONG).show()
                                            if (dbSuccess) {
                                                context.startActivity(Intent(context, LoginActivity::class.java))
                                            }
                                        }
                                    } else {
                                        isLoading = false
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = buttonGray)
                    ) {
                        Text("CREATE ACCOUNT", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    buildAnnotatedString {
                        append("Already have an account? ")
                        withStyle(
                            style = SpanStyle(
                                color = primaryGray,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Log In")
                        }
                    },
                    modifier = Modifier.clickable {
                        context.startActivity(
                            Intent(context, LoginActivity::class.java)
                        )
                    },
                    color = lightGray
                )

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@Composable
fun CustomInputFieldGray(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onVisibilityToggle: () -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword && !isPasswordVisible)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = onVisibilityToggle) {
                    Icon(
                        painter = painterResource(
                            if (isPasswordVisible)
                                R.drawable.baseline_visibility_24
                            else
                                R.drawable.baseline_visibility_off_24
                        ),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF444444),
            unfocusedBorderColor = Color(0xFF888888)
        )
    )
}

@Preview
@Composable
fun PreviewRegister() {
    RegisterBody()
}