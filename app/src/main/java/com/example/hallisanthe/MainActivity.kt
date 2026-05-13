package com.example.hallisanthe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hallisanthe.data.CartViewModel
import com.example.hallisanthe.data.WishlistViewModel
import com.example.hallisanthe.data.di.AppViewModelFactory
import com.example.hallisanthe.data.viewmodel.AuthViewModel
import com.example.hallisanthe.data.viewmodel.OrderViewModel
import com.example.hallisanthe.data.viewmodel.ProductCatalogViewModel
import com.example.hallisanthe.data.viewmodel.SantheAIViewModel
import com.example.hallisanthe.data.viewmodel.SellerDashboardViewModel
import com.example.hallisanthe.data.LocaleHelper
import com.example.hallisanthe.data.dataStore
import com.example.hallisanthe.ui.screens.*
import com.example.hallisanthe.ui.theme.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: android.content.Context) {
        val langKey = stringPreferencesKey("app_language")
        val savedLang = runBlocking {
            newBase.dataStore.data.map { it[langKey] ?: "en" }.first()
        }
        val wrapped = LocaleHelper.applyLocale(newBase, savedLang)
        super.attachBaseContext(wrapped)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HalliSantheTheme {
                HalliSantheApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalliSantheApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as HalliSantheApplication
    val appContainer = application.appContainer

    val factory = remember {
        AppViewModelFactory(
            application = application,
            productRepository = appContainer.productRepository,
            artisanRepository = appContainer.artisanRepository,
            categoryRepository = appContainer.categoryRepository,
            enquiryRepository = appContainer.enquiryRepository,
            orderRepository = appContainer.orderRepository,
            genAIRepository = appContainer.genAIRepository,
            authRepository = appContainer.authRepository
        )
    }

    val authViewModel: AuthViewModel = viewModel(factory = factory)
    val currentUser by authViewModel.currentUser.collectAsState()

    val cartViewModel: CartViewModel = viewModel(factory = factory)
    val wishlistViewModel: WishlistViewModel = viewModel(factory = factory)
    val productCatalogViewModel: ProductCatalogViewModel = viewModel(factory = factory)
    val sellerDashboardViewModel: SellerDashboardViewModel = viewModel(factory = factory)
    val orderViewModel: OrderViewModel = viewModel(factory = factory)
    val santheAIViewModel: SantheAIViewModel = viewModel(factory = factory)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isSeller = remember { mutableStateOf(false) }

    val protectedRoutes = listOf("home", "search", "cart", "checkout", "profile", "sell", "seller_home", "orders", "wishlist", "notifications", "add_product", "my_listings", "inquiries", "seller_profile", "language", "help", "santhe_ai_assistant", "shop_settings")
    LaunchedEffect(currentUser, currentRoute) {
        if (currentUser == null && currentRoute != null && currentRoute in protectedRoutes) {
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    val topLevelRoutes = listOf("home", "search", "profile", "sell", "seller_home", "add_product")
    val showBottomBar = topLevelRoutes.any { currentRoute == it }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Background,
        topBar = {
            if (currentRoute !in listOf("splash", "onboarding", "login", "santhe_ai_assistant", "seller_onboarding", "seller_login_register")) {
                HalliTopBar(navController = navController, currentRoute = currentRoute, cartCount = cartViewModel.itemCount, isSeller = isSeller.value, sellerDashboardViewModel = sellerDashboardViewModel)
            }
        },
        bottomBar = {
            if (showBottomBar) {
                HalliBottomNav(navController = navController, currentRoute = currentRoute, cartCount = cartViewModel.itemCount, isSeller = isSeller.value)
            }
        }
    ) { paddingValues ->
        NavHost(navController = navController, startDestination = "splash", modifier = Modifier.padding(paddingValues)) {
            composable("splash") { SplashScreen(navController = navController, isLoggedIn = authViewModel.isLoggedIn, isSeller = isSeller.value) }
            composable("onboarding") { OnboardingScreen(navController = navController) }
            composable("login") { 
                LoginRegisterScreen(navController = navController, isSeller = isSeller, authViewModel = authViewModel) 
            }
            composable("home") { HomeScreen(navController = navController, cartViewModel = cartViewModel, wishlistViewModel = wishlistViewModel, catalogViewModel = productCatalogViewModel, authViewModel = authViewModel) }
            composable("seller_home") { SellerDashboardScreen(navController = navController, viewModel = sellerDashboardViewModel, authViewModel = authViewModel) }
            composable("search") { SearchScreen(navController = navController, cartViewModel = cartViewModel, wishlistViewModel = wishlistViewModel, catalogViewModel = productCatalogViewModel) }
            composable("product/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: "p1"
                ProductDetailScreen(navController = navController, productId = productId, cartViewModel = cartViewModel, wishlistViewModel = wishlistViewModel, catalogViewModel = productCatalogViewModel)
            }
            composable("cart") { CartScreen(navController = navController, cartViewModel = cartViewModel) }
            composable("checkout") { CheckoutScreen(navController = navController, cartViewModel = cartViewModel, orderViewModel = orderViewModel) }
            composable("profile") {
                if (isSeller.value) SellerProfileScreen(navController = navController, isSeller = isSeller, viewModel = sellerDashboardViewModel, authViewModel = authViewModel)
                else ProfileScreen(navController = navController, isSeller = isSeller, authViewModel = authViewModel)
            }
            composable("sell") {
                if (isSeller.value) SellerDashboardScreen(navController = navController, viewModel = sellerDashboardViewModel, authViewModel = authViewModel)
                else SellerOnboardingScreen(navController = navController, isSeller = isSeller)
            }
            composable("seller_onboarding") { SellerOnboardingScreen(navController = navController, isSeller = isSeller) }
            composable("seller_login_register") { SellerLoginRegisterScreen(navController = navController, isSeller = isSeller, authViewModel = authViewModel) }
            composable("seller_profile") { SellerProfileScreen(navController = navController, isSeller = isSeller, viewModel = sellerDashboardViewModel, authViewModel = authViewModel) }
            composable("orders") { MyOrdersScreen(navController = navController, orderViewModel = orderViewModel) }
            composable("wishlist") { WishlistScreen(navController = navController, cartViewModel = cartViewModel, wishlistViewModel = wishlistViewModel) }
            composable("notifications") { NotificationsScreen(navController = navController, viewModel = sellerDashboardViewModel) }
            composable("language") { LanguageSettingsScreen(navController = navController) }
            composable("help") { HelpSupportScreen(navController = navController) }
            composable("add_product") { AddProductScreen(navController = navController, genAIRepository = appContainer.genAIRepository, productRepository = appContainer.productRepository, authViewModel = authViewModel) }
            composable("my_listings") { MyListingsScreen(navController = navController, productRepository = appContainer.productRepository, authViewModel = authViewModel) }
            composable("inquiries") { InquiriesScreen(navController = navController, viewModel = sellerDashboardViewModel) }
            composable("santhe_ai_assistant") {
                SantheAIAssistantScreen(
                    navController = navController,
                    viewModel = santheAIViewModel,
                    cartViewModel = cartViewModel,
                    wishlistViewModel = wishlistViewModel,
                    isSeller = isSeller.value
                )
            }
            composable("shop_settings") { ShopSettingsScreen(navController = navController, artisanRepository = appContainer.artisanRepository, authViewModel = authViewModel) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalliTopBar(navController: NavController, currentRoute: String?, cartCount: Int, isSeller: Boolean, sellerDashboardViewModel: SellerDashboardViewModel) {
    val context = LocalContext.current
    val topLevelRoutes = listOf("home", "search", "cart", "profile", "sell", "seller_home")
    val isTopLevel = topLevelRoutes.any { currentRoute == it }

    val title = when (currentRoute) {
        "home" -> "Halli Santhe"
        "search" -> "Browse Crafts"
        "cart" -> "Your Basket"
        "checkout" -> "Checkout"
        "profile" -> "My Account"
        "sell" -> "Sell Your Craft"
        "seller_home" -> "Halli Santhe"
        "my_listings" -> "My Listings"
        "inquiries" -> "Inquiries"
        "santhe_ai_assistant" -> "AI Assistant"
        "add_product" -> "Add Product"
        "shop_settings" -> "Shop Settings"
        "notifications" -> "Notifications"
        "language" -> "Language"
        "help" -> "Help & Support"
        else -> "Halli Santhe"
    }

    TopAppBar(
        title = {
            if (currentRoute == "home" || currentRoute == "seller_home" || currentRoute == "sell") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.foundation.Image(
                        painter = androidx.compose.ui.res.painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(32.dp).padding(end = 8.dp)
                    )
                    Text("Halli Santhe", style = MaterialTheme.typography.titleLarge, color = Primary, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                }
            } else {
                Text(title, style = MaterialTheme.typography.titleLarge, color = OnSurface, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        },
        navigationIcon = {
            if (!isTopLevel) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = OnSurface)
                }
            }
        },
        actions = {
            when (currentRoute) {
                "notifications" -> {
                    TextButton(onClick = { 
                        sellerDashboardViewModel.clearAllNotifications() 
                        Toast.makeText(context, "All Notifications Cleared", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Clear All", color = Primary, fontWeight = FontWeight.Bold)
                    }
                }
                "inquiries" -> {
                    IconButton(onClick = { Toast.makeText(context, "Sorting Inquiries...", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Filled.Sort, contentDescription = "Sort")
                    }
                }
                "my_listings" -> {
                    IconButton(onClick = { navController.navigate("add_product") }) {
                        Icon(Icons.Filled.Add, contentDescription = "New Listing")
                    }
                }
                else -> {
                    if (!isSeller) {
                        if (currentRoute != "cart" && currentRoute != "checkout") {
                            Box {
                                IconButton(onClick = { navController.navigate("cart") }) {
                                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart", tint = OnSurface)
                                }
                                if (cartCount > 0) {
                                    Badge(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .offset(x = (-6).dp, y = 6.dp),
                                        containerColor = Primary,
                                        contentColor = OnPrimary
                                    ) {
                                        Text(cartCount.toString())
                                    }
                                }
                            }
                        }
                    } else if (currentRoute == "seller_home" || currentRoute == "sell") {
                        IconButton(onClick = { navController.navigate("notifications") }) {
                            Icon(Icons.Filled.Notifications, contentDescription = "Notifications", tint = OnSurface)
                        }
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandCream, scrolledContainerColor = BrandCream)
    )
}

@Composable
fun HalliBottomNav(navController: NavController, currentRoute: String?, cartCount: Int, isSeller: Boolean) {
    data class NavItem(val route: String, val label: String, val icon: ImageVector, val selectedIcon: ImageVector)

    val items = if (isSeller) {
        listOf(
            NavItem("seller_home", "Dashboard", Icons.Filled.Dashboard, Icons.Filled.Dashboard),
            NavItem("add_product", "Add Product", Icons.Filled.Add, Icons.Filled.Add),
            NavItem("profile", "Profile", Icons.Filled.Person, Icons.Filled.Person),
        )
    } else {
        listOf(
            NavItem("home", "Home", Icons.Filled.Home, Icons.Filled.Home),
            NavItem("search", "Browse", Icons.Filled.Explore, Icons.Filled.Explore),
            NavItem("profile", "Account", Icons.Filled.Person, Icons.Filled.Person),
        )
    }

    NavigationBar(containerColor = BrandCream, contentColor = OnSurface, tonalElevation = 0.dp) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { 
                    BadgedBox(
                        badge = {
                            if (item.route == "cart" && cartCount > 0) {
                                Badge(containerColor = Primary, contentColor = OnPrimary) {
                                    Text("$cartCount")
                                }
                            }
                        }
                    ) {
                        Icon(imageVector = if (selected) item.selectedIcon else item.icon, contentDescription = item.label)
                    }
                },
                label = { Text(item.label, style = MaterialTheme.typography.labelSmall, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary, selectedTextColor = Primary, indicatorColor = SurfaceContainerHigh, unselectedIconColor = OnSurfaceVariant, unselectedTextColor = OnSurfaceVariant)
            )
        }
    }
}