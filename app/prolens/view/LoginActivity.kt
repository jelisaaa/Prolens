package com.example.prolens.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import androidx.compose.ui.window.DialogProperties
import androidx.lint.kotlin.metadata.Visibility
import com.example.prolens.R
import com.example.prolens.model.UserModel
import com.example.prolens.repository.UserRepoImpl
import com.example.prolens.ui.theme.Blue
import com.example.prolens.ui.theme.PurpleGrey80
import com.example.prolens.ui.theme.WhitePoint
import com.example.prolens.view.ui.theme.ProLensTheme



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

    val keyCo = LocalSoftwareKeyboardController.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }

//    val loginViewModel = remember { UserViewModel(UserRepoImpl()) }

    val context = LocalContext.current

    val activity = context as Activity

//    val userViewModel = remember { UserViewModel(UserRepoImpl()) }

    val snackbarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope ()

    var showDialog by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(false) }


    val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
    Scaffold (
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ){ padding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(WhitePoint)
        ) {
            item {
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialog = false
                        },
                        confirmButton = {
                            Text("Ok")
                        },
                        dismissButton = {
                            Text("Cancel")
                        },
                        title = {
                            Text("Confirm")
                        },
                        text = {
                            Text("Are you sure you want  to delete")
                        },
                        properties = DialogProperties(
                            dismissOnBackPress = true,
                            dismissOnClickOutside = true
                        )
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    "Sign In",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Blue,
                        fontSize = 24.sp
                    )
                )

                Text(
                    "This is ecommerce, please login. Here you can buy multiple products at our application. Limited stock",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 15.dp),
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        color = Color.Gray.copy(0.7f)
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                ) {
                    SocialMediaCard(
                        Modifier
                            .height(60.dp)
                            .weight(1f),
                        R.drawable.face,
                        "Facebook"
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    SocialMediaCard(
                        Modifier
                            .height(60.dp)
                            .weight(1f),
                        R.drawable.gmail,
                        "Gmail"
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 25.dp, horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    HorizontalDivider(
                        modifier = Modifier.weight(1f)
                    )
                    Text("OR", modifier = Modifier.padding(horizontal = 15.dp))
                    HorizontalDivider(
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = email,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    onValueChange = { data ->
                        email = data
                    },
                    placeholder = {
                        Text("jelly@gmail.com")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = PurpleGrey80,
                        unfocusedContainerColor = PurpleGrey80,
                        focusedIndicatorColor = Blue,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    value = password,

                    onValueChange = { data ->
                        password = data
                    },
                    visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = {
                            visibility = !visibility
                        }) {
                            Icon(
                                painter = if (visibility)
                                    painterResource(R.drawable.baseline_visibility_24)
                                else
                                    painterResource(R.drawable.baseline_visibility_off_24),
                                contentDescription = null
                            )

                        }
                    },
                    placeholder = {
                        Text("********")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = PurpleGrey80,
                        unfocusedContainerColor = PurpleGrey80,
                        focusedIndicatorColor = Blue,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Text(
                    "Forget Password?",
                    style = TextStyle(textAlign = TextAlign.End),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Forget Password?", modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp), style = TextStyle(textAlign = TextAlign.End)
                )
                Button(
                    onClick = {
//                    keyCo?.hide()
//                    userViewModel.login(email,password) { success, message ->
//                        if (success){
//                            val intent = Intent(
//                                context, DashboardActivity::class.java
//                            )
//                            context.startActivity(intent)
//                            activity.finish()
//                        }else{
//                            Toast.makeText(
//                                context,
//                                message,
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
//                    val intent = Intent(
//                        context, DashboardActivity::class.java
//                    )
//                    intent.putExtra("email",email)
//                    intent.putExtra("password",password)
//
//                    context.startActivity(intent)
//                    activity.finish()
//                    loginViewModel.login(email, password){
//                            success, message->
//                        if (success){
//                            val model = UserModel(
//                                email = email
//                            )
//                            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
//                            val intent = Intent(
//                                context, DashboardActivity::class.java
//                            )
//                            context.startActivity(intent)
//                            activity.finish()
//                        }else{
//                            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
//                        }
//
//                    }


//                    showDialog = true
//                    if (email == "ram" && password == "password") {
//                        Toast.makeText(
//                            context,
//                            "Login successfully",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    } else {
//                        coroutineScope.launch {
//                            snackbarHostState.showSnackbar(
//                                "Invalid email or password",
//                                withDismissAction = true
//                            )
//                        }
//                    }

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp).padding(horizontal = 15.dp)
                ) {
                    Text("Log In")
                }

                Text(buildAnnotatedString {
                    append("Don't have an account?")


                    withStyle(style = SpanStyle(color = Blue)) {
                        append(" Sign Up")
                    }
                }, modifier = Modifier.clickable {
                    val intent = Intent(
                        context,
                        RegistrationActivity::class.java
                    )

                    context.startActivity(intent)

                    activity.finish()
                })


            }


        }

    }
}

@Composable
fun SocialMediaCard(modifier: Modifier, image: Int, label: String) {
    Card(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(label)
        }
    }
}

@Preview
@Composable
fun PreviewLogin() {
    LoginBody()
}