// Copyright (c) Microsoft Corporation. All rights reserved

package com.microsoft.did.sdk

import com.microsoft.did.sdk.credential.service.validators.JwtDomainLinkageCredentialValidator
import com.microsoft.did.sdk.credential.service.validators.JwtValidator
import com.microsoft.did.sdk.identifier.models.identifierdocument.IdentifierResponse
import com.microsoft.did.sdk.identifier.resolvers.Resolver
import com.microsoft.did.sdk.util.controlflow.Result
import com.microsoft.did.sdk.util.serializer.Serializer
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LinkedDomainsServiceTest {
    private val serializer = Serializer()
    private val mockedResolver: Resolver = mockk()
    private val mockedJwtValidator: JwtValidator = mockk()
    private val mockedJwtDomainLinkageCredentialValidator: JwtDomainLinkageCredentialValidator
    private val linkedDomainsService: LinkedDomainsService

    init {
        mockedJwtDomainLinkageCredentialValidator = JwtDomainLinkageCredentialValidator(mockedJwtValidator, serializer)
        linkedDomainsService = LinkedDomainsService(mockk(relaxed = true), mockedResolver, mockedJwtDomainLinkageCredentialValidator)
    }

    @Test
    fun `test linked domains with single domain as string successfully`() {
        val didDocument = """{"@context":"https://www.w3.org/ns/did-resolution/v1","didDocument":{"id":"did:ion:EiC5-1uBg-YC2DvQRbI6eihDvk7DOYaQ08OB0I3jCe9Ydg:eyJkZWx0YSI6eyJwYXRjaGVzIjpbeyJhY3Rpb24iOiJyZXBsYWNlIiwiZG9jdW1lbnQiOnsicHVibGljX2tleXMiOlt7ImlkIjoiYW55U2lnbmluZ0tleUlkIiwiandrIjp7ImNydiI6InNlY3AyNTZrMSIsImt0eSI6IkVDIiwieCI6ImFHc01HMHU5Rlg2STU0cGVJS3FZb2tqblFQR2hMVVlUT1FOYzNuT3ZFMVEiLCJ5IjoiZmppbHFoZVdRWWtITkU3MHNoTVJ5TURyWnA4RUdDZkVfYUwzaC15Sm1RQSJ9LCJwdXJwb3NlIjpbImF1dGgiLCJnZW5lcmFsIl0sInR5cGUiOiJFY2RzYVNlY3AyNTZrMVZlcmlmaWNhdGlvbktleTIwMTkifV0sInNlcnZpY2VfZW5kcG9pbnRzIjpbeyJlbmRwb2ludCI6Imh0dHA6Ly9hbnkuZW5kcG9pbnQiLCJpZCI6ImFueVNlcnZpY2VFbmRwb2ludElkIiwidHlwZSI6ImFueVR5cGUifV19fV0sInVwZGF0ZV9jb21taXRtZW50IjoiRWlERkM2RE9Ed0JNeG5kX19oMTFSeDRObjFlOHpubFlPUjJhLVBqeUNva2NGZyJ9LCJzdWZmaXhfZGF0YSI6eyJkZWx0YV9oYXNoIjoiRWlBbExNMC1qem1DWi1FcElVZ0laQ2piWk5yMDFfVVBMbnd5MHdfT3I0Rks0dyIsInJlY292ZXJ5X2NvbW1pdG1lbnQiOiJFaUJDNGhTMVVHeVNnTmYzbWFMdnNKRUpxX05aQUlKa0pndTNKMTJMeGNESE93In19","@context":["https://www.w3.org/ns/did/v1",{"@base":"did:ion:EiC5-1uBg-YC2DvQRbI6eihDvk7DOYaQ08OB0I3jCe9Ydg:eyJkZWx0YSI6eyJwYXRjaGVzIjpbeyJhY3Rpb24iOiJyZXBsYWNlIiwiZG9jdW1lbnQiOnsicHVibGljX2tleXMiOlt7ImlkIjoiYW55U2lnbmluZ0tleUlkIiwiandrIjp7ImNydiI6InNlY3AyNTZrMSIsImt0eSI6IkVDIiwieCI6ImFHc01HMHU5Rlg2STU0cGVJS3FZb2tqblFQR2hMVVlUT1FOYzNuT3ZFMVEiLCJ5IjoiZmppbHFoZVdRWWtITkU3MHNoTVJ5TURyWnA4RUdDZkVfYUwzaC15Sm1RQSJ9LCJwdXJwb3NlIjpbImF1dGgiLCJnZW5lcmFsIl0sInR5cGUiOiJFY2RzYVNlY3AyNTZrMVZlcmlmaWNhdGlvbktleTIwMTkifV0sInNlcnZpY2VfZW5kcG9pbnRzIjpbeyJlbmRwb2ludCI6Imh0dHA6Ly9hbnkuZW5kcG9pbnQiLCJpZCI6ImFueVNlcnZpY2VFbmRwb2ludElkIiwidHlwZSI6ImFueVR5cGUifV19fV0sInVwZGF0ZV9jb21taXRtZW50IjoiRWlERkM2RE9Ed0JNeG5kX19oMTFSeDRObjFlOHpubFlPUjJhLVBqeUNva2NGZyJ9LCJzdWZmaXhfZGF0YSI6eyJkZWx0YV9oYXNoIjoiRWlBbExNMC1qem1DWi1FcElVZ0laQ2piWk5yMDFfVVBMbnd5MHdfT3I0Rks0dyIsInJlY292ZXJ5X2NvbW1pdG1lbnQiOiJFaUJDNGhTMVVHeVNnTmYzbWFMdnNKRUpxX05aQUlKa0pndTNKMTJMeGNESE93In19"}],"service":[{"id":"#anyServiceEndpointId","type":"LinkedDomains","serviceEndpoint":"http://any.endpoint"}],"publicKey":[{"id":"#anySigningKeyId","controller":"","type":"EcdsaSecp256k1VerificationKey2019","publicKeyJwk":{"crv":"secp256k1","kty":"EC","x":"aGsMG0u9FX6I54peIKqYokjnQPGhLUYTOQNc3nOvE1Q","y":"fjilqheWQYkHNE70shMRyMDrZp8EGCfE_aL3h-yJmQA"}}],"authentication":["#anySigningKeyId"]},"methodMetadata":{"published":false,"recoveryCommitment":"EiBC4hS1UGySgNf3maLvsJEJq_NZAIJkJgu3J12LxcDHOw","updateCommitment":"EiDFC6DODwBMxnd__h11Rx4Nn1e8znlYOR2a-PjyCokcFg"},"resolverMetadata":{"driverId":"did:ion","driver":"HttpDriver","retrieved":"2020-10-20T05:52:41.693Z","duration":"126.6567ms"}}"""
        val didDoc = serializer.parse(IdentifierResponse.serializer(), didDocument)
        coEvery { mockedResolver.resolve(didDoc.didDocument.id) } returns Result.Success(didDoc.didDocument)
        runBlocking {
            val linkedDomainsResult = linkedDomainsService.getDomainUrlFromRelyingPartyDid(didDoc.didDocument.id)
            assertThat(linkedDomainsResult).isInstanceOf(Result.Success::class.java)
            assertThat((linkedDomainsResult as Result.Success).payload).isEqualTo("http://any.endpoint")
        }
    }

    @Test
    fun `test linked domains with an array of domains successfully`() {
        val didDocumentMapArray = """{"@context":"https://www.w3.org/ns/did-resolution/v1","didDocument":{"id":"did:ion:EiC5-1uBg-YC2DvQRbI6eihDvk7DOYaQ08OB0I3jCe9Ydg:eyJkZWx0YSI6eyJwYXRjaGVzIjpbeyJhY3Rpb24iOiJyZXBsYWNlIiwiZG9jdW1lbnQiOnsicHVibGljX2tleXMiOlt7ImlkIjoiYW55U2lnbmluZ0tleUlkIiwiandrIjp7ImNydiI6InNlY3AyNTZrMSIsImt0eSI6IkVDIiwieCI6ImFHc01HMHU5Rlg2STU0cGVJS3FZb2tqblFQR2hMVVlUT1FOYzNuT3ZFMVEiLCJ5IjoiZmppbHFoZVdRWWtITkU3MHNoTVJ5TURyWnA4RUdDZkVfYUwzaC15Sm1RQSJ9LCJwdXJwb3NlIjpbImF1dGgiLCJnZW5lcmFsIl0sInR5cGUiOiJFY2RzYVNlY3AyNTZrMVZlcmlmaWNhdGlvbktleTIwMTkifV0sInNlcnZpY2VfZW5kcG9pbnRzIjpbeyJlbmRwb2ludCI6Imh0dHA6Ly9hbnkuZW5kcG9pbnQiLCJpZCI6ImFueVNlcnZpY2VFbmRwb2ludElkIiwidHlwZSI6ImFueVR5cGUifV19fV0sInVwZGF0ZV9jb21taXRtZW50IjoiRWlERkM2RE9Ed0JNeG5kX19oMTFSeDRObjFlOHpubFlPUjJhLVBqeUNva2NGZyJ9LCJzdWZmaXhfZGF0YSI6eyJkZWx0YV9oYXNoIjoiRWlBbExNMC1qem1DWi1FcElVZ0laQ2piWk5yMDFfVVBMbnd5MHdfT3I0Rks0dyIsInJlY292ZXJ5X2NvbW1pdG1lbnQiOiJFaUJDNGhTMVVHeVNnTmYzbWFMdnNKRUpxX05aQUlKa0pndTNKMTJMeGNESE93In19","@context":["https://www.w3.org/ns/did/v1",{"@base":"did:ion:EiC5-1uBg-YC2DvQRbI6eihDvk7DOYaQ08OB0I3jCe9Ydg:eyJkZWx0YSI6eyJwYXRjaGVzIjpbeyJhY3Rpb24iOiJyZXBsYWNlIiwiZG9jdW1lbnQiOnsicHVibGljX2tleXMiOlt7ImlkIjoiYW55U2lnbmluZ0tleUlkIiwiandrIjp7ImNydiI6InNlY3AyNTZrMSIsImt0eSI6IkVDIiwieCI6ImFHc01HMHU5Rlg2STU0cGVJS3FZb2tqblFQR2hMVVlUT1FOYzNuT3ZFMVEiLCJ5IjoiZmppbHFoZVdRWWtITkU3MHNoTVJ5TURyWnA4RUdDZkVfYUwzaC15Sm1RQSJ9LCJwdXJwb3NlIjpbImF1dGgiLCJnZW5lcmFsIl0sInR5cGUiOiJFY2RzYVNlY3AyNTZrMVZlcmlmaWNhdGlvbktleTIwMTkifV0sInNlcnZpY2VfZW5kcG9pbnRzIjpbeyJlbmRwb2ludCI6Imh0dHA6Ly9hbnkuZW5kcG9pbnQiLCJpZCI6ImFueVNlcnZpY2VFbmRwb2ludElkIiwidHlwZSI6ImFueVR5cGUifV19fV0sInVwZGF0ZV9jb21taXRtZW50IjoiRWlERkM2RE9Ed0JNeG5kX19oMTFSeDRObjFlOHpubFlPUjJhLVBqeUNva2NGZyJ9LCJzdWZmaXhfZGF0YSI6eyJkZWx0YV9oYXNoIjoiRWlBbExNMC1qem1DWi1FcElVZ0laQ2piWk5yMDFfVVBMbnd5MHdfT3I0Rks0dyIsInJlY292ZXJ5X2NvbW1pdG1lbnQiOiJFaUJDNGhTMVVHeVNnTmYzbWFMdnNKRUpxX05aQUlKa0pndTNKMTJMeGNESE93In19"}],"service":[{"id":"#anyServiceEndpointId","type":"LinkedDomains","serviceEndpoint": { "origins": [ "someValue1", "someValue2" ] }}],"publicKey":[{"id":"#anySigningKeyId","controller":"","type":"EcdsaSecp256k1VerificationKey2019","publicKeyJwk":{"crv":"secp256k1","kty":"EC","x":"aGsMG0u9FX6I54peIKqYokjnQPGhLUYTOQNc3nOvE1Q","y":"fjilqheWQYkHNE70shMRyMDrZp8EGCfE_aL3h-yJmQA"}}],"authentication":["#anySigningKeyId"]},"methodMetadata":{"published":false,"recoveryCommitment":"EiBC4hS1UGySgNf3maLvsJEJq_NZAIJkJgu3J12LxcDHOw","updateCommitment":"EiDFC6DODwBMxnd__h11Rx4Nn1e8znlYOR2a-PjyCokcFg"},"resolverMetadata":{"driverId":"did:ion","driver":"HttpDriver","retrieved":"2020-10-20T05:52:41.693Z","duration":"126.6567ms"}}"""
        val didDocMapArray = serializer.parse(IdentifierResponse.serializer(), didDocumentMapArray)
        coEvery { mockedResolver.resolve(didDocMapArray.didDocument.id) } returns Result.Success(didDocMapArray.didDocument)
        runBlocking {
            val linkedDomainsArrResult = linkedDomainsService.getDomainUrlFromRelyingPartyDid(didDocMapArray.didDocument.id)
            assertThat(linkedDomainsArrResult).isInstanceOf(Result.Success::class.java)
            assertThat((linkedDomainsArrResult as Result.Success).payload).isEqualTo("someValue1")
        }
    }

    @Test
    fun `test did without service endpoints`() {
        val suppliedDidWithoutServiceEndpoint = "did:ion:EiAGYVovJcSCiUWuX9K1eFHBcv4BorIjMG7e44hf1hKtGg?-ion-initial-state=eyJkZWx0YV9oYXNoIjoiRWlDUDBKOEVyRmZXeEw2WGNqT2g4STU2Smp3bXhVQ01zWk5yT2ZoSWFMbUxVQSIsInJlY292ZXJ5X2NvbW1pdG1lbnQiOiJFaUQyNHVWbkd1ZW9aZUs0OEl1aE9BZ1c4Z3NvTmdncHV2bGRRSUVjM09wNFZRIn0.eyJ1cGRhdGVfY29tbWl0bWVudCI6IkVpQXpYZmprQkE1Z05tZWJOam56TmhkYzYycjdCUkJremcyOXFLWVBON3MtQUEiLCJwYXRjaGVzIjpbeyJhY3Rpb24iOiJyZXBsYWNlIiwiZG9jdW1lbnQiOnsicHVibGljX2tleXMiOlt7ImlkIjoiY2FwcHRvc28taXNzdWVyLXNpdGUtc2lnbmluZy1rZXkiLCJ0eXBlIjoiRWNkc2FTZWNwMjU2azFWZXJpZmljYXRpb25LZXkyMDE5IiwiandrIjp7ImtpZCI6Imh0dHBzOi8vdmMtMjAyMC1rdi52YXVsdC5henVyZS5uZXQva2V5cy9jYXBwdG9zby1pc3N1ZXItc2l0ZS1zaWduaW5nLWtleS9lZTM5MDUxNGFhN2Y0ZjNiYTAzZjViNDM3ZjNlYjRlZSIsImt0eSI6IkVDIiwiY3J2Ijoic2VjcDI1NmsxIiwieCI6IkFIQ29XM1k4cHVvRmFqa0JqeU1HcUtwZTJ3TktFb1BaSWtINDVxelJaeVUiLCJ5IjoiQ3JaaU02VU1sLVFNMnlCYTgtaS1kTlM3X1JyeDA3VnN3OVlTVlA4UzBxTSJ9LCJwdXJwb3NlIjpbImF1dGgiLCJnZW5lcmFsIl19XX19XX0"
        val actualDidDocString = """{"@context":"https://www.w3.org/ns/did-resolution/v1","didDocument":{"id":"did:ion:EiAGYVovJcSCiUWuX9K1eFHBcv4BorIjMG7e44hf1hKtGg?-ion-initial-state=eyJkZWx0YV9oYXNoIjoiRWlDUDBKOEVyRmZXeEw2WGNqT2g4STU2Smp3bXhVQ01zWk5yT2ZoSWFMbUxVQSIsInJlY292ZXJ5X2NvbW1pdG1lbnQiOiJFaUQyNHVWbkd1ZW9aZUs0OEl1aE9BZ1c4Z3NvTmdncHV2bGRRSUVjM09wNFZRIn0.eyJ1cGRhdGVfY29tbWl0bWVudCI6IkVpQXpYZmprQkE1Z05tZWJOam56TmhkYzYycjdCUkJremcyOXFLWVBON3MtQUEiLCJwYXRjaGVzIjpbeyJhY3Rpb24iOiJyZXBsYWNlIiwiZG9jdW1lbnQiOnsicHVibGljX2tleXMiOlt7ImlkIjoiY2FwcHRvc28taXNzdWVyLXNpdGUtc2lnbmluZy1rZXkiLCJ0eXBlIjoiRWNkc2FTZWNwMjU2azFWZXJpZmljYXRpb25LZXkyMDE5IiwiandrIjp7ImtpZCI6Imh0dHBzOi8vdmMtMjAyMC1rdi52YXVsdC5henVyZS5uZXQva2V5cy9jYXBwdG9zby1pc3N1ZXItc2l0ZS1zaWduaW5nLWtleS9lZTM5MDUxNGFhN2Y0ZjNiYTAzZjViNDM3ZjNlYjRlZSIsImt0eSI6IkVDIiwiY3J2Ijoic2VjcDI1NmsxIiwieCI6IkFIQ29XM1k4cHVvRmFqa0JqeU1HcUtwZTJ3TktFb1BaSWtINDVxelJaeVUiLCJ5IjoiQ3JaaU02VU1sLVFNMnlCYTgtaS1kTlM3X1JyeDA3VnN3OVlTVlA4UzBxTSJ9LCJwdXJwb3NlIjpbImF1dGgiLCJnZW5lcmFsIl19XX19XX0","@context":["https://www.w3.org/ns/did/v1",{"@base":"did:ion:EiAGYVovJcSCiUWuX9K1eFHBcv4BorIjMG7e44hf1hKtGg?-ion-initial-state=eyJkZWx0YV9oYXNoIjoiRWlDUDBKOEVyRmZXeEw2WGNqT2g4STU2Smp3bXhVQ01zWk5yT2ZoSWFMbUxVQSIsInJlY292ZXJ5X2NvbW1pdG1lbnQiOiJFaUQyNHVWbkd1ZW9aZUs0OEl1aE9BZ1c4Z3NvTmdncHV2bGRRSUVjM09wNFZRIn0.eyJ1cGRhdGVfY29tbWl0bWVudCI6IkVpQXpYZmprQkE1Z05tZWJOam56TmhkYzYycjdCUkJremcyOXFLWVBON3MtQUEiLCJwYXRjaGVzIjpbeyJhY3Rpb24iOiJyZXBsYWNlIiwiZG9jdW1lbnQiOnsicHVibGljX2tleXMiOlt7ImlkIjoiY2FwcHRvc28taXNzdWVyLXNpdGUtc2lnbmluZy1rZXkiLCJ0eXBlIjoiRWNkc2FTZWNwMjU2azFWZXJpZmljYXRpb25LZXkyMDE5IiwiandrIjp7ImtpZCI6Imh0dHBzOi8vdmMtMjAyMC1rdi52YXVsdC5henVyZS5uZXQva2V5cy9jYXBwdG9zby1pc3N1ZXItc2l0ZS1zaWduaW5nLWtleS9lZTM5MDUxNGFhN2Y0ZjNiYTAzZjViNDM3ZjNlYjRlZSIsImt0eSI6IkVDIiwiY3J2Ijoic2VjcDI1NmsxIiwieCI6IkFIQ29XM1k4cHVvRmFqa0JqeU1HcUtwZTJ3TktFb1BaSWtINDVxelJaeVUiLCJ5IjoiQ3JaaU02VU1sLVFNMnlCYTgtaS1kTlM3X1JyeDA3VnN3OVlTVlA4UzBxTSJ9LCJwdXJwb3NlIjpbImF1dGgiLCJnZW5lcmFsIl19XX19XX0"}],"publicKey":[{"id":"#capptoso-issuer-site-signing-key","controller":"","type":"EcdsaSecp256k1VerificationKey2019","publicKeyJwk":{"kid":"https://vc-2020-kv.vault.azure.net/keys/capptoso-issuer-site-signing-key/ee390514aa7f4f3ba03f5b437f3eb4ee","kty":"EC","crv":"secp256k1","x":"AHCoW3Y8puoFajkBjyMGqKpe2wNKEoPZIkH45qzRZyU","y":"CrZiM6UMl-QM2yBa8-i-dNS7_Rrx07Vsw9YSVP8S0qM"}}],"authentication":["#capptoso-issuer-site-signing-key"]},"methodMetadata":{"published":false,"recoveryCommitment":"EiD24uVnGueoZeK48IuhOAgW8gsoNggpuvldQIEc3Op4VQ","updateCommitment":"EiAzXfjkBA5gNmebNjnzNhdc62r7BRBkzg29qKYPN7s-AA"},"resolverMetadata":{"driverId":"did:ion","driver":"HttpDriver","retrieved":"2020-10-21T20:03:42.743Z","duration":"115.0629ms"}}"""
        val actualDidDoc = serializer.parse(IdentifierResponse.serializer(), actualDidDocString)
        coEvery { mockedResolver.resolve(suppliedDidWithoutServiceEndpoint) } returns Result.Success(actualDidDoc.didDocument)
        runBlocking {
            val actualDomainUrlResult = linkedDomainsService.getDomainUrlFromRelyingPartyDid(suppliedDidWithoutServiceEndpoint)
            assertThat(actualDomainUrlResult).isInstanceOf(Result.Success::class.java)
            assertThat((actualDomainUrlResult as Result.Success).payload).isEqualTo("")
        }
    }
}