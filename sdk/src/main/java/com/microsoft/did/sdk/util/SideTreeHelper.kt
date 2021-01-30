// Copyright (c) Microsoft Corporation. All rights reserved

package com.microsoft.did.sdk.util

import android.util.Base64
import com.microsoft.did.sdk.crypto.CryptoOperations
import com.microsoft.did.sdk.crypto.DigestAlgorithm
import com.nimbusds.jose.jwk.JWK
import org.erdtman.jcs.JsonCanonicalizer
import javax.inject.Inject

class SideTreeHelper @Inject constructor(private val cryptoOperations: CryptoOperations) {

    fun canonicalizeAndMultiHash(json: String): String {
        val jsonCanonicalizer = JsonCanonicalizer(json)
        val hashed = cryptoOperations.digest(jsonCanonicalizer.encodedUTF8, DigestAlgorithm.Sha256())
        val hashedInfo = prependMultiHashInfo(hashed)
        return Base64.encodeToString(hashedInfo, Base64.URL_SAFE)
    }

    /**
     * Creates a commitment value that is used by Sidetree to verify the origin of the next request
     */
    fun createCommitmentValue(key: JWK): String {
        return canonicalizeAndDoubleMultiHash(key.toJSONString())
    }

    /**
     * Canonicalize a JSON String and performs a multi hash on it
     */
    private fun canonicalizeAndDoubleMultiHash(json: String): String {
        val jsonCanonicalizer = JsonCanonicalizer(json)
        val hashed = cryptoOperations.digest(jsonCanonicalizer.encodedUTF8, DigestAlgorithm.Sha256())
        val doubleHashed = cryptoOperations.digest(hashed, DigestAlgorithm.Sha256())
        val doubleHashedInfo = prependMultiHashInfo(doubleHashed)
        return Base64.encodeToString(doubleHashedInfo, Base64.URL_SAFE)
    }

    /**
     * Prepend the hash value with the hash algorithm code and digest length to be in multihash format as expected by Sidetree
     */
    private fun prependMultiHashInfo(bytes: ByteArray): ByteArray {
        return byteArrayOf(Constants.SIDETREE_MULTIHASH_CODE.toByte(), Constants.SIDETREE_MULTIHASH_LENGTH.toByte()) + bytes
    }
}