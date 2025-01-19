package com.ainsln.core.database.dao

import com.ainsln.core.database.model.entity.RecentSearchEntity
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.Date

@HiltAndroidTest
class RecentSearchesDaoTest : BaseDaoTest() {

    @Test
    fun getAllReturnsEmptyListInitially() = runTest {
        val searches = recentSearchesDao.get(10).first()
        assertThat(searches).isEmpty()
    }

    @Test
    fun insertAndGetSingleItem() = runTest {
        val search = RecentSearchEntity("test query", Date())
        recentSearchesDao.upsert(search)

        val result = recentSearchesDao.get(10).first()
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(search)
    }

    @Test
    fun upsertUpdatesDatesForSameQuery() = runTest {
        val oldDate = Date(1000)
        val newDate = Date(2000)

        val oldSearch = RecentSearchEntity("test query", oldDate)
        val newSearch = RecentSearchEntity("test query", newDate)

        recentSearchesDao.upsert(oldSearch)
        recentSearchesDao.upsert(newSearch)

        val result = recentSearchesDao.get(10).first()
        assertThat(result).hasSize(1)
        assertThat(result[0].queriedDate).isEqualTo(newDate)
    }

    @Test
    fun getLimitWorksCorrectly() = runTest {
        val searches = listOf(
            RecentSearchEntity("query1", Date(1000)),
            RecentSearchEntity("query2", Date(2000)),
            RecentSearchEntity("query3", Date(3000))
        )

        searches.forEach { recentSearchesDao.upsert(it) }

        val result = recentSearchesDao.get(2).first()
        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(searches[2])
        assertThat(result[1]).isEqualTo(searches[1])
    }

    @Test
    fun deleteSingleItemWorks() = runTest {
        val search = RecentSearchEntity("test query", Date())
        recentSearchesDao.upsert(search)
        recentSearchesDao.delete(search)

        val result = recentSearchesDao.get(10).first()
        assertThat(result).isEmpty()
    }

    @Test
    fun clearDeletesAllItems() = runTest {
        val searches = listOf(
            RecentSearchEntity("query1", Date()),
            RecentSearchEntity("query2", Date())
        )

        searches.forEach { recentSearchesDao.upsert(it) }
        recentSearchesDao.clear()

        val result = recentSearchesDao.get(10).first()
        assertThat(result).isEmpty()
    }

    @Test
    fun specialCharactersInQueryWorkCorrectly() = runTest {
        val specialQuery = "!@#$%^&*()_+-=[]{}|;:'\",.<>?/\\"
        val search = RecentSearchEntity(specialQuery, Date())
        recentSearchesDao.upsert(search)

        val result = recentSearchesDao.get(10).first()
        assertThat(result).hasSize(1)
        assertThat(result[0].query).isEqualTo(specialQuery)
    }
}
