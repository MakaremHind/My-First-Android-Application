package com.automacorp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import com.automacorp.ui.theme.AutomacorpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AutomacorpTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        onClick = { name -> println("Button clicked with name: $name") },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_logo),
        contentDescription = stringResource(R.string.app_logo_description),
        modifier = modifier
            .padding(top = 32.dp)
            .height(80.dp)
            .fillMaxWidth()
    )
}

@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    name: String,
    onClick: (name: String) -> Unit = {}
) {
    var inputName by remember { mutableStateOf("") } // State for the input field

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display the App Logo
        AppLogo()

        // Welcome Text
        Text(
            text = stringResource(R.string.act_main_welcome) + "\nHello $name!", // Use the name parameter
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )

        // Text Field with placeholder and icon
        OutlinedTextField(
            value = inputName,
            onValueChange = { inputName = it }, // Update state with user input
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            placeholder = {
                Row {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = stringResource(R.string.act_main_fill_name),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(stringResource(R.string.act_main_fill_name))
                }
            }
        )

        // Button to handle the onClick event
        Button(
            onClick = { onClick(inputName) }, // Pass the user input to the click handler
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Submit")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AutomacorpTheme {
        Greeting(
            name = "Preview",
            onClick = { name -> println("Preview clicked with name: $name") }
        )
    }
}
