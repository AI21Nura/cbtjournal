package com.ainsln.cbtjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ainsln.cbtjournal.ui.CBTJournalApp
import com.ainsln.core.ui.theme.CBTJournalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CBTJournalTheme {
                CBTJournalApp()
            }
        }
    }
}

