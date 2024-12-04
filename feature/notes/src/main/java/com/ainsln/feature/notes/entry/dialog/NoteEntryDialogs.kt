package com.ainsln.feature.notes.entry.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ainsln.core.ui.utils.MultiSelectionDialogArgs
import com.ainsln.core.ui.utils.MultiSelectionDialogCallbacks
import com.ainsln.feature.notes.entry.dialog.circle.EmotionsCircleDialog

@Composable
fun DistortionsDialog(
    initListIds: List<Long>,
    toggleDistortionsDialog: (Boolean) -> Unit,
    onUpdateDistortionsList: (List<Long>) -> Unit,
    distortionsSelectionDialog: @Composable (MultiSelectionDialogArgs) -> Unit
) {
    val callbacks = remember {
        object : MultiSelectionDialogCallbacks {

            override fun onSave(updatedList: List<Long>) {
                onUpdateDistortionsList(updatedList)
                toggleDistortionsDialog(false)
            }

            override fun onClose() {
                toggleDistortionsDialog(false)
            }
        }
    }

    distortionsSelectionDialog(object : MultiSelectionDialogArgs {
        override val initListIds: List<Long> = initListIds
        override val callbacks: MultiSelectionDialogCallbacks = callbacks
    })

}

@Composable
fun EmotionsDialog(
    initListIds: List<Long>,
    toggleEmotionsDialog: (Boolean) -> Unit,
    onUpdateEmotionsList: (List<Long>) -> Unit,
){
    val callbacks = remember {
        object : MultiSelectionDialogCallbacks {
            override fun onSave(updatedList: List<Long>) {
                onUpdateEmotionsList(updatedList)
                toggleEmotionsDialog(false)
            }

            override fun onClose() {
                toggleEmotionsDialog(false)
            }
        }
    }

    EmotionsCircleDialog(
        args = object : MultiSelectionDialogArgs {
            override val initListIds: List<Long> = initListIds
            override val callbacks: MultiSelectionDialogCallbacks = callbacks
        }
    )
}
