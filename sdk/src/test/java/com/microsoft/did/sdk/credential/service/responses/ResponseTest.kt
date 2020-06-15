// Copyright (c) Microsoft Corporation. All rights reserved

package com.microsoft.did.sdk.credential.service.responses

import com.microsoft.did.sdk.credential.service.IssuanceResponse
import com.microsoft.did.sdk.credential.service.models.attestations.CredentialAttestations
import com.microsoft.did.sdk.credential.service.models.contracts.PicContract
import com.microsoft.did.sdk.credential.service.IssuanceRequest
import com.microsoft.did.sdk.credential.models.PortableIdentityCard
import com.microsoft.did.sdk.credential.receipts.ReceiptAction
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ResponseTest {
    var response: IssuanceResponse
    var request: IssuanceRequest
    private val attestations: CredentialAttestations = mockk()
    private var picContract: PicContract = mockk()
    private val entityName = "testEntityName"
    private val entityDid = "testEntityDid"
    private val issuedBy = "testIssuer"
    private val issuer = "testIssuerDid"
    private val credentialIssuer = "issuanceEndpoint"

    init {
        every { picContract.input.attestations } returns attestations
        every { picContract.display.card.issuedBy } returns issuedBy
        every { picContract.input.issuer } returns issuer
        request = IssuanceRequest(picContract, "testContractUrl")
        every { request.contract.input.credentialIssuer } returns credentialIssuer
        response = IssuanceResponse(request)
    }

    @Test
    fun `test add and get card`() {
        val suppliedPortableIdentityCard1: PortableIdentityCard = mockk()
        val suppliedCardType1 = "testCard1"
        response.addVerifiablePresentationContext(suppliedPortableIdentityCard1, "testCard1")
        val suppliedPortableIdentityCard2: PortableIdentityCard = mockk()
        val suppliedCardType2 = "testCard2"
        response.addVerifiablePresentationContext(suppliedPortableIdentityCard2, suppliedCardType2)
        val actualCollectedCards = response.getVerifiablePresentationContexts()
        val expectedCardCount = 2
        assertThat(actualCollectedCards).isNotNull
        assertThat(actualCollectedCards?.size).isEqualTo(expectedCardCount)
        assertThat(actualCollectedCards?.get(suppliedCardType2)).isEqualTo(suppliedPortableIdentityCard2)
        assertThat(actualCollectedCards?.get(suppliedCardType1)).isEqualTo(suppliedPortableIdentityCard1)
    }

    @Test
    fun `test add and get Id Tokens`() {
        val suppliedIdToken = "testIdToken"
        val suppliedIdTokenConfiguration = "testIdTokenConfig"
        response.addIdTokenContext(suppliedIdTokenConfiguration, suppliedIdToken)
        val actualCollectedTokens = response.getIdTokenContexts()
        val expectedTokenCount = 1
        assertThat(actualCollectedTokens).isNotNull
        assertThat(actualCollectedTokens?.size).isEqualTo(expectedTokenCount)
        assertThat(actualCollectedTokens?.get(suppliedIdTokenConfiguration)).isEqualTo(suppliedIdToken)
    }

    @Test
    fun `test add and get Self Issued Claims`() {
        val suppliedSelfIssuedClaim = "testSelfIssuedClaim"
        val suppliedSelfIssuedClaimField = "testSelfIssuedClaimField"
        response.addSelfAttestedClaimContext(suppliedSelfIssuedClaimField, suppliedSelfIssuedClaim)
        val actualSelfIssuedClaims = response.getSelfAttestedClaimContexts()
        val expectedSelfIssuedClaimCount = 1
        assertThat(actualSelfIssuedClaims).isNotNull
        assertThat(actualSelfIssuedClaims?.size).isEqualTo(expectedSelfIssuedClaimCount)
        assertThat(actualSelfIssuedClaims?.get(suppliedSelfIssuedClaimField)).isEqualTo(suppliedSelfIssuedClaim)
    }

    @Test
    fun `test create receipt by adding empty card id`() {
        val piCard: PortableIdentityCard = mockk()
        val receiptCreationStartTime = System.currentTimeMillis()
        response.addVerifiablePresentationContext(piCard, "testCard1")
        val cardId = ""
        every { piCard.cardId } returns cardId
        val receipts = response.createReceiptsForPresentedVerifiableCredentials(entityDid, entityName)
        val expectedReceiptCount = 1
        assertThat(receipts.size).isEqualTo(expectedReceiptCount)
        val receipt = receipts.first()
        assertThat(receipt.cardId).isEqualTo("")
        assertThat(receipt.entityName).isEqualTo(entityName)
        assertThat(receipt.entityIdentifier).isEqualTo(entityDid)
        assertThat(receipt.action).isEqualTo(ReceiptAction.Presentation)
        assertThat(receipt.activityDate).isGreaterThanOrEqualTo(receiptCreationStartTime)
    }

    @Test
    fun `test create receipt by adding 1 card`() {
        val piCard: PortableIdentityCard = mockk()
        val receiptCreationStartTime = System.currentTimeMillis()
        response.addVerifiablePresentationContext(piCard, "testCard1")
        val cardId = "testCardId"
        every { piCard.cardId } returns cardId
        val receipts = response.createReceiptsForPresentedVerifiableCredentials(entityDid, entityName)
        val expectedReceiptCount = 1
        assertThat(receipts.size).isEqualTo(expectedReceiptCount)
        val receipt = receipts.first()
        assertThat(receipt.cardId).isEqualTo(cardId)
        assertThat(receipt.entityName).isEqualTo(entityName)
        assertThat(receipt.entityIdentifier).isEqualTo(entityDid)
        assertThat(receipt.action).isEqualTo(ReceiptAction.Presentation)
        assertThat(receipt.activityDate).isGreaterThanOrEqualTo(receiptCreationStartTime)
    }

    @Test
    fun `test create receipt without adding card`() {
        val receipts = response.createReceiptsForPresentedVerifiableCredentials(entityDid, entityName)
        val expectedReceiptCount = 0
        assertThat(receipts.size).isEqualTo(expectedReceiptCount)
    }

    @Test
    fun `test create receipt by adding multiple cards with same type`() {
        val piCard1: PortableIdentityCard = mockk()
        response.addVerifiablePresentationContext(piCard1, "testCard1")
        val cardId1 = "testCardId1"
        every { piCard1.cardId } returns cardId1
        val piCard2: PortableIdentityCard = mockk()
        response.addVerifiablePresentationContext(piCard2, "testCard1")
        val cardId2 = "testCardId2"
        every { piCard2.cardId } returns cardId2
        val receiptCreationStartTime = System.currentTimeMillis()
        val receipts = response.createReceiptsForPresentedVerifiableCredentials(entityDid, entityName)
        val expectedReceiptCount = 1
        assertThat(receipts.size).isEqualTo(expectedReceiptCount)
        val receipt = receipts.first()
        assertThat(receipt.cardId).isEqualTo(cardId2)
        assertThat(receipt.entityName).isEqualTo(entityName)
        assertThat(receipt.entityIdentifier).isEqualTo(entityDid)
        assertThat(receipt.action).isEqualTo(ReceiptAction.Presentation)
        assertThat(receipt.activityDate).isGreaterThanOrEqualTo(receiptCreationStartTime)
    }

    @Test
    fun `test create receipt by adding multiple cards with different types`() {
        val piCard1: PortableIdentityCard = mockk()
        response.addVerifiablePresentationContext(piCard1, "testCard1")
        val cardId1 = "testCardId1"
        every { piCard1.cardId } returns cardId1
        val piCard2: PortableIdentityCard = mockk()
        response.addVerifiablePresentationContext(piCard2, "testCard2")
        val cardId2 = "testCardId2"
        every { piCard2.cardId } returns cardId2
        val receiptCreationStartTime = System.currentTimeMillis()
        val receipts = response.createReceiptsForPresentedVerifiableCredentials(entityDid, entityName)
        val expectedReceiptCount = 2
        assertThat(receipts.size).isEqualTo(expectedReceiptCount)
        val receipt1 = receipts.first()
        assertThat(receipt1.cardId).isEqualTo(cardId1)
        assertThat(receipt1.entityName).isEqualTo(entityName)
        assertThat(receipt1.entityIdentifier).isEqualTo(entityDid)
        assertThat(receipt1.action).isEqualTo(ReceiptAction.Presentation)
        val receipt2 = receipts.last()
        assertThat(receipt2.cardId).isEqualTo(cardId2)
        assertThat(receipt2.entityName).isEqualTo(entityName)
        assertThat(receipt2.entityIdentifier).isEqualTo(entityDid)
        assertThat(receipt2.action).isEqualTo(ReceiptAction.Presentation)
        assertThat(receipt1.activityDate).isLessThanOrEqualTo(receipt2.activityDate)
        assertThat(receipt1.activityDate).isGreaterThanOrEqualTo(receiptCreationStartTime)
    }

    @Test
    fun `test create receipt by adding empty entity information`() {
        val piCard: PortableIdentityCard = mockk()
        response.addVerifiablePresentationContext(piCard, "testCard1")
        val cardId = "testCardId"
        every { piCard.cardId } returns cardId
        val receiptCreationStartTime = System.currentTimeMillis()
        val receipts = response.createReceiptsForPresentedVerifiableCredentials("", "")
        val expectedReceiptCount = 1
        assertThat(receipts.size).isEqualTo(expectedReceiptCount)
        val receipt = receipts.first()
        assertThat(receipt.cardId).isEqualTo(cardId)
        assertThat(receipt.entityName).isEqualTo("")
        assertThat(receipt.entityIdentifier).isEqualTo("")
        assertThat(receipt.action).isEqualTo(ReceiptAction.Presentation)
        assertThat(receipt.activityDate).isGreaterThanOrEqualTo(receiptCreationStartTime)
    }
}