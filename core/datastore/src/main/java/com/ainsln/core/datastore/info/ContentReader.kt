package com.ainsln.core.datastore.info


public interface ContentReader <T,R> {
    public fun readInfo(localeCode: String): T
    public fun readGuide(localeCode: String): R
}

