package com.example.pr35

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pr35.ui.theme.PR35Theme
import com.example.pr35.data.User
import com.example.pr35.data.AppDatabase
import com.example.pr35.ProfileInfoRow
import com.example.pr35.ProfileViewModel
import androidx.compose.runtime.getValue

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = viewModel(
    factory = ProfileViewModel.Factory(AppDatabase.getDatabase(navController.context).userDao())
)) {
    val user by viewModel.user.collectAsState()

    Scaffold(
        topBar = { ProfileTopBar(navController) },
        bottomBar = { BottomNavigationBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.height(16.dp))

            // User Name
            Text(
                text = "${user?.firstName ?: ""} ${user?.lastName ?: ""}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Barcode Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f)) // Заглушка для штрихкода
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {

            }

            Spacer(modifier = Modifier.height(24.dp))

            // User Details
            Column(modifier = Modifier.fillMaxWidth()) {
                ProfileInfoRow(label = "Имя", value = user?.firstName ?: "")
                Divider(modifier = Modifier.padding(vertical = 4.dp))
                ProfileInfoRow(label = "Фамилия", value = user?.lastName ?: "")
                Divider(modifier = Modifier.padding(vertical = 4.dp))
                ProfileInfoRow(label = "Адрес", value = user?.address ?: "")
                Divider(modifier = Modifier.padding(vertical = 4.dp))
                ProfileInfoRow(label = "Телефон", value = user?.phoneNumber ?: "")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Navigation Bar - already in Scaffold
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(text = "Профиль", fontWeight = FontWeight.Bold)
        },

        actions = {
            IconButton(onClick = { navController.navigate("edit_profile") }) {

            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavigationItem(icon = Icons.Default.Home, contentDescription = "Home") { /* TODO: Handle click */ }
        BottomNavigationItem(icon = Icons.Default.Favorite, contentDescription = "Favorites") { /* TODO: Handle click */ }
        // Central icon (shopping bag)
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
        BottomNavigationItem(icon = Icons.Default.AccountCircle, contentDescription = "Account") { /* TODO: Handle click */ }
        BottomNavigationItem(icon = Icons.Default.Person, contentDescription = "Profile") { /* TODO: Handle click */ }
    }
}

@Composable
fun BottomNavigationItem(icon: ImageVector, contentDescription: String, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    PR35Theme {
        ProfileScreen(rememberNavController())
    }
}