package com.awt.retrofittest.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.awt.retrofittest.R
import com.awt.retrofittest.network.RetroTrack
import com.awt.retrofittest.network.RetroTracks
import retrofit2.Response

@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    retryAction: () -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    when (homeUiState) {
        is HomeUiState.Success -> TrackGrid(homeUiState.tracks, windowSizeClass, modifier)
        is HomeUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        else -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun TrackGrid(
    tracks: Response<RetroTracks>,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val gridColumns: Int = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 2
        WindowWidthSizeClass.Medium -> 3
        else -> 4
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(gridColumns),
        modifier = modifier.padding(horizontal = 2.dp),
        contentPadding = contentPadding
    ) {
        items(
            items = tracks.body()!!.results
        ) { track ->
            TrackCard(
                track = track,
                modifier = modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun TrackCard(
    track: RetroTrack,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        TrackArtwork(track.artworkUrl100)
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = track.trackName,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = track.artistName,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = track.collectionName,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TrackArtwork(
    artwork: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(artwork)
            .crossfade(true)
            .build(),
        error = painterResource(R.drawable.ic_broken_image),
        placeholder = painterResource(R.drawable.loading_img),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading),
        modifier = modifier.size(200.dp)
    )
}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_connection_error),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.failed_to_load),
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}