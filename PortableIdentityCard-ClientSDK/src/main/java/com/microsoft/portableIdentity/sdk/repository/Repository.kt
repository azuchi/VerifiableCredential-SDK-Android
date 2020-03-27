// Copyright (c) Microsoft Corporation. All rights reserved

package com.microsoft.portableIdentity.sdk.repository

import com.microsoft.portableIdentity.sdk.credentials.deprecated.ClaimObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val store: Store) {

    suspend fun saveClaim(claim: ClaimObject): Boolean {
        return store.saveClaim(claim)
    }

    suspend fun getClaims(): List<ClaimObject> {
        return store.getClaims()
    }
}