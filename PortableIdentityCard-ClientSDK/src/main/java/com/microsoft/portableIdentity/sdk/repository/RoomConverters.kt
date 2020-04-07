/*---------------------------------------------------------------------------------------------
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package com.microsoft.portableIdentity.sdk.repository

import androidx.room.TypeConverter
import com.microsoft.portableIdentity.sdk.auth.models.contracts.display.DisplayContract
import com.microsoft.portableIdentity.sdk.cards.verifiableCredential.VerifiableCredential
import com.microsoft.portableIdentity.sdk.cards.verifiableCredential.VerifiableCredentialDescriptor
import com.microsoft.portableIdentity.sdk.utilities.Serializer

object RoomConverters {

    @TypeConverter
    @JvmStatic
    fun displayContractToString(displayContract: DisplayContract) = Serializer.stringify(DisplayContract.serializer(), displayContract)

    @TypeConverter
    @JvmStatic
    fun stringToDisplayContract(serializedContract: String) = Serializer.parse(DisplayContract.serializer(), serializedContract)

    @TypeConverter
    @JvmStatic
    fun verifiableCredentialToString(vc: VerifiableCredential) = Serializer.stringify(VerifiableCredential.serializer(), vc)

    @TypeConverter
    @JvmStatic
    fun stringToVerifiableCredential(serializedVc: String) = Serializer.parse(VerifiableCredential.serializer(), serializedVc)
}