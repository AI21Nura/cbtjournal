package com.ainsln.feature.notes.adaptive

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.device.action.ScreenOrientation
import androidx.test.espresso.device.rules.ScreenOrientationRule
import com.ainsln.core.testing.TestCompactWidth
import com.ainsln.core.testing.goBack
import com.ainsln.core.testing.onNodeWithDescriptionStringId
import com.ainsln.core.testing.onNodeWithStringId
import com.ainsln.core.testing.waitAndClickByDescriptionId
import com.ainsln.feature.notes.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class PortraitOrientationChangeTests: AbstractOrientationChangeTests() {

    override val hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 2)
    val screenOrientationRule: ScreenOrientationRule = ScreenOrientationRule(ScreenOrientation.PORTRAIT)

    @Test
    @TestCompactWidth
    fun compactToExpanded_switchToDetailsAndBackToList(){
        goToDetails()
        //change orientation
        changeOrientationToLandscape()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
        //change orientation
        changeOrientationToPortrait()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsNotDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
        //back
        composeRule.goBack()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsNotDisplayed()
    }

    @Test
    @TestCompactWidth
    fun compactToExpanded_navigateBackFromSearchAfterRotation(){
        goToSearchAndInputQuery()
        //change orientation
        changeOrientationToLandscape()
        composeRule.onNodeWithText(sampleQuery).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsDisplayed()
        //open details
        goToDetails()
        composeRule.onNodeWithText(sampleQuery).assertIsDisplayed()
        //change orientation
        changeOrientationToPortrait()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
        //go back to search
        composeRule.goBack()
        composeRule.onNodeWithText(sampleQuery).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun compactToExpanded_switchBetweenNotesWithPlaceholder(){
        //go to details
        goToDetails()
        //change orientation
        changeOrientationToLandscape()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
        //go to another note
        composeRule.onNodeWithTag("note:2").performClick()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
        //delete note
        composeRule.waitAndClickByDescriptionId(R.string.delete_note)
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsDisplayed()
        //change orientation
        changeOrientationToPortrait()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun compactToExpanded_searchEditAndCancel(){
        goToSearchAndInputQuery()
        //change orientation
        changeOrientationToLandscape()
        composeRule.onNodeWithText(sampleQuery).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsDisplayed()
        //open details
        goToDetails()
        composeRule.onNodeWithText(sampleQuery).assertIsDisplayed()
        //open editor
        composeRule.waitAndClickByDescriptionId(R.string.edit_note)
        composeRule.onNodeWithStringId(R.string.note_edit_title).assertIsDisplayed()
        //change orientation
        changeOrientationToPortrait()
        composeRule.onNodeWithStringId(R.string.note_edit_title).assertIsDisplayed()
        //cancel changes
        composeRule.onNodeWithDescriptionStringId(R.string.back).performClick()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun compactToExpanded_addNoteWithNavigationBack(){
        //go to add
        composeRule.waitAndClickByDescriptionId(R.string.add_note)
        composeRule.onNodeWithStringId(R.string.note_add_title).assertIsDisplayed()
        //change orientation
        changeOrientationToLandscape()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.note_add_title).assertIsDisplayed()
        //go back
        composeRule.goBack()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsDisplayed()
        //change orientation
        changeOrientationToPortrait()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun compactToExpanded_detailsAndMultiDelete(){
        multiSelect()
        //change orientation
        changeOrientationToLandscape()
        composeRule.onNodeWithDescriptionStringId(com.ainsln.core.ui.R.string.delete_selected).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsDisplayed()
        //cancel selection
        composeRule.onNodeWithDescriptionStringId(com.ainsln.core.ui.R.string.reset_selection).performClick()
        composeRule.onNodeWithDescriptionStringId(com.ainsln.core.ui.R.string.delete_selected).assertIsNotDisplayed()
        //open details & multiselect
        goToDetails()
        multiSelect()
        //delete selected
        composeRule.onNodeWithDescriptionStringId(com.ainsln.core.ui.R.string.delete_selected).performClick()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        composeRule.onNodeWithDescriptionStringId(com.ainsln.core.ui.R.string.delete_selected).assertIsNotDisplayed()
        composeRule.onNodeWithStringId(R.string.empty_journal_placeholder).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsDisplayed()
        //change orientation
        changeOrientationToPortrait()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.empty_journal_placeholder).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun compactToExpanded_cancelSearchNavigation(){
        goToSearchAndInputQuery()
        //go back
        composeRule.goBack()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        //change orientation
        changeOrientationToLandscape()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsDisplayed()
    }
}

