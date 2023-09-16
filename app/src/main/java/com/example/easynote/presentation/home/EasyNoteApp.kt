package com.example.easynote.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.example.easynote.data.model.Note
import com.example.easynote.navigation.EasyNoteBottomNavigationBar
import com.example.easynote.navigation.EasyNoteNavigationActions
import com.example.easynote.navigation.EasyNoteNavigationRail
import com.example.easynote.navigation.EasyNoteRoute
import com.example.easynote.navigation.EasyNoteTopLevelDestination
import com.example.easynote.navigation.ModalNavigationDrawerContent
import com.example.easynote.navigation.PermanentNavigationDrawerContent
import com.example.easynote.util.DevicePosture
import com.example.easynote.util.EasyNoteContentType
import com.example.easynote.util.EasyNoteNavigationContentPosition
import com.example.easynote.util.EasyNoteNavigationType
import com.example.easynote.util.isBookPosture
import com.example.easynote.util.isSeparating
import kotlinx.coroutines.launch

@Composable
fun EasyNoteApp(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    homeUiState: HomeUiState,
    onFavouriteChange: (note: Note) -> Unit,
    onNoteDelete: (id: Long) -> Unit,
    navigateToDetails: (Long, EasyNoteContentType) -> Unit,
    closeDetailScreen: () -> Unit = {}
) {
    val navigationType: EasyNoteNavigationType
    val contentType: EasyNoteContentType
    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()
    val foldingDevicePosture = when {
        isBookPosture(foldingFeature) ->
            DevicePosture.BookPosture(foldingFeature.bounds)
        isSeparating(foldingFeature) ->
            DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)
        else -> DevicePosture.NormalPosture
    }

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = EasyNoteNavigationType.BOTTOM_NAVIGATION
            contentType = EasyNoteContentType.SINGLE_PANE
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = EasyNoteNavigationType.NAVIGATION_RAIL
            contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
                EasyNoteContentType.DUAL_PANE
            } else {
                EasyNoteContentType.SINGLE_PANE
            }
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                EasyNoteNavigationType.NAVIGATION_RAIL
            } else {
                EasyNoteNavigationType.PERMANENT_NAVIGATION_DRAWER
            }
            contentType = EasyNoteContentType.DUAL_PANE
        }
        else -> {
            navigationType = EasyNoteNavigationType.BOTTOM_NAVIGATION
            contentType = EasyNoteContentType.SINGLE_PANE
        }
    }

    /**
     * Content inside Navigation Rail/Drawer can also be positioned at top, bottom or center for
     * ergonomics and reachability depending upon the height of the device.
     */
    val navigationContentPosition = when (windowSize.heightSizeClass) {
        WindowHeightSizeClass.Compact -> {
            EasyNoteNavigationContentPosition.TOP
        }
        WindowHeightSizeClass.Medium,
        WindowHeightSizeClass.Expanded -> EasyNoteNavigationContentPosition.CENTER
        else -> EasyNoteNavigationContentPosition.TOP
    }

    EasyNoteNavigationWrapper(
        navigationType = navigationType,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationContentPosition = navigationContentPosition,
        onFavouriteChange = onFavouriteChange,
        onNoteDelete = onNoteDelete,
        homeUiState = homeUiState,
        closeDetailScreen = closeDetailScreen,
        navigateToDetails = navigateToDetails,
        onFloatingButtonClicked = {}
    )
}

@Composable
fun EasyNoteNavigationWrapper(
    navigationType: EasyNoteNavigationType,
    contentType: EasyNoteContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: EasyNoteNavigationContentPosition,
    onFavouriteChange: (note: Note) -> Unit,
    onNoteDelete: (Long) -> Unit,
    homeUiState: HomeUiState,
    closeDetailScreen: () -> Unit,
    navigateToDetails: (Long, EasyNoteContentType) -> Unit,
    onFloatingButtonClicked: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        EasyNoteNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination =
        navBackStackEntry?.destination?.route ?: EasyNoteRoute.home

    if (navigationType == EasyNoteNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(drawerContent = {
            PermanentNavigationDrawerContent(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                onFloatingButtonClicked = { onFloatingButtonClicked() }
            )
        }) {
            EasyNoteContent(
                navigateToDetails = navigateToDetails,
                navigationType = navigationType,
                selectedDestination = selectedDestination,
                onFloatingButtonClicked = { onFloatingButtonClicked() },
                navigateToTopLevelDestination = navigationActions::navigateTo,
                navController = navController,
                onNoteDelete = onNoteDelete,
                closeDetailScreen = closeDetailScreen,
                contentType = contentType,
                displayFeatures = displayFeatures,
                onFavouriteChange = onFavouriteChange,
                navigationContentPosition = navigationContentPosition,
                homeUiState = homeUiState
            )
        }
    } else {
        ModalNavigationDrawer(
            drawerContent = {
                ModalNavigationDrawerContent(
                    selectedDestination = selectedDestination,
                    navigationContentPosition = navigationContentPosition,
                    navigateToTopLevelDestination = navigationActions::navigateTo,
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    onFloatingButtonClicked = onFloatingButtonClicked
                )
            }, drawerState = drawerState
        ) {
            EasyNoteContent(
                navigateToDetails = navigateToDetails,
                navigationType = navigationType,
                selectedDestination = selectedDestination,
                onFloatingButtonClicked = { onFloatingButtonClicked() },
                navigateToTopLevelDestination = navigationActions::navigateTo,
                navController = navController,
                onNoteDelete = onNoteDelete,
                closeDetailScreen = closeDetailScreen,
                contentType = contentType,
                displayFeatures = displayFeatures,
                onFavouriteChange = onFavouriteChange,
                navigationContentPosition = navigationContentPosition,
                homeUiState = homeUiState,
                onDrawerClicked = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    }
}

@Composable
fun EasyNoteContent(
    modifier: Modifier = Modifier,
    navigateToDetails: (Long, EasyNoteContentType) -> Unit,
    navigationType: EasyNoteNavigationType,
    selectedDestination: String,
    onFloatingButtonClicked: () -> Unit,
    navigateToTopLevelDestination: (EasyNoteTopLevelDestination) -> Unit,
    onDrawerClicked: () -> Unit = {},
    navController: NavHostController,
    onNoteDelete: (Long) -> Unit,
    closeDetailScreen: () -> Unit,
    contentType: EasyNoteContentType,
    displayFeatures: List<DisplayFeature>,
    onFavouriteChange: (note: Note) -> Unit,
    navigationContentPosition: EasyNoteNavigationContentPosition,
    homeUiState: HomeUiState,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = navigationType == EasyNoteNavigationType.NAVIGATION_RAIL) {
            EasyNoteNavigationRail(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                onDrawerClicked = onDrawerClicked,
                onFloatingButtonClicked = onFloatingButtonClicked
            )
        }
        Column(modifier = modifier.fillMaxSize()) {
            EasyNoteNavHost(
                homeUiState = homeUiState,
                modifier = Modifier.weight(1f),
                navController = navController,
                onFavouriteChange = onFavouriteChange,
                onNoteDelete = onNoteDelete,
                closeDetailScreen = closeDetailScreen,
                navigateToDetails = navigateToDetails,
                onFloatingButtonClicked = { onFloatingButtonClicked() },
                displayFeatures = displayFeatures,
                contentType = contentType,
                navigationType = navigationType
            )
            AnimatedVisibility(visible = navigationType == EasyNoteNavigationType.BOTTOM_NAVIGATION) {
                EasyNoteBottomNavigationBar(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination
                )
            }
        }
    }
}

@Composable
fun EasyNoteNavHost(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState,
    navController: NavHostController,
    onFavouriteChange: (note: Note) -> Unit,
    onNoteDelete: (Long) -> Unit,
    closeDetailScreen: () -> Unit,
    navigateToDetails: (Long, EasyNoteContentType) -> Unit,
    onFloatingButtonClicked: () -> Unit,
    displayFeatures: List<DisplayFeature>,
    contentType: EasyNoteContentType,
    navigationType: EasyNoteNavigationType
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = EasyNoteRoute.home
    ) {
        composable(EasyNoteRoute.home) {
            HomeScreen(
                homeUiState = homeUiState,
                onFavouriteChange = onFavouriteChange,
                onNoteDelete = onNoteDelete,
                onAddNoteClicked = onFloatingButtonClicked,
                onNoteClicked = navigateToDetails,
                closeDetailScreen = closeDetailScreen,
                displayFeatures = displayFeatures,
                contentType = contentType,
                navigationType = navigationType
            )
        }
        composable(EasyNoteRoute.favourites) {
            FavouritesScreen(
                homeUiState = homeUiState,
                onFavouriteChange = onFavouriteChange,
                onDeleteNote = onNoteDelete,
                onNoteClicked = {}
            )
        }
    }
}