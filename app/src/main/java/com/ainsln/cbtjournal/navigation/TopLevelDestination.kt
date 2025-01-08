package com.ainsln.cbtjournal.navigation

import com.ainsln.cbtjournal.R
import com.ainsln.feature.distortions.navigation.DistortionsDestinations
import com.ainsln.feature.info.navigation.InfoDestinations
import com.ainsln.feature.notes.navigation.NotesDestinations
import kotlin.reflect.KClass


enum class TopLevelDestination(
    val titleResId: Int,
    val iconResId: Int,
    val route: KClass<*>,
    val tag: String
) {
    JOURNAL(
        titleResId = R.string.journal,
        iconResId = R.drawable.journal_ic,
        route = NotesDestinations.List::class,
        tag = "JournalNavigationItem"
    ),

    DISTORTIONS(
        titleResId = R.string.distortions,
        iconResId = R.drawable.distortions_ic,
        route = DistortionsDestinations.List::class,
        tag = "DistortionsNavigationItem"
    ),
    HELP(
        titleResId = R.string.help,
        iconResId = R.drawable.help_ic,
        route = InfoDestinations.Main::class,
        tag = "HelpNavigationItem"
    )
}
