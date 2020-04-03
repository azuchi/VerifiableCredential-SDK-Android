package com.microsoft.portableIdentity.sdk.credentials.deprecated

import com.microsoft.portableIdentity.sdk.credentials.deprecated.ClaimDetail
import com.microsoft.portableIdentity.sdk.crypto.CryptoOperations
import com.microsoft.portableIdentity.sdk.crypto.protocols.jose.DidKeyResolver
import com.microsoft.portableIdentity.sdk.crypto.protocols.jose.jws.JwsToken
import com.microsoft.portableIdentity.sdk.resolvers.IResolver
import com.microsoft.portableIdentity.sdk.utilities.SdkLog
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("jws")
data class SignedClaimDetail(
    val data: String
): ClaimDetail {
    @Required
    override val type: String
        get() = "jws"

    override suspend fun verify(cryptoOperations: CryptoOperations, resolver: IResolver) {
        val claimDetail = JwsToken(data)
        DidKeyResolver.verifyJws(claimDetail, cryptoOperations, resolver)
    }
}