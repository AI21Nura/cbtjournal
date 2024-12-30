package com.ainsln.feature.notes.adaptive

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.device.action.ScreenOrientation
import androidx.test.espresso.device.rules.ScreenOrientationRule
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
class LandscapeOrientationChangeTests : AbstractOrientationChangeTests() {

    override val hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 2)
    val screenOrientationRule: ScreenOrientationRule = ScreenOrientationRule(ScreenOrientation.LANDSCAPE)

    @Test
    fun expandedToCompact_editNoteWithDoubleOrientationChange() {
        //open details
        goToDetails()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        //open editor
        composeRule.waitAndClickByDescriptionId(R.string.edit_note)
        composeRule.onNodeWithStringId(R.string.note_edit_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        //change orientation
        changeOrientationToPortrait()
        composeRule.onNodeWithStringId(R.string.note_edit_title).assertIsDisplayed()
        //change orientation
        changeOrientationToLandscape()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.note_edit_title).assertIsDisplayed()
    }

    @Test
    fun expandedToCompact_addNewNoteWithCancelDialog(){
        //go to add
        composeRule.waitAndClickByDescriptionId(R.string.add_note)
        composeRule.onNodeWithStringId(R.string.note_add_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        //change orientation
        changeOrientationToPortrait()
        composeRule.onNodeWithStringId(R.string.note_add_title).assertIsDisplayed()
        //cancel
        composeRule.onNodeWithDescriptionStringId(com.ainsln.core.ui.R.string.back).performClick()
        //change orientation
        changeOrientationToLandscape()
        //confirm cancellation
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsDisplayed()
        //change orientation
        changeOrientationToPortrait()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
    }

    @Test
    fun compactToExpanded_searchAndMultiDelete(){
        goToSearchAndInputQuery()
        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsDisplayed()
        //open details
        goToDetails()
        composeRule.onNodeWithText(sampleQuery).assertIsDisplayed()
        //go back from search to list
        composeRule.goBack()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
        //multiselect
        multiSelect()
        //delete note (details pane)
        composeRule.waitAndClickByDescriptionId(R.string.delete_note)
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        composeRule.onNodeWithDescriptionStringId(com.ainsln.core.ui.R.string.delete_selected).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsDisplayed()
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
    fun expandedToCompact_deleteNoteAndSelectAnother(){
        //open details & delete
        goToDetails()
        composeRule.waitAndClickByDescriptionId(R.string.delete_note)
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsDisplayed()
        //change orientation
        changeOrientationToPortrait()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        //change orientation
        changeOrientationToLandscape()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsDisplayed()
        //open another note
        goToDetails("note:2")
        //change orientation
        changeOrientationToPortrait()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
    }

}
