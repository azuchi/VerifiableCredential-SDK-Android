package com.microsoft.portableIdentity.sdk.crypto.protocols.jose.jws

import com.microsoft.portableIdentity.sdk.crypto.protocols.jose.JoseConstants
import com.microsoft.portableIdentity.sdk.utilities.*
import kotlinx.serialization.Serializable

/**
 * JWS signature used by the general JSON
 */
@Serializable
data class JwsSignature (
    /**
     * The protected (signed) header.
     */
    val protected: String,

    /**
     * The unprotected (unverified) header.
     */
    val header: Map<String, String>?,

    /**
     * The JWS Signature
     */
    val signature: String
) {
    fun getKid(): String? {
        return getMember(JoseConstants.Kid.value)
    }
    
    fun getAlg(): String? {
        return getMember(JoseConstants.Alg.value)
    }
    
    private fun getMember(member: String): String? {
        if (protected.isNotEmpty()) {
            val jsonProtected = Base64Url.decode(protected)
            val mapObject = Serializer.parseMap(byteArrayToString(jsonProtected), String::class, String::class)
            if (mapObject.containsKey(member)) {
                return mapObject[member]
            }
        }
        if (!header.isNullOrEmpty() && header.containsKey(member)) {
            return header[member]
        }
        return null
    }
}