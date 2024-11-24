package com.automacorp

import android.os.Bundle
import android.widget.Toast
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
import com.automacorp.ui.theme.AutomacorpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Action to do when the button is clicked
        val onSayHelloButtonClick: (name: String) -> Unit = { name ->
            Toast.makeText(baseContext, "Hello $name", Toast.LENGTH_LONG).show()
        }

        setContent {
            AutomacorpTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        onClick = onSayHelloButtonClick, // Pass the action to Greeting
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
    onClick: (name: String) -> Unit
) {
    var inputName by remember { mutableStateOf("") } // State to manage text input

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Logo
        AppLogo()

        // Welcome Text
        Text(
            text = stringResource(R.string.act_main_welcome),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )

        // Text Field for Input
        OutlinedTextField(
            value = inputName,
            onValueChange = { inputName = it },
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(stringResource(R.string.act_main_fill_name))
            }
        )

        // Button to Display the Name
        Button(
            onClick = { onClick(inputName) }, // Pass inputName to the onClick function
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.act_main_open)) // Button text: "Open"
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AutomacorpTheme {
        Greeting(
            onClick = { name -> println("Preview clicked with name: $name") }
        )
    }
}
