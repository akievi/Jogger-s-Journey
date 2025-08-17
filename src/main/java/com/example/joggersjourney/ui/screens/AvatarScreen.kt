package com.example.joggersjourney.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.joggersjourney.data.models.AccessoryType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarScreen(navController: NavController) {
    var selectedAccessoryType: AccessoryType by remember { mutableStateOf(AccessoryType.HAT) }
    var selectedAccessories: Map<AccessoryType, String> by remember {
        mutableStateOf(mapOf<AccessoryType, String>()) 
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top bar
        TopAppBar(
            title = { Text("Avatar anpassen", fontWeight = FontWeight.Bold) }
        )
        
        // Avatar display area
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Dein Avatar",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Avatar representation
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2196F3)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = Color.White,
                        modifier = Modifier.size(80.dp)
                    )
                    
                    // Show equipped accessories as small icons around avatar
                    selectedAccessories[AccessoryType.HAT]?.let {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .offset(y = (-40).dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFD700)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "Hat",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Tippe auf ein Accessoire, um es anzuziehen",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
        
        // Accessory type selector
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(AccessoryType.entries.toTypedArray()) { type ->
                AccessoryTypeButton(
                    type = type,
                    isSelected = selectedAccessoryType == type,
                    onClick = { selectedAccessoryType = type }
                )
            }
        }
        
        // Available accessories for selected type
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = getAccessoryTypeName(selectedAccessoryType),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Accessory grid
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(getSampleAccessories(selectedAccessoryType)) { accessory ->
                        AccessoryItem(
                            icon = accessory.icon,
                            name = accessory.name,
                            isEquipped = selectedAccessories[selectedAccessoryType] == accessory.id,
                            isOwned = accessory.isOwned,
                            onClick = {
                                if (accessory.isOwned) {
                                    selectedAccessories = selectedAccessories.toMutableMap().apply {
                                        if (get(selectedAccessoryType) == accessory.id) {
                                            remove(selectedAccessoryType) // Unequip
                                        } else {
                                            put(selectedAccessoryType, accessory.id) // Equip
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AccessoryTypeButton(
    type: AccessoryType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary 
                           else Color.LightGray
        ),
        modifier = Modifier.height(48.dp)
    ) {
        Icon(
            getAccessoryTypeIcon(type),
            contentDescription = getAccessoryTypeName(type),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(getAccessoryTypeName(type))
    }
}

@Composable
fun AccessoryItem(
    icon: ImageVector,
    name: String,
    isEquipped: Boolean,
    isOwned: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .clickable(enabled = isOwned) { onClick() }
            .then(
                if (isEquipped) Modifier.border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(8.dp)
                ) else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isOwned) Color.White else Color.Gray.copy(alpha = 0.3f)
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(
                icon,
                contentDescription = name,
                modifier = Modifier.size(32.dp),
                tint = if (isOwned) Color.Black else Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = name,
                fontSize = 12.sp,
                color = if (isOwned) Color.Black else Color.Gray
            )
            if (isEquipped) {
                Text(
                    text = "Ausgerüstet",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class SampleAccessory(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val isOwned: Boolean
)

fun getSampleAccessories(type: AccessoryType): List<SampleAccessory> {
    return when (type) {
        AccessoryType.HAT -> listOf(
            SampleAccessory("hat1", "Baseballcap", Icons.Default.AccountCircle, true),
            SampleAccessory("hat2", "Beanie", Icons.Default.AccountCircle, true),
            SampleAccessory("hat3", "Cowboyhut", Icons.Default.AccountCircle, false)
        )
        AccessoryType.SHIRT -> listOf(
            SampleAccessory("shirt1", "T-Shirt", Icons.Default.Person, true),
            SampleAccessory("shirt2", "Hoodie", Icons.Default.Person, false),
            SampleAccessory("shirt3", "Tank Top", Icons.Default.Person, true)
        )
        AccessoryType.PANTS -> listOf(
            SampleAccessory("pants1", "Jeans", Icons.Default.Person, true),
            SampleAccessory("pants2", "Shorts", Icons.Default.Person, true),
            SampleAccessory("pants3", "Jogginghose", Icons.Default.Person, false)
        )
        AccessoryType.SHOES -> listOf(
            SampleAccessory("shoes1", "Sneaker", Icons.Default.Star, true),
            SampleAccessory("shoes2", "Laufschuhe", Icons.Default.Star, false),
            SampleAccessory("shoes3", "Boots", Icons.Default.Star, false)
        )
        AccessoryType.GLASSES -> listOf(
            SampleAccessory("glasses1", "Sonnenbrille", Icons.Default.Star, true),
            SampleAccessory("glasses2", "Brille", Icons.Default.Star, false)
        )
        AccessoryType.BACKPACK -> listOf(
            SampleAccessory("backpack1", "Rucksack", Icons.Default.ShoppingCart, true),
            SampleAccessory("backpack2", "Sporttasche", Icons.Default.ShoppingCart, false)
        )
    }
}

fun getAccessoryTypeName(type: AccessoryType): String {
    return when (type) {
        AccessoryType.HAT -> "Hüte"
        AccessoryType.SHIRT -> "Oberteile"
        AccessoryType.PANTS -> "Hosen"
        AccessoryType.SHOES -> "Schuhe"
        AccessoryType.GLASSES -> "Brillen"
        AccessoryType.BACKPACK -> "Rucksäcke"
    }
}

fun getAccessoryTypeIcon(type: AccessoryType): ImageVector {
    return when (type) {
        AccessoryType.HAT -> Icons.Default.AccountCircle
        AccessoryType.SHIRT -> Icons.Default.Person
        AccessoryType.PANTS -> Icons.Default.Person
        AccessoryType.SHOES -> Icons.Default.Star
        AccessoryType.GLASSES -> Icons.Default.Star
        AccessoryType.BACKPACK -> Icons.Default.ShoppingCart
    }
} 