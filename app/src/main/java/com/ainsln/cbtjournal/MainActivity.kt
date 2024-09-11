package com.ainsln.cbtjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.feature.distortions.DistortionsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CBTJournalTheme {
               Scaffold { innerPadding ->
                   DistortionsScreen(contentPadding = innerPadding)
               }
            }
        }
    }
}

