package com.ainsln.cbtjournal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ainsln.feature.distortions.navigation.DistortionsDestinations
import com.ainsln.feature.distortions.navigation.distortionsDestination

@Composable
fun AppNavHost(){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = DistortionsDestinations.List){

        distortionsDestination()
        
    }


}
