package com.ainsln.core.ui.utils


interface MultiSelectionDialogArgs {
    val initListIds: List<Long>
    val callbacks: MultiSelectionDialogCallbacks
}

interface MultiSelectionDialogCallbacks {
    fun onSave(updatedList: List<Long>)
    fun onClose()
}
