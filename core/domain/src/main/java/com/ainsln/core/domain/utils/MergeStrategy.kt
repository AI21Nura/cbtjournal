package com.ainsln.core.domain.utils

interface MergeStrategy<T, R> {
    fun merge(first: T, second: R): T
}
