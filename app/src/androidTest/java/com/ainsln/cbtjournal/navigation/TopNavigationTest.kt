package com.ainsln.cbtjournal.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.ainsln.core.testing.TestCompactWidth
import com.ainsln.core.testing.TestExpandedWidth
import com.ainsln.core.testing.goBack
import com.ainsln.core.testing.onNodeWithDescriptionStringId
import com.ainsln.core.testing.onNodeWithStringId
import com.ainsln.core.testing.onNodeWithTagStringId
import com.ainsln.core.testing.waitAndClickByTag
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.uitesthilt.HiltComponentActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class TopNavigationTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<HiltComponentActivity>()

    private val journalTag = TopLevelDestination.JOURNAL.tag
    private val distortionsTag = TopLevelDestination.DISTORTIONS.tag
    private val helpTag = TopLevelDestination.HELP.tag

    @Before
    fun setup(){
        composeRule.setContent {
            CBTJournalTheme {
                AppNavHost()
            }
        }
    }

    @Test
    @TestCompactWidth
    fun testStartScreenIsDisplayed() {
        composeRule.waitUntil {
            composeRule.onNodeWithTag(journalTag).isDisplayed()
        }
        composeRule.onNodeWithTag(distortionsTag).assertIsDisplayed()
        composeRule.onNodeWithTag(helpTag).assertIsDisplayed()
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.notes_list_title).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun testNavigateFromJournalToDistortions(){
        composeRule.waitAndClickByTag(distortionsTag)
        composeRule.onNodeWithStringId(com.ainsln.feature.distortions.R.string.distortions_list_title).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun testNavigateFromJournalToDistortionsAndBack(){
        testNavigateFromJournalToDistortions()
        composeRule.goBack()
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.notes_list_title).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun testNavigateFromDistortionsToHelp(){
        testNavigateFromJournalToDistortions()
        composeRule.onNodeWithTag(helpTag).performClick()
        composeRule.onNodeWithStringId(com.ainsln.feature.info.R.string.main_title).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun testNavigateFromHelpToJournal(){
        composeRule.waitAndClickByTag(helpTag)
        composeRule.onNodeWithStringId(com.ainsln.feature.info.R.string.main_title).assertIsDisplayed()
        composeRule.goBack()
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.notes_list_title).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun testDeepNavigationThroughAllSections(){
        openNoteDetails()
        //go to distortions section & open details
        testNavigateFromJournalToDistortions()
        openDistortionDetails()
        //go to help section
        openHelpSection()
        //open guide
        composeRule.onNodeWithStringId(com.ainsln.feature.info.R.string.open_guide).performClick()
        composeRule.onNodeWithStringId(com.ainsln.feature.info.R.string.guide_title).assertIsDisplayed()
        //go to journal section (note details)
        composeRule.onNodeWithTag(journalTag).performClick()
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.notes_detail_title).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun testBackwardAndForwardNavigationBetweenSections(){
        //go to help section & open guide
        composeRule.waitAndClickByTag(helpTag)
        composeRule.onNodeWithStringId(com.ainsln.feature.info.R.string.open_guide).performClick()
        composeRule.onNodeWithStringId(com.ainsln.feature.info.R.string.guide_title).assertIsDisplayed()
        //go to journal section & open note details
        composeRule.onNodeWithTag(journalTag).performClick()
        openNoteDetails()
        //go back to journal
        composeRule.goBack()
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.notes_list_title).assertIsDisplayed()
        //go to distortions section & open details
        testNavigateFromJournalToDistortions()
        openDistortionDetails()
        //go to help section (guide)
        composeRule.waitAndClickByTag(helpTag)
        composeRule.onNodeWithStringId(com.ainsln.feature.info.R.string.guide_title).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun testScreenStateIsMaintainedOnReturn(){
        openNoteDetails()
        //go to distortions section
        testNavigateFromJournalToDistortions()
        //back to journal (details)
        composeRule.goBack()
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.notes_detail_title).assertIsDisplayed()
        //back to journal (list)
        composeRule.goBack()
        //go to distortions section
        testNavigateFromJournalToDistortions()
        //back to journal (list)
        composeRule.goBack()
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.notes_list_title).assertIsDisplayed()
        openNoteDetails()
    }

    @Test
    @TestExpandedWidth
    fun expanded_testStartScreenIsDisplayed(){
        composeRule.waitUntil {
            composeRule.onNodeWithTag(journalTag).isDisplayed()
        }
        composeRule.onNodeWithTag(distortionsTag).assertIsDisplayed()
        composeRule.onNodeWithTag(helpTag).assertIsDisplayed()
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.notes_details_placeholder).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_testNavigateFromJournalToDistortions(){
        composeRule.waitAndClickByTag(distortionsTag)
        composeRule.onNodeWithStringId(com.ainsln.feature.distortions.R.string.distortions_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(com.ainsln.feature.distortions.R.string.distortions_placeholder).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_testNavigateFromJournalToDistortionsAndBack(){
        expanded_testNavigateFromJournalToDistortions()
        composeRule.goBack()
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.notes_details_placeholder).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_testNavigateFromDistortionsToHelp(){
        expanded_testNavigateFromJournalToDistortions()
        openHelpSection(expanded = true)
    }

    @Test
    @TestExpandedWidth
    fun expanded_testNavigationBetweenSections(){
        openNoteDetails(expanded = true)
        //go to distortions section & open details
        expanded_testNavigateFromJournalToDistortions()
        openDistortionDetails()
        //go to help section
        openHelpSection(expanded = true)
    }

    @Test
    @TestExpandedWidth
    fun expanded_testSystemBackNavigation(){
        //go to distortions section & open details
        expanded_testNavigateFromJournalToDistortions()
        openDistortionDetails(expanded = true)
        //go back to journal
        composeRule.goBack()
        //go to help section
        openHelpSection()
        //go back to journal
        composeRule.goBack()
    }

    @Test
    @TestExpandedWidth
    fun expanded_testNavigationFromNestedScreens(){
        openNoteDetails(expanded = true)
        //go to distortions section & open details
        expanded_testNavigateFromJournalToDistortions()
        openDistortionDetails(expanded = true)
        //go to help section
        openHelpSection(expanded = true)
        //back to journal (details)
        composeRule.goBack()
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.notes_detail_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.notes_list_title).assertIsDisplayed()
        //open add
        composeRule.onNodeWithDescriptionStringId(com.ainsln.feature.notes.R.string.add_note).performClick()
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.note_add_title).assertIsDisplayed()
        //go to distortions (details)
        composeRule.onNodeWithTag(distortionsTag).performClick()
        composeRule.onNodeWithTagStringId(com.ainsln.feature.distortions.R.string.distortion_details_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(com.ainsln.feature.distortions.R.string.distortions_list_title).assertIsDisplayed()
        //back to list
        composeRule.goBack()
        //go to journal (add)
        composeRule.onNodeWithTag(journalTag).performClick()
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.note_add_title).assertIsDisplayed()
    }

    private fun openNoteDetails(expanded: Boolean = false){
        composeRule.waitAndClickByTag("note:1")
        composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.notes_detail_title).assertIsDisplayed()
        if (expanded)
            composeRule.onNodeWithStringId(com.ainsln.feature.notes.R.string.notes_list_title).assertIsDisplayed()
    }

    private fun openDistortionDetails(expanded: Boolean = false){
        composeRule.waitAndClickByTag("distortion:1")
        composeRule.onNodeWithTagStringId(com.ainsln.feature.distortions.R.string.distortion_details_title).assertIsDisplayed()
        if (expanded)
            composeRule.onNodeWithStringId(com.ainsln.feature.distortions.R.string.distortions_list_title).assertIsDisplayed()
    }

    private fun openHelpSection(expanded: Boolean = false){
        composeRule.onNodeWithTag(helpTag).performClick()
        composeRule.onNodeWithStringId(com.ainsln.feature.info.R.string.main_title).assertIsDisplayed()
        if (expanded)
            composeRule.onNodeWithStringId(com.ainsln.feature.info.R.string.guide_title).assertIsDisplayed()
    }

}
