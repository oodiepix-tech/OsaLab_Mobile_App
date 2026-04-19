package org.khedr.project.presention.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import osacheck.composeapp.generated.resources.Res
import osacheck.composeapp.generated.resources.*

@Composable
fun FancyBottomBar(
    selectedIndex: Int,
    onItemClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomItem(
            title = stringResource(Res.string.test_mic),
            icon = Res.drawable.ic_find,
            selected = selectedIndex == 0,
            onClick = { onItemClick(0) }
        )

        BottomItem(
            title = stringResource(Res.string.test_mic),
            icon = Res.drawable.ic_test,
            selected = selectedIndex == 1,
            onClick = { onItemClick(1) }
        )
    }
}

@Composable
fun BottomItem(
    title: String,
    icon: DrawableResource,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }.padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    if (selected)
                        Brush.linearGradient(
                            listOf(Color(0xFF6EA8FF), Color(0xFF4A90E2))
                        )
                    else Brush.linearGradient(
                        listOf(Color.Transparent, Color.Transparent)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
           Icon(painter = painterResource(icon), contentDescription = null,
               tint = if (selected) Color.White else Color.Black,
               modifier = Modifier.size(32.dp).align(Alignment.Center),)
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = title,
            color = if (selected) Color(0xFF4A90E2) else Color.Gray,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
