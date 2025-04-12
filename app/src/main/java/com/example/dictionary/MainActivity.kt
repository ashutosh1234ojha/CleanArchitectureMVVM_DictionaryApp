package com.example.dictionary

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dictionary.feature_dictionary.presentation.WordInfoItem
import com.example.dictionary.feature_dictionary.presentation.WordInfoViewModel
import com.example.dictionary.ui.theme.DictonaryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DictonaryTheme {
                val viewModel: WordInfoViewModel = hiltViewModel()
                val state = viewModel.state.value
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                LaunchedEffect(key1 = true) {
                    viewModel.eventFlow.collectLatest {
                        when (it) {
                            is WordInfoViewModel.UIEvent.ShowSnackBar -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = it.message,
                                        actionLabel = "Click me",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        }
                    }
                }

                Scaffold() {
                    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                        ) {


                            TextField(
                                value = viewModel.searchQuery.value,
                                onValueChange = viewModel::onSearch,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text("Search...")
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            LazyColumn(modifier = Modifier.fillMaxSize()) {

                                items(state.wordInfo.size) { i ->
                                    val wordInfo = state.wordInfo[i]
                                    if (i > 0) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    WordInfoItem(wordInfo = wordInfo)

                                    if (i < state.wordInfo.size - 1) {
                                        HorizontalDivider()
                                    }

                                }

                            }
                            if (state.isLoading) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                }


            }
        }


    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DictonaryTheme {
        Greeting("Android")
    }
}