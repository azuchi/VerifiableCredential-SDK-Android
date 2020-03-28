/*---------------------------------------------------------------------------------------------
 *  Copyright (c) Microsoft Corporation. All rights reserved.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package com.microsoft.portableIdentity.sdk.cards

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.microsoft.portableIdentity.sdk.auth.models.contracts.display.DisplayContract
import com.microsoft.portableIdentity.sdk.cards.verifiableCredential.VerifiableCredential
import kotlinx.serialization.Serializable

/**
 * Data model to describe a Portable Identity Card.
 */
@Entity
@Serializable
data class PortableIdentityCard (

    @PrimaryKey
    val id: String,

    val verifiableCredential: VerifiableCredential,

    val displayContract: DisplayContract
)