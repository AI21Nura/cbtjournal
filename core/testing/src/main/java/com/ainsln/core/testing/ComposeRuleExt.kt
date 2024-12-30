package com.ainsln.core.testing

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule

fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.onNodeWithStringId(
    @StringRes stringRes: Int
): SemanticsNodeInteraction = onNodeWithText(activity.getString(stringRes))


fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.onNodeWithDescriptionStringId(
    @StringRes stringRes: Int
): SemanticsNodeInteraction = onNodeWithContentDescription(activity.getString(stringRes))

fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.onNodeWithTagStringId(
    @StringRes stringRes: Int
): SemanticsNodeInteraction = onNodeWithTag(activity.getString(stringRes))

fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.goBack(){
    activityRule.scenario.onActivity { activity ->
        activity.onBackPressedDispatcher.onBackPressed()
    }
}


fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.waitAndClickByDescriptionId(
    @StringRes stringRes: Int
){
    waitUntil {
        onNodeWithDescriptionStringId(stringRes).isDisplayed()
    }
    onNodeWithDescriptionStringId(stringRes).performClick()
}

fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.waitAndClickByTag(
    tag: String
){
    waitUntil {
        onNodeWithTag(tag).isDisplayed()
    }
    onNodeWithTag(tag).performClick()
}
