package org.khedr.project.presention.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.khedr.project.presention.model.TestStatus
import org.khedr.project.presention.viewmodel.TestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceTestScreen(
    navController: NavHostController,
    viewModel: TestViewModel, // نجعله باراميتر إجباري لضمان استخدام النسخة المشتركة
    modifier: Modifier = Modifier
) {
    val tests = viewModel.tests
    val allTestsCompleted = tests.none { it.status == TestStatus.PENDING }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Device Check",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = { /* Handle Report Submission */ },
                    enabled = allTestsCompleted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A90E2),
                        contentColor = Color.White,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Send Report",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },
        containerColor = Color.White
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
            ) {
                items(tests) { test ->
                    TestCard(
                        test = test,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth(),
                        onClick = {
                            navController.navigate(viewModel.getRouteFromTest(test.id))
                        }
                    )
                }
            }
        }
    }
}
