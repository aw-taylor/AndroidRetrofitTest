package com.awt.retrofittest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.awt.retrofittest.ui.home.HomeScreen
import com.awt.retrofittest.ui.home.HomeViewModel
import com.awt.retrofittest.ui.theme.RetrofitTestTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetrofitTestTheme {
                RetrofitApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun RetrofitApp(
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var searchTerm by rememberSaveable { mutableStateOf("") }
    searchTerm = searchTerm.replace(" ", "+")
    var selectedCountry by remember { mutableIntStateOf(0) }
    val countryOptions = listOf("GB", "ES", "FR", "DE", "IT")
    var searchCountry by rememberSaveable { mutableStateOf("GB") }
    var selectedLimit by remember { mutableIntStateOf(2) }
    val limitOptions = listOf(6, 12, 18, 24, 30)
    var searchLimit by rememberSaveable { mutableIntStateOf(18) }
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { RetrofitTopAppBar(
            scrollBehavior = scrollBehavior,
            showBottomSheet = { showBottomSheet = true },
            retryAction = { homeViewModel.getTrackData(
                searchTerm,
                searchCountry,
                searchLimit
            ) }
        ) }
    ) { innerPadding ->
        HomeScreen(
            homeUiState = homeViewModel.homeUiState,
            retryAction = { homeViewModel.getTrackData(
                searchTerm,
                searchCountry,
                searchLimit
            ) },
            windowSizeClass = windowSizeClass,
            modifier = Modifier.padding(innerPadding)
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        stringResource(R.string.search_term),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = searchTerm,
                        onValueChange = { searchTerm = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        stringResource(R.string.search_country),
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                    )
                    SingleChoiceSegmentedButtonRow {
                        countryOptions.forEachIndexed { index, label ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = countryOptions.size
                                ),
                                onClick = {
                                    selectedCountry = index
                                    searchCountry = label
                                },
                                selected = index == selectedCountry,
                                label = { Text(label) }
                            )
                        }
                    }
                    Text(
                        stringResource(R.string.search_limit),
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                    )
                    SingleChoiceSegmentedButtonRow {
                        limitOptions.forEachIndexed { index, label ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = limitOptions.size
                                ),
                                onClick = {
                                    selectedLimit = index
                                    searchLimit = label
                                },
                                selected = index == selectedLimit,
                                label = { Text(label.toString()) }
                            )
                        }
                    }
                    Button(onClick = {
                        scope.launch {
                            sheetState.hide()
                            homeViewModel.getTrackData(
                                searchTerm,
                                searchCountry,
                                searchLimit
                            )
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }) {
                        Text(stringResource(R.string.search))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RetrofitTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    showBottomSheet: () -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            IconButton(onClick = showBottomSheet) {
                Icon(
                    painter = painterResource(R.drawable.search),
                    contentDescription = stringResource(R.string.search)
                )
            }
            IconButton(onClick = retryAction) {
                Icon(
                    painter = painterResource(R.drawable.refresh),
                    contentDescription = stringResource(R.string.refresh)
                )
            }
        },
        modifier = modifier
    )
}