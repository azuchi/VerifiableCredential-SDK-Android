// Copyright (c) Microsoft Corporation. All rights reserved

package com.microsoft.did.sdk.crypto.spi

import java.security.spec.KeySpec

class EcPairwiseKeySpec(
    val seed: ByteArray,
    val userDid: String,
    val peerDid: String
) : KeySpec