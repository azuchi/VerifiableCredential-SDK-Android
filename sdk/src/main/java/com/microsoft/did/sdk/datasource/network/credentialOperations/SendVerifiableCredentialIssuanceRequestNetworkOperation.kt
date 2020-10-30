/*---------------------------------------------------------------------------------------------
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package com.microsoft.did.sdk.datasource.network.credentialOperations

import com.microsoft.did.sdk.credential.models.VerifiableCredential
import com.microsoft.did.sdk.credential.models.VerifiableCredentialContent
import com.microsoft.did.sdk.credential.service.models.serviceResponses.IssuanceServiceResponse
import com.microsoft.did.sdk.credential.service.validators.JwtValidator
import com.microsoft.did.sdk.crypto.protocols.jose.jws.JwsToken
import com.microsoft.did.sdk.datasource.network.PostNetworkOperation
import com.microsoft.did.sdk.datasource.network.apis.ApiProvider
import com.microsoft.did.sdk.util.controlflow.InvalidSignatureException
import com.microsoft.did.sdk.util.controlflow.IssuanceException
import com.microsoft.did.sdk.util.controlflow.Result
import com.microsoft.did.sdk.util.serializer.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class SendVerifiableCredentialIssuanceRequestNetworkOperation(
    url: String,
    serializedResponse: String,
    apiProvider: ApiProvider,
    private val jwtValidator: JwtValidator,
    private val serializer: Serializer
) : PostNetworkOperation<IssuanceServiceResponse, VerifiableCredential>() {
    override val call: suspend () -> Response<IssuanceServiceResponse> = { apiProvider.issuanceApis.sendResponse(url, serializedResponse) }

    override fun onSuccess(response: Response<IssuanceServiceResponse>): Result<VerifiableCredential> {
        return runBlocking(Dispatchers.IO) {
            val rawVerifiableCredential = response.body()?.vc ?: throw IssuanceException("No Verifiable Credential in Body.")
            val jwsToken = JwsToken.deserialize(rawVerifiableCredential, serializer)
            if (jwtValidator.verifySignature(jwsToken)) {
                val verifiableCredentialContent = serializer.parse(VerifiableCredentialContent.serializer(), jwsToken.content())
                Result.Success(VerifiableCredential(verifiableCredentialContent.jti, rawVerifiableCredential, verifiableCredentialContent))
            } else
                throw InvalidSignatureException("Signature is not Valid.")
        }
    }
}