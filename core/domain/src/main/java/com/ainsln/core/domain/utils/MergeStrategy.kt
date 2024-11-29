package com.ainsln.core.domain.utils

interface MergeStrategy<T, R> {
    fun merge(first: T, second: R): T
}

interface MergeStrategyTriple<T, R, U> {
    fun merge(first: T, second: R, third: U): T
}
