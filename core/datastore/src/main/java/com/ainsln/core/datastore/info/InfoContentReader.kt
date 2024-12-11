package com.ainsln.core.datastore.info

import android.content.Context
import com.ainsln.core.datastore.info.model.GuideData
import com.ainsln.core.datastore.info.model.InfoData
import com.ainsln.core.datastore.utils.AppLocale
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

public class InfoContentReader @Inject constructor(
    @ApplicationContext private val context: Context
) : ContentReader<InfoData, GuideData> {

    override fun readInfo(localeCode: String): InfoData {
        val fileName = when(localeCode){
            AppLocale.Russian.code -> "info_content_ru.json"
            else -> "info_content_en.json"
        }
        return readData<InfoData>(fileName)

    }

    override fun readGuide(localeCode: String): GuideData {
        val fileName = when(localeCode){
            AppLocale.Russian.code -> "guide_ru.json"
            else -> "guide_en.json"
        }
        return readData<GuideData>(fileName)
    }

    private inline fun <reified T> readData(fileName: String): T{
        val str = context.assets.open(fileName)
            .bufferedReader(Charsets.UTF_8)
            .use { it.readText() }

        return Json.decodeFromString<T>(str)
    }
}
