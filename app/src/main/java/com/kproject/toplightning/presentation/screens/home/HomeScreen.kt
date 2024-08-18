package com.kproject.toplightning.presentation.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kproject.toplightning.R
import com.kproject.toplightning.domain.model.Node
import com.kproject.toplightning.domain.model.sampleNodeList
import com.kproject.toplightning.presentation.screens.components.Image
import com.kproject.toplightning.presentation.screens.home.components.ListSorterBottomSheet
import com.kproject.toplightning.presentation.theme.PreviewTheme
import com.kproject.toplightning.presentation.utils.Utils
import com.kproject.toplightning.presentation.utils.ViewState

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        uiState = uiState,
        onUiAction = homeViewModel::onUiAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    onUiAction: (HomeUiAction) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var showListSorterBottomSheet by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    val iconResId = if (uiState.isDarkMode) {
                        R.drawable.baseline_light_mode_24
                    } else {
                        R.drawable.baseline_dark_mode_24
                    }
                    IconButton(onClick = { onUiAction.invoke(HomeUiAction.ChangeAppTheme) }) {
                        Icon(
                            painter = painterResource(id = iconResId),
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            if (uiState.viewState == ViewState.Success) {
                FloatingActionButton(
                    onClick = { showListSorterBottomSheet = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_sort_24),
                        contentDescription = null,
                        modifier = Modifier
                    )
                }
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            AnimatedContent(
                targetState = uiState.viewState,
                transitionSpec = {
                    scaleIn() togetherWith scaleOut()
                },
                label = "MainAnimatedContent"
            ) { viewState ->
                when (viewState) {
                    ViewState.Loading -> {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize(),
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(64.dp))
                        }
                    }

                    ViewState.Success -> {
                        val listState = rememberLazyListState()
                        NodeList(
                            nodeList = uiState.nodeList,
                            listState = listState,
                            isRefreshing = uiState.isRefreshingNodeList,
                            onRefreshList = { onUiAction.invoke(HomeUiAction.RefreshList) }
                        )

                        ListSorterBottomSheet(
                            showBottomSheet = showListSorterBottomSheet,
                            onDismiss = { showListSorterBottomSheet = false },
                            selectedSortOption = uiState.sortListBy,
                            onSort = { sortListBy ->
                                onUiAction.invoke(HomeUiAction.ChangeListOrder(sortListBy))
                                showListSorterBottomSheet = false
                                listState.requestScrollToItem(0)
                            }
                        )
                    }

                    ViewState.Error -> {
                        ErrorContent(onRetry = { onUiAction.invoke(HomeUiAction.RefreshList) })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NodeList(
    nodeList: List<Node>,
    listState: LazyListState,
    isRefreshing: Boolean,
    onRefreshList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefreshList,
        modifier = modifier
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            itemsIndexed(
                items = nodeList,
                key = { _, node -> node.publicKey }
            ) { index, node ->
                NodeListItem(
                    index = index + 1,
                    node = node,
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}

@Composable
private fun NodeListItem(
    index: Int,
    node: Node,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        val spacerHeight = 16.dp
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
        ) {
            Alias(
                alias = node.alias,
                ranking = index
            )

            Spacer(Modifier.height(spacerHeight + 6.dp))

            CountryAndCity(node = node)

            Spacer(Modifier.height(spacerHeight))

            CommonNodeItem(
                iconResId = R.drawable.baseline_language_24,
                title = stringResource(id = R.string.channels),
                value = node.channels.toString()
            )

            Spacer(Modifier.height(spacerHeight))

            val formattedCapacity = remember {
                Utils.convertSatoshiToBitcoin(node.capacity.toString())
            }
            CommonNodeItem(
                iconResId = R.drawable.baseline_currency_bitcoin_24,
                title = stringResource(id = R.string.capacity),
                value = formattedCapacity
            )

            Spacer(Modifier.height(spacerHeight))

            SelectionContainer {
                CommonNodeItem(
                    iconResId = R.drawable.baseline_key_24,
                    title = stringResource(id = R.string.public_key),
                    value = node.publicKey
                )
            }

            Spacer(Modifier.height(spacerHeight))

            val lastUpdate = remember {
                Utils.formatDate(node.updatedAt)
            }
            CommonNodeItem(
                iconResId = R.drawable.baseline_update_24,
                title = stringResource(id = R.string.last_update),
                value = lastUpdate,
            )

            Spacer(Modifier.height(spacerHeight))

            val firstSeen = remember {
                Utils.formatDate(node.firstSeen)
            }
            CommonNodeItem(
                iconResId = R.drawable.baseline_calendar_month_24,
                title = stringResource(id = R.string.first_seen),
                value = firstSeen,
            )
        }
    }
}

@Composable
private fun Alias(
    alias: String,
    ranking: Int,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "$ranking",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .circleBackground(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    padding = 12.dp
                )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = alias,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

private fun Modifier.circleBackground(color: Color, padding: Dp): Modifier {
    val backgroundModifier = drawBehind {
        drawCircle(color, size.width / 2f, center = Offset(size.width / 2f, size.height / 2f))
    }

    val layoutModifier = layout { measurable, constraints ->
        val adjustedConstraints = constraints.offset(-padding.roundToPx())
        val placeable = measurable.measure(adjustedConstraints)
        val currentHeight = placeable.height
        val currentWidth = placeable.width
        val newDiameter = maxOf(currentHeight, currentWidth) + padding.roundToPx() * 2
        layout(newDiameter, newDiameter) {
            placeable.placeRelative(
                (newDiameter - currentWidth) / 2,
                (newDiameter - currentHeight) / 2
            )
        }
    }

    return this then backgroundModifier then layoutModifier
}

@Composable
private fun CountryAndCity(
    node: Node,
    modifier: Modifier = Modifier,
) {
    val country = node.country
    val city = node.city
    val countryCode = node.isoCode
    val countryName = country?.ptBr ?: country?.en ?: stringResource(id = R.string.unknown_country)
    val cityName = city?.ptBr ?: city?.en ?: ""
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        val countryFlagImage: Any = if (country != null) {
            "https://flagcdn.com/256x192/${countryCode?.lowercase()}.png"
        } else {
            R.drawable.baseline_flag_24
        }
        Image(
            imageModel = countryFlagImage,
            contentDescription = stringResource(id = R.string.country_flag),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp, 40.dp)
                .clip(RoundedCornerShape(6.dp))
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = countryName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
            city?.let {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = cityName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Composable
private fun CommonNodeItem(
    @DrawableRes iconResId: Int,
    title: String,
    value: String,
    valueFontSize: Int = 16,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            )
            Text(
                text = value,
                fontSize = valueFontSize.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ErrorContent(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.error_animation)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(150.dp)
        )
        Text(
            text = stringResource(id = R.string.error_loading_nodes_data),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(6.dp)
        )
        Spacer(Modifier.height(12.dp))
        Button(onClick = onRetry) {
            Text(
                text = stringResource(id = R.string.try_again)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun HomeContentPreview() {
    PreviewTheme {
        HomeContent(
            uiState = HomeUiState(
                viewState = ViewState.Success,
                nodeList = sampleNodeList
            ),
            onUiAction = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun ErrorContentPreview() {
    PreviewTheme {
        ErrorContent(
            onRetry = {}
        )
    }
}

@Preview
@Composable
private fun NodeListItemPreview() {
    PreviewTheme {
        NodeListItem(
            index = 1,
            node = sampleNodeList.first(),
        )
    }
}