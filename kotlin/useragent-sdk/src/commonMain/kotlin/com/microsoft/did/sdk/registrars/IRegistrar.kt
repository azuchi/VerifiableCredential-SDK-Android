package com.microsoft.did.sdk.registrars

import com.microsoft.did.sdk.identifier.IdentifierDocument
import io.ktor.client.engine.HttpClientEngine

/**
 * @interface defining methods and properties
 * to be implemented by specific registration methods.
 */
abstract IRegistrar {

    /**
     * Registers the identifier document on the ledger
     * returning the identifier generated by the registrar.
     * @param identifierDocument to be registered.
     * @param signingKeyReference reference to the key to be used for signing request.
     */
    fun register() {

    }

    abstract fun onRegister()
}