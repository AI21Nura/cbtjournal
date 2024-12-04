package com.ainsln.core.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import javax.inject.Inject

interface IntentSender {
    fun send(context: Context, subject: String, text: String, email: String? = null)
}

class BaseIntentSender @Inject constructor() : IntentSender {

    override fun send(context: Context, subject: String, text: String, email: String?) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            email?.let {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            }
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }

        context.startActivity(
            Intent.createChooser(
                intent,
                subject
            )
        )
    }
}
