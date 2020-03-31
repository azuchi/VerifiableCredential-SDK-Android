/*---------------------------------------------------------------------------------------------
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package com.microsoft.portableIdentity.sdk.repository.networking.apis

import com.microsoft.portableIdentity.sdk.auth.models.contracts.PicContract
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface PortableIdentityCardApi {

    @GET
    fun getContract(@Url overrideUrl: String): Deferred<Response<PicContract>>

    @GET
    fun getRequest(@Url overrideUrl: String): Deferred<Response<String>>

    @POST
    fun sendResponse(@Url overrideUrl: String, @Body body: String): Deferred<Response<Unit>>
}