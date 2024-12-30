package com.ainsln.feature.notes.adaptive

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.device.DeviceInteraction.Companion.setScreenOrientation
import androidx.test.espresso.device.EspressoDevice.Companion.onDevice
import androidx.test.espresso.device.action.ScreenOrientation
import com.ainsln.core.testing.onNodeWithDescriptionStringId
import com.ainsln.core.testing.onNodeWithStringId
import com.ainsln.core.testing.waitAndClickByDescriptionId
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.feature.notes.ActivityRecreationTester
import com.ainsln.feature.notes.R
import com.ainsln.uitesthilt.HiltComponentActivity
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule

abstract class AbstractOrientationChangeTests {

    @get:Rule(order = 0)
    abstract val hiltRule: HiltAndroidRule
    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<HiltComponentActivity>()

    private val recreationTester = ActivityRecreationTester<HiltComponentActivity>()
    val sampleQuery = "si"

    @Before
    fun setup() {
        hiltRule.inject()
        recreationTester.setContent(composeRule) {
            CBTJournalTheme {
                NotesListDetailScreen(
                    distortionsSelectionDialog = {}
                )
            }
        }
    }

    fun goToDetails(tag: String = "note:1"){
        composeRule.waitUntil {
            composeRule.onNodeWithTag(tag).isDisplayed()
        }
        composeRule.onNodeWithTag(tag).performClick()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
    }

    fun goToSearchAndInputQuery(tag: String = "note:1"){
        composeRule.waitAndClickByDescriptionId(R.string.search)
        composeRule.onNodeWithStringId(R.string.search_placeholder).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.search_placeholder).performTextInput(sampleQuery)
        composeRule.waitUntil {
            composeRule.onNodeWithTag(tag).isDisplayed()
        }
    }

    fun multiSelect(){
        composeRule.waitUntil {
            composeRule.onNodeWithTag("note:1").isDisplayed()
                    && composeRule.onNodeWithTag("note:2").isDisplayed()
        }
        composeRule.onNodeWithTag("note:1").performTouchInput { longClick() }
        composeRule.onNodeWithTag("note:2").performClick()
        composeRule.onNodeWithDescriptionStringId(com.ainsln.core.ui.R.string.delete_selected).assertIsDisplayed()
    }

    fun changeOrientationToLandscape(){
        recreationTester.recreateWith(composeRule) {
            onDevice().setScreenOrientation(ScreenOrientation.LANDSCAPE)
        }
    }

    fun changeOrientationToPortrait(){
        recreationTester.recreateWith(composeRule) {
            onDevice().setScreenOrientation(ScreenOrientation.PORTRAIT)
        }
    }

}
