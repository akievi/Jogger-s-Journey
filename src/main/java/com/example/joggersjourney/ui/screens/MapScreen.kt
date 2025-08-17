package com.example.joggersjourney.ui.screens

// Specific Maps Compose imports
import android.Manifest
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.joggersjourney.data.LocationService
import com.example.joggersjourney.navigation.Screen
import com.example.joggersjourney.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(navController: NavController) {
    val context = LocalContext.current
    val locationService = remember { LocationService(context) }
    val scope = rememberCoroutineScope()

    var currentUserGold by remember { mutableIntStateOf(150) }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LocationService.DEFAULT_LOCATION, 18f)
    }

    // Feste Herausforderer-Positionen basierend auf Benutzerstandort
    val challengerPositions = remember(userLocation) {
        val baseLat = userLocation?.latitude ?: LocationService.DEFAULT_LOCATION.latitude
        val baseLng = userLocation?.longitude ?: LocationService.DEFAULT_LOCATION.longitude
        
        mapOf(
            "easy_jogger1" to LatLng(baseLat + 0.002, baseLng + 0.003),
            "easy_collector" to LatLng(baseLat + 0.001, baseLng - 0.002),
            "easy_explorer" to LatLng(baseLat - 0.001, baseLng + 0.001),
            "medium_runner" to LatLng(baseLat + 0.003, baseLng - 0.003),
            "hard_marathon" to LatLng(baseLat - 0.002, baseLng - 0.001)
        )
    }

    // Permission handling
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // Debug: Permissions Status
    LaunchedEffect(Unit) {
        println("DEBUG: MapScreen gestartet")
        println("DEBUG: Permissions erteilt: ${locationPermissions.allPermissionsGranted}")
        println("DEBUG: Individual permissions:")
        locationPermissions.permissions.forEach { permission ->
            println("DEBUG: ${permission.permission} - ${permission.status}")
        }
    }

    // Get current location when permissions are granted
    LaunchedEffect(locationPermissions.allPermissionsGranted) {
        println("DEBUG: LaunchedEffect ausgelöst, Permissions: ${locationPermissions.allPermissionsGranted}")
        if (locationPermissions.allPermissionsGranted) {
            locationService.getCurrentLocation()?.let { location ->
                println("DEBUG: Location erhalten: $location")
                userLocation = location
                cameraPositionState.position = CameraPosition.fromLatLngZoom(location, 18f)
            } ?: println("DEBUG: Keine Location erhalten")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E)) // Dunkler Gaming-Hintergrund
    ) {
        // Gaming-Style Top Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFF0F0F23) // Sehr dunkler Header
                )
                .padding(16.dp)
        ) {
            // Retro Gaming Layout - Titel oben, Stats unten
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Gaming Title mit Pixel-Style
                Text(
                    text = "| JOGGER'S JOURNEY |",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF00AAFF), // Neon Dunkelblau statt Grün
                    modifier = Modifier
                        .background(
                            Color(0xFF001133), // Dunkles Blau statt Grün
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Gaming Stats HUD unter dem Titel
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Health Bar Style für Level
                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFF330033),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "LVL",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF00FF) // Magenta
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "7",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                    
                    // Gold/Coins im Gaming-Style
                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFF4A4A00),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "💰",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentUserGold.toString(),
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFFFD700) // Gold
                            )
                        }
                    }
                    
                    // HP/Energy Bar
                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFF4A0000),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "❤️",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "85/100",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFFF4444) // Rot
                            )
                        }
                    }
                }
            }
        }
        
        // XP/Progress Bar (Gaming-Style)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color(0xFF2A2A2A))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f) // 70% XP
                    .fillMaxHeight()
                    .background(Color(0xFF00FFFF)) // Cyan XP Bar
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Map area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // Debug UI state
            println("DEBUG: UI Entscheidung - Permissions erteilt: ${locationPermissions.allPermissionsGranted}")
            
            if (locationPermissions.allPermissionsGranted) {
                println("DEBUG: Zeige Google Map")
                // Real Google Map mit Gaming-Style
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = false, // Deaktiviert Standard-Standort-Punkt
                        mapType = MapType.NORMAL, // Normale Straßenkarte
                        // Einfacher Dark Mode über JSON Style
                        mapStyleOptions = com.google.android.gms.maps.model.MapStyleOptions(
                            """
                            [
                              {
                                "elementType": "geometry",
                                "stylers": [
                                  {
                                    "color": "#1d2c4d"
                                  }
                                ]
                              },
                              {
                                "elementType": "labels.text.fill",
                                "stylers": [
                                  {
                                    "color": "#8ec3b9"
                                  }
                                ]
                              },
                              {
                                "elementType": "labels.text.stroke",
                                "stylers": [
                                  {
                                    "color": "#1a3646"
                                  }
                                ]
                              },
                              {
                                "featureType": "road",
                                "elementType": "geometry",
                                "stylers": [
                                  {
                                    "color": "#38414e"
                                  }
                                ]
                              },
                              {
                                "featureType": "road",
                                "elementType": "geometry.stroke",
                                "stylers": [
                                  {
                                    "color": "#212a37"
                                  }
                                ]
                              },
                              {
                                "featureType": "road",
                                "elementType": "labels.text.fill",
                                "stylers": [
                                  {
                                    "color": "#9ca5b3"
                                  }
                                ]
                              },
                              {
                                "featureType": "water",
                                "elementType": "geometry",
                                "stylers": [
                                  {
                                    "color": "#0e1626"
                                  }
                                ]
                              }
                            ]
                            """.trimIndent()
                        )
                    ),
                    uiSettings = MapUiSettings(
                        myLocationButtonEnabled = false, // Wir verwenden unseren Gaming-Button
                        zoomControlsEnabled = false,
                        compassEnabled = false,
                        mapToolbarEnabled = false // Cleaner für Gaming-Look
                    ),
                    onMapLoaded = {
                        // Debug: Map wurde erfolgreich geladen
                        println("DEBUG: Gaming-Style Google Map geladen!")
                    }
                ) {
                    // Custom Spieler-Marker anstatt Standard-Standort-Punkt
                    val playerPosition = userLocation ?: LocationService.DEFAULT_LOCATION
                    Marker(
                        state = MarkerState(position = playerPosition),
                        title = "👤 DEINE POSITION",
                        snippet = if (userLocation != null) "🎮 SPIELER • GPS aktiv!" else "🎮 SPIELER • Standardposition",
                        icon = vectorToBitmap(LocalContext.current, R.drawable.avatar_player, 2.0f), // Normale Größe
                        anchor = Offset(0.5f, 0.5f) // Zentriert den Marker
                    )
                    
                    // Gaming-Style Quest Markers - Feste Herausforderer-Positionen mit PNG-Avataren
                    // 3 Leichte Herausforderer - Alle verwenden avatar_easy_npc.PNG
                    GamingQuestMarker(
                        position = challengerPositions["easy_jogger1"]!!,
                        title = "ANFÄNGER JOGGER",
                        difficulty = "EASY",
                        reward = "30 XP + 25 Gold",
                        iconResId = R.drawable.avatar_easy_npc, // Einheitlicher Easy-NPC Avatar
                        onClick = { navController.navigate(Screen.QuestDetail.createRoute("easy_jogger1")) }
                    )
                    
                    GamingQuestMarker(
                        position = challengerPositions["easy_collector"]!!,
                        title = "NATUR-GENIEßER",
                        difficulty = "EASY", 
                        reward = "35 XP + 20 Gold",
                        iconResId = R.drawable.avatar_easy_npc, // Einheitlicher Easy-NPC Avatar
                        onClick = { navController.navigate(Screen.QuestDetail.createRoute("easy_collector")) }
                    )
                    
                    GamingQuestMarker(
                        position = challengerPositions["easy_explorer"]!!,
                        title = "PARK-ERKUNDER",
                        difficulty = "EASY",
                        reward = "40 XP + 30 Gold",
                        iconResId = R.drawable.avatar_easy_npc, // Einheitlicher Easy-NPC Avatar
                        onClick = { navController.navigate(Screen.QuestDetail.createRoute("easy_explorer")) }
                    )
                    
                    // 1 Mittlerer Herausforderer - Verwendet avatar_medium_npc.PNG
                    GamingQuestMarker(
                        position = challengerPositions["medium_runner"]!!,
                        title = "AUSDAUER-LÄUFER", 
                        difficulty = "MEDIUM",
                        reward = "100 XP + 75 Gold",
                        iconResId = R.drawable.avatar_medium_npc, // Medium-NPC Avatar
                        onClick = { navController.navigate(Screen.QuestDetail.createRoute("medium_runner")) }
                    )
                    
                    // 1 Schwerer Herausforderer - Verwendet avatar_hard_npc.PNG
                    GamingQuestMarker(
                        position = challengerPositions["hard_marathon"]!!,
                        title = "MARATHON-MEISTER",
                        difficulty = "HARD", 
                        reward = "250 XP + 150 Gold",
                        iconResId = R.drawable.avatar_hard_npc, // Hard-NPC Avatar
                        onClick = { navController.navigate(Screen.QuestDetail.createRoute("hard_marathon")) }
                    )
                }
            } else {
                println("DEBUG: Zeige Permission Request UI")
                // Permission request UI
                PermissionRequestUI(
                    onRequestPermissions = { 
                        println("DEBUG: Permission Request Button geklickt")
                        locationPermissions.launchMultiplePermissionRequest() 
                    }
                )
            }
            
            // Gaming-Style Location Button
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(80.dp) // Button größer machen
                    .background(
                        Color(0xFF4A0080), // Lila Gaming-Farbe
                        RoundedCornerShape(4.dp) // Verpixelter Look
                    )
                    .padding(2.dp) // Für Border-Effekt
                    .background(
                        Color(0xFF6600CC),
                        RoundedCornerShape(2.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { 
                        scope.launch {
                            if (locationPermissions.allPermissionsGranted) {
                                locationService.getCurrentLocation()?.let { location ->
                                    userLocation = location
                                    cameraPositionState.position = CameraPosition.fromLatLngZoom(location, 18f)
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "GPS Location",
                        modifier = Modifier.size(40.dp), // Icon explizit größer
                        tint = Color(0xFF00FFFF) // Cyan Gaming-Farbe
                    )
                }
            }
        }
    }
}

@Composable
fun GamingQuestMarker(
    position: LatLng,
    title: String,
    difficulty: String,
    reward: String,
    iconResId: Int,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val difficultyIndicator = when(difficulty) {
        "EASY" -> "★" // Stern für einfach
        "MEDIUM" -> "★★" // Zwei Sterne für mittel  
        "HARD" -> "★★★" // Drei Sterne für schwer
        else -> "○"
    }
    
    Marker(
        state = MarkerState(position = position),
        title = title,
        snippet = "$difficultyIndicator $difficulty • $reward",
        icon = vectorToBitmap(context, iconResId, 2.0f), // Normale Größe - doppelt so groß wie vorher
        onClick = { 
            onClick()
            true
        }
    )
}

@Composable
fun PermissionRequestUI(
    onRequestPermissions: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E)), // Gaming-Hintergrund
        contentAlignment = Alignment.Center
    ) {
        // Gaming-Style Dialog Box
        Column(
            modifier = Modifier
                .padding(32.dp)
                .background(
                    Color(0xFF0F0F23), // Dunkler Dialog-Hintergrund
                    RoundedCornerShape(8.dp)
                )
                .padding(4.dp) // Border-Effekt
                .background(
                    Color(0xFF2A2A4A),
                    RoundedCornerShape(4.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Gaming-Style Header
            Text(
                text = "⚠️ QUEST BLOCKIERT ⚠️",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFF4444) // Rot für Warnung
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Gaming Location Icon
            Text(
                text = "🗺️",
                style = MaterialTheme.typography.displayMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "GPS-BERECHTIGUNG ERFORDERLICH",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00FFFF) // Cyan
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Aktiviere GPS um Quests in deiner Umgebung zu finden und dein Abenteuer zu starten!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFCCCCCC) // Hellgrau
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Gaming-Style Button
            Box(
                modifier = Modifier
                    .background(
                        Color(0xFF006600), // Dunkelgrün
                        RoundedCornerShape(8.dp)
                    )
                    .padding(2.dp)
                    .background(
                        Color(0xFF00AA00), // Hellgrün
                        RoundedCornerShape(6.dp)
                    )
            ) {
                Button(
                    onClick = onRequestPermissions,
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🚀",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "BERECHTIGUNG AKTIVIEREN",
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// Hilfsfunktion um Vector Drawables in Bitmaps zu konvertieren mit fester Größe
fun vectorToBitmap(context: android.content.Context, vectorResId: Int, scaleFactor: Float = 1.0f): BitmapDescriptor {
    val drawable = ContextCompat.getDrawable(context, vectorResId)
    
    // Feste Grundgröße für statische Marker (unabhängig vom Zoom)
    val baseSize = 120 // Basis-Pixelgröße
    val finalSize = (baseSize * scaleFactor).toInt()
    
    val bitmap = Bitmap.createBitmap(
        finalSize,
        finalSize,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable!!.setBounds(0, 0, finalSize, finalSize)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
} 