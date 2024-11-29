package com.automacorp

import android.content.Intent
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
import com.automacorp.ui.theme.AutomacorpTheme

class MainActivity : ComponentActivity() {

    companion object {
        const val ROOM_PARAM = "com.automacorp.room.attribute"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Action to do when the button is clicked
        val onSayHelloButtonClick: (name: String) -> Unit = { name ->
            val intent = Intent(this, RoomActivity::class.java).apply {
                putExtra(ROOM_PARAM, name)
            }
            startActivity(intent)
        }

        setContent {
            AutomacorpTheme {
                Scaffold(
                    topBar = { AutomacorpTopAppBar(null) }, // Add the top app bar
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Greeting(
                        onClick = onSayHelloButtonClick,
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
    var inputName by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppLogo()

        Text(
            text = stringResource(R.string.act_main_welcome),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )

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

        Button(
            onClick = { onClick(inputName) },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.act_main_open))
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
