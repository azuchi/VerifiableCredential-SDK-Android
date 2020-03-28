package com.microsoft.portableIdentity.sdk.auth.requests

import org.junit.Test

class OidcRequestTest {

    val testParameters = mapOf("client_id" to listOf("https://didwebtest.azurewebsites.net/verify"))
    val serializedTestToken = "eyJhbGciOiJFUzI1NksiLCJraWQiOiJkaWQ6d2ViOmRpZHdlYnRlc3QuYXp1cmV3ZWJzaXRlcy5uZXQjd2ViTWV0aG9kS2V5IiwidHlwIjoiSldUIn0.eyJjbGllbnRfaWQiOiJodHRwczovL2RpZHdlYnRlc3QuYXp1cmV3ZWJzaXRlcy5uZXQvdmVyaWZ5IiwicmVzcG9uc2VfdHlwZSI6ImlkX3Rva2VuIiwibWF4X2FnZSI6IjMwMCIsImF0dGVzdGF0aW9ucyI6eyJwcmVzZW50YXRpb25zIjpbeyJyZXF1aXJlZCI6dHJ1ZSwiY3JlZGVudGlhbFR5cGUiOiJYYm94TGl2ZUdhbWVyQ2FyZCIsImlzc3VlcnMiOlt7ImlzcyI6ImRpZDp3ZWI6ZGlkd2VidGVzdC5henVyZXdlYnNpdGVzLm5ldCJ9XSwiY29udHJhY3RzIjpbImh0dHBzOi8vcG9ydGFibGVpZGVudGl0eWNhcmRzLmF6dXJlLWFwaS5uZXQvNDJiMzlkOWQtMGNkZC00YWUwLWIyNTEtYjdiMzlhNTYxZjkxL2FwaS9wb3J0YWJsZS92MS4wL2NvbnRyYWN0cy94YmwiXX1dfSwiaXNzIjoiZGlkOndlYjpkaWR3ZWJ0ZXN0LmF6dXJld2Vic2l0ZXMubmV0IiwibmJmIjoxNTg1MzIzMDMwLCJleHAiOjE1ODUzMjMzMzB9.msaGCRXPwh_fneWqZGiA2kRUIjDyjIlmJknN6bylFcDFnB8-my_tTsnXXyniDRcIdY6XuHAQX09Z91ryUY46IA"

    @Test
    fun testRequest() {

//        runBlocking {
//            val httpResponse = HttpWrapper.get("https://portableidentitycards.azure-api.net/42b39d9d-0cdd-4ae0-b251-b7b39a561f91/api/portable/v1.0/contracts/xbl")
//            print(httpResponse)
//            val contract = Serializer.parse(Contract.serializer(), httpResponse)
//            print(contract)
//        }
    }
}

