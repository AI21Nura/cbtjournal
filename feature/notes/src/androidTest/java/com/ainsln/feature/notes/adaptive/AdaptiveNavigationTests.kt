package com.ainsln.feature.notes.adaptive

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import com.ainsln.core.testing.TestCompactWidth
import com.ainsln.core.testing.TestExpandedWidth
import com.ainsln.core.testing.goBack
import com.ainsln.core.testing.onNodeWithDescriptionStringId
import com.ainsln.core.testing.onNodeWithStringId
import com.ainsln.core.testing.waitAndClickByDescriptionId
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.feature.notes.R
import com.ainsln.uitesthilt.HiltComponentActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test



@HiltAndroidTest
class AdaptiveNavigationTests {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<HiltComponentActivity>()

    private val sampleQuery = "si"

    @Before
    fun setup() {
        hiltRule.inject()
        composeRule.setContent {
            CBTJournalTheme {
                NotesListDetailScreen(
                    distortionsSelectionDialog = {}
                )
            }
        }
    }

    //#region Single Pane: From List
    @Test
    @TestCompactWidth
    fun singlePane_initialState_showsListPane(){
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsNotDisplayed()
    }

    @Test
    @TestCompactWidth
    fun singlePane_listToSearchPane_showsSearch(){
        //go to search
        composeRule.waitAndClickByDescriptionId(R.string.search)
        composeRule.onNodeWithStringId(R.string.search_placeholder).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun singlePane_listToSearchPane_showsSearchAndReturnsToList(){
        singlePane_listToSearchPane_showsSearch()
        composeRule.goBack()
        singlePane_initialState_showsListPane()
    }


    @Test
    @TestCompactWidth
    fun singlePane_listToAddPane_showsAdd(){
        composeRule.waitAndClickByDescriptionId(R.string.add_note)
        composeRule.onNodeWithStringId(R.string.note_add_title).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun singlePane_listToAddPane_warningDialog_staysOrReturnsToList(){
        singlePane_listToAddPane_showsAdd()
        //back
        composeRule.goBack()
        composeRule.onNodeWithStringId(R.string.discard_changes).assertIsDisplayed()
        //dismiss warning dialog
        composeRule.onNodeWithStringId(R.string.cancel_label).performClick()
        composeRule.onNodeWithStringId(R.string.discard_changes).assertIsNotDisplayed()
        //back & confirm cancellation
        composeRule.onNodeWithDescriptionStringId(R.string.back).performClick()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        singlePane_initialState_showsListPane()
    }

    @Test
    @TestCompactWidth
    fun singlePane_listToDetailsPane_showsDetails(){
        composeRule.waitUntil {
            composeRule.onNodeWithTag("note:1").isDisplayed()
        }
        composeRule.onNodeWithTag("note:1").performClick()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun singlePane_sequentialNavigationFromList_allPanesReturnToList(){
        //go to details & back
        singlePane_listToDetailsPane_showsDetails()
        composeRule.goBack()
        singlePane_initialState_showsListPane()
        //go to add & back
        singlePane_listToAddPane_warningDialog_staysOrReturnsToList()
        //go to search & back
        singlePane_listToSearchPane_showsSearchAndReturnsToList()
    }

    //#endregion

    //#region Single Pane: From Details
    @Test
    @TestCompactWidth
    fun singlePane_detailPane_editButtonOpensEditPane(){
        singlePane_listToDetailsPane_showsDetails()
        composeRule.waitAndClickByDescriptionId(R.string.edit_note)
        composeRule.onNodeWithStringId(R.string.note_edit_title).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun singlePane_detailPane_deleteButtonOpensWarningDialog(){
        singlePane_listToDetailsPane_showsDetails()
        composeRule.waitAndClickByDescriptionId(R.string.delete_note)
        composeRule.onNodeWithStringId(R.string.delete_warning).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun singlePane_detailPane_confirmDeleteNavigatesToListPane(){
        singlePane_detailPane_deleteButtonOpensWarningDialog()
        //cancel deletion
        composeRule.onNodeWithStringId(R.string.cancel_label).performClick()
        composeRule.onNodeWithStringId(R.string.delete_warning).isNotDisplayed()
        //confirm deletion & back to list
        composeRule.onNodeWithDescriptionStringId(R.string.delete_note).performClick()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        singlePane_initialState_showsListPane()
    }

    @Test
    @TestCompactWidth
    fun singlePane_detailPane_editPaneCancelNavigatesBackToDetailPane(){
        singlePane_detailPane_editButtonOpensEditPane()
        composeRule.onNodeWithDescriptionStringId(R.string.back).performClick()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun singlePane_detailPane_editPaneSaveNavigatesBackToDetailPane(){
        singlePane_detailPane_editButtonOpensEditPane()
        composeRule.onNodeWithDescriptionStringId(R.string.save_note).performClick()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun singlePane_detailPane_editSaveAndBackNavigatesToListPane(){
        singlePane_detailPane_editPaneSaveNavigatesBackToDetailPane()
        composeRule.onNodeWithDescriptionStringId(R.string.back).performClick()
        singlePane_initialState_showsListPane()
    }

    //#endregion

    //#region Single Pane: From Search
    @Test
    @TestCompactWidth
    fun singlePane_searchPane_displaysResultsWhenQueryEntered(){
        singlePane_listToSearchPane_showsSearch()
        composeRule.onNodeWithStringId(R.string.search_placeholder).performTextInput(sampleQuery)
        composeRule.waitUntil {
            composeRule.onNodeWithTag("note:1").isDisplayed()
        }
    }

    @Test
    @TestCompactWidth
    fun singlePane_searchPane_clickResultNavigatesToDetailPane(){
        singlePane_searchPane_displaysResultsWhenQueryEntered()
        composeRule.onNodeWithTag("note:1").performClick()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun singlePane_searchPane_detailBackNavigatesToSearchPane(){
        singlePane_searchPane_clickResultNavigatesToDetailPane()
        composeRule.goBack()
        composeRule.onNodeWithText(sampleQuery).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun singlePane_searchPane_editAndBackNavigatesToSearchPaneWithResults(){
        //go to details
        singlePane_searchPane_clickResultNavigatesToDetailPane()
        //go to editor
        composeRule.waitAndClickByDescriptionId(R.string.edit_note)
        composeRule.onNodeWithStringId(R.string.note_edit_title).assertIsDisplayed()
        //save & back to details
        composeRule.onNodeWithDescriptionStringId(R.string.save_note).performClick()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
        //back to search
        composeRule.goBack()
        composeRule.onNodeWithText(sampleQuery).assertIsDisplayed()
    }

    @Test
    @TestCompactWidth
    fun singlePane_searchPane_deleteNavigatesToSearchPaneWithoutDeletedNote(){
        //go to details
        singlePane_searchPane_clickResultNavigatesToDetailPane()
        //click delete & confirm
        composeRule.onNodeWithDescriptionStringId(R.string.delete_note).performClick()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        //back to search after remove
        composeRule.onNodeWithText(sampleQuery).assertIsDisplayed()
    }

    //#endregion

    //#region Two Panes: From List
    @Test
    @TestExpandedWidth
    fun expanded_initialState_showsListAndPlaceholder(){
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_listToSearchPane_showsSearchAndPlaceholder(){
        singlePane_listToSearchPane_showsSearch()
        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_listToSearchPane_showsSearchAndReturnsToList(){
        expanded_listToSearchPane_showsSearchAndPlaceholder()
        composeRule.goBack()
        expanded_initialState_showsListAndPlaceholder()
    }

    @Test
    @TestExpandedWidth
    fun expanded_listToAddPane_showsListAndAdd(){
        singlePane_listToAddPane_showsAdd()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_listToAddPane_warningDialog_staysOrReturns_showsListAndPlaceholder(){
        expanded_listToAddPane_showsListAndAdd()
        //back
        composeRule.goBack()
        composeRule.onNodeWithStringId(R.string.discard_changes).assertIsDisplayed()
        //dismiss warning dialog
        composeRule.onNodeWithStringId(R.string.cancel_label).performClick()
        composeRule.onNodeWithStringId(R.string.discard_changes).assertIsNotDisplayed()
        //back & confirm cancellation
        composeRule.onNodeWithDescriptionStringId(R.string.back).performClick()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        expanded_initialState_showsListAndPlaceholder()
    }

    @Test
    @TestExpandedWidth
    fun expanded_listToAddPane_selectNoteConfirmCancelDisplaysDetails_showsListAndDetails(){
        //open add
        expanded_listToAddPane_showsListAndAdd()
        //select note from list
        composeRule.waitUntil {
            composeRule.onNodeWithTag("note:1").isDisplayed()
        }
        composeRule.onNodeWithTag("note:1").performClick()
        //discard changes dialog & confirm
        composeRule.onNodeWithStringId(R.string.discard_changes).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()

        composeRule.onNodeWithStringId(R.string.discard_changes).assertIsNotDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_listToDetailsPane_showsListAndDetails(){
        singlePane_listToDetailsPane_showsDetails()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_listPane_warningDialog_multiSelectAndDelete(){
        //select note
        composeRule.waitUntil {
            composeRule.onNodeWithTag("note:1").isDisplayed()
        }
        composeRule.onNodeWithTag("note:1").performTouchInput { longClick() }
        //click delete selected
        composeRule.onNodeWithDescriptionStringId(com.ainsln.core.ui.R.string.delete_selected).performClick()
        composeRule.onNodeWithStringId(R.string.delete_notes).assertIsDisplayed()
        //dismiss warning dialog
        composeRule.onNodeWithStringId(R.string.cancel_label).performClick()
        composeRule.onNodeWithStringId(R.string.delete_notes).assertIsNotDisplayed()
        //click delete selected & confirm cancellation
        composeRule.onNodeWithDescriptionStringId(com.ainsln.core.ui.R.string.delete_selected).performClick()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        //after deletion
        composeRule.onNodeWithStringId(R.string.delete_notes).assertIsNotDisplayed()
        expanded_initialState_showsListAndPlaceholder()
    }

    @Test
    @TestExpandedWidth
    fun expanded_listPane_warningDialog_deleteSelectedClearsDetailsPane(){
        //open note details
        expanded_listToDetailsPane_showsListAndDetails()
        //select & delete
        composeRule.onNodeWithTag("note:1").performTouchInput { longClick() }
        composeRule.onNodeWithDescriptionStringId(com.ainsln.core.ui.R.string.delete_selected).performClick()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        //after deletion
        composeRule.onNodeWithStringId(R.string.delete_notes).assertIsNotDisplayed()
        expanded_initialState_showsListAndPlaceholder()
    }

    //#endregion

    //#region Two Panes: From Details
    @Test
    @TestExpandedWidth
    fun expanded_detailPane_editButtonOpensEditPane_showsListAndEditor(){
        expanded_listToDetailsPane_showsListAndDetails()
        composeRule.waitAndClickByDescriptionId(R.string.edit_note)
        composeRule.onNodeWithStringId(R.string.note_edit_title).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_detailPane_deleteButtonOpensWarningDialog_showsListAndDetails(){
        expanded_listToDetailsPane_showsListAndDetails()
        composeRule.waitAndClickByDescriptionId(R.string.delete_note)
        composeRule.onNodeWithStringId(R.string.delete_warning).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_detailPane_confirmDeleteNavigatesToListPane_showsListAndPlaceholder(){
        expanded_detailPane_deleteButtonOpensWarningDialog_showsListAndDetails()
        composeRule.onNodeWithDescriptionStringId(R.string.delete_note).performClick()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        expanded_initialState_showsListAndPlaceholder()
    }

    //#endregion

    //#region Two Panes: From Note Editor
    @Test
    @TestExpandedWidth
    fun expanded_editNotePane_editPaneCancelNavigatesBackToDetailPane_showsListAndDetails(){
        expanded_detailPane_editButtonOpensEditPane_showsListAndEditor()
        composeRule.onNodeWithDescriptionStringId(R.string.back).performClick()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_editNotePane_editPaneSaveNavigatesBackToDetailPane_showsListAndDetails(){
        expanded_detailPane_editButtonOpensEditPane_showsListAndEditor()
        composeRule.onNodeWithDescriptionStringId(R.string.save_note).performClick()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_editNotePane_deleteSelectedClearsEditorPane_showsListAndPlaceholder(){
        //open editor for note
        expanded_detailPane_editButtonOpensEditPane_showsListAndEditor()
        //select & delete
        composeRule.onNodeWithTag("note:1").performTouchInput { longClick() }
        composeRule.onNodeWithDescriptionStringId(com.ainsln.core.ui.R.string.delete_selected).performClick()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()
        //after deletion
        composeRule.onNodeWithStringId(R.string.delete_notes).assertIsNotDisplayed()
        expanded_initialState_showsListAndPlaceholder()
    }

    @Test
    @TestExpandedWidth
    fun expanded_editNotePane_selectAnotherNoteShowsCancelDialog_showsListAndEditor(){
        //open editor for note
        expanded_detailPane_editButtonOpensEditPane_showsListAndEditor()
        //select note from list
        composeRule.onNodeWithTag("note:2").performClick()
        //discard changes dialog
        composeRule.onNodeWithStringId(R.string.discard_changes).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_editNotePane_confirmCancelDisplaysSelectedNoteDetails_showsListAndDetails(){
        expanded_editNotePane_selectAnotherNoteShowsCancelDialog_showsListAndEditor()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()

        composeRule.onNodeWithStringId(R.string.discard_changes).assertIsNotDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_list_title).assertIsDisplayed()
    }

    //#endregion

    //#region Two Panes: From Search
    @Test
    @TestExpandedWidth
    fun expanded_searchPane_enterQueryDisplaysResults_showsSearchAndPlaceholder(){
        singlePane_searchPane_displaysResultsWhenQueryEntered()
        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_searchPane_selectNoteDisplaysDetailsPane_showsSearchAndDetails(){
        singlePane_searchPane_clickResultNavigatesToDetailPane()
        composeRule.onNodeWithText(sampleQuery).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_searchPane_selectAnotherNoteUpdatesDetailsPane_showsSearchAndDetails(){
        singlePane_searchPane_clickResultNavigatesToDetailPane()
        composeRule.onNodeWithTag("note:2").performClick()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
        composeRule.onNodeWithText(sampleQuery).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_searchPane_selectNoteDuringAddNoteShowsCancelDialog_showsSearchAndAdd(){
        //open add
        expanded_listToAddPane_showsListAndAdd()
        //open search and select note
        singlePane_searchPane_displaysResultsWhenQueryEntered()
        composeRule.onNodeWithTag("note:1").performClick()

        composeRule.onNodeWithStringId(R.string.discard_changes).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_searchPane_confirmSelectionDuringAddNavigatesToDetails_showsSearchAndDetails(){
        expanded_searchPane_selectNoteDuringAddNoteShowsCancelDialog_showsSearchAndAdd()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()

        composeRule.onNodeWithStringId(R.string.discard_changes).assertIsNotDisplayed()
        composeRule.onNodeWithText(sampleQuery).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_searchPane_deleteNoteClearsDetailsPane_showsSearchAndPlaceholder(){
        expanded_searchPane_selectNoteDisplaysDetailsPane_showsSearchAndDetails()
        composeRule.onNodeWithDescriptionStringId(R.string.delete_note).performClick()
        composeRule.onNodeWithStringId(R.string.ok_label).performClick()

        composeRule.onNodeWithStringId(R.string.notes_details_placeholder).assertIsDisplayed()
        composeRule.onNodeWithText(sampleQuery).assertIsDisplayed()
    }

    @Test
    @TestExpandedWidth
    fun expanded_searchPane_cancelDeleteAndSelectAnotherNoteDisplaysDetails_showsSearchAndDetails(){
        //open note
        expanded_searchPane_selectNoteDisplaysDetailsPane_showsSearchAndDetails()
        //cancel deletion
        composeRule.onNodeWithDescriptionStringId(R.string.delete_note).performClick()
        composeRule.onNodeWithStringId(R.string.cancel_label).performClick()
        //select another note
        composeRule.onNodeWithTag("note:2").performClick()
        composeRule.onNodeWithStringId(R.string.notes_detail_title).assertIsDisplayed()
        composeRule.onNodeWithText(sampleQuery).assertIsDisplayed()
    }

    //#endregion

}


