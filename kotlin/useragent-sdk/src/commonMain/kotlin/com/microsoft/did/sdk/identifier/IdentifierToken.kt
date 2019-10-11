package com.microsoft.did.sdk.identifier

import com.microsoft.did.sdk.crypto.CryptoOperations
import com.microsoft.did.sdk.identifier.document.IdentifierDocument
import com.microsoft.did.sdk.registrars.IRegistrar
import com.microsoft.did.sdk.resolvers.IResolver
import com.microsoft.did.sdk.utilities.MinimalJson
import kotlinx.serialization.Serializable

@Serializable
data class IdentifierToken (
    val document: IdentifierDocument,
    val alias: String,
    val signatureKeyReference: String,
    val encryptionKeyReference: String
) {
    companion object {
        private fun tokenize(identifier: Identifier): IdentifierToken {
            return IdentifierToken(
                identifier.document,
                identifier.alias,
                identifier.signatureKeyReference,
                identifier.encryptionKeyReference
            )
        }

        fun serialize(identifier: Identifier): String {
            val token = tokenize(identifier)
            return MinimalJson.serializer.stringify(IdentifierToken.serializer(), token)
        }

        fun deserialize(
            identifierToken: String,
            cryptoOperations: CryptoOperations,
            resolver: IResolver,
            registrar: IRegistrar
        ): Identifier {
            val token = MinimalJson.serializer.parse(IdentifierToken.serializer(), identifierToken)
            return Identifier(
                alias = token.alias,
                document = token.document,
                signatureKeyReference = token.signatureKeyReference,
                encryptionKeyReference = token.encryptionKeyReference,
                cryptoOperations = cryptoOperations,
                resolver = resolver,
                registrar = registrar
            )
        }
    }
}