package com.github.caay2000.archkata.ex4

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert

class ApplicationTestEx4 {

    @Test
    fun `create user`() = testApplication {
        this.application { cleanDatabase() }

        val response = client.post("/user/example@test.com/myName")

        val responseBody = response.bodyAsText()
        val id = JSONObject(responseBody).getString("id")
        val expected = """ { "id": "$id", "email": "example@test.com", "name": "myName", "messages" : [], "follows": [] } """
        assertThat(response.status).isEqualTo(HttpStatusCode.Created)
        JSONAssert.assertEquals(expected, responseBody, true)
    }

    @Test
    fun `create multiple users`() = testApplication {
        this.application { cleanDatabase() }

        val response1 = client.post("/user/example@test.com/myName")
        val response2 = client.post("/user/example2@test.com/anotherName")

        val responseBody1 = response1.bodyAsText()
        val responseBody2 = response2.bodyAsText()
        val id1 = JSONObject(responseBody1).getString("id")
        val id2 = JSONObject(responseBody2).getString("id")

        val expected1 = """ { "id": "$id1", "email": "example@test.com", "name": "myName", "messages" : [], "follows": []  } """
        val expected2 = """ { "id": "$id2", "email": "example2@test.com", "name": "anotherName", "messages" : [], "follows": []  } """
        assertThat(response1.status).isEqualTo(HttpStatusCode.Created)
        assertThat(response2.status).isEqualTo(HttpStatusCode.Created)
        JSONAssert.assertEquals(expected1, response1.bodyAsText(), true)
        JSONAssert.assertEquals(expected2, response2.bodyAsText(), true)
    }

    @Test
    fun `write post`() = testApplication {
        this.application { cleanDatabase() }

        val createResponse = client.post("/user/example@test.com/myName")
        val userId = JSONObject(createResponse.bodyAsText()).getString("id")
        val response = client.post("/write/$userId") {
            setBody("Hello, how are you?")
        }

        val responseBody = response.bodyAsText()
        val msgId = JSONObject(responseBody).getString("id")
        val date = JSONObject(responseBody).getString("date")
        val expected = """ { "id": "$msgId", "user": "myName", "userId": "$userId", "message": "Hello, how are you?", "date": "$date" } """
        assertThat(response.status).isEqualTo(HttpStatusCode.Created)
        JSONAssert.assertEquals(expected, responseBody, true)
    }

    @Test
    fun `view user`() = testApplication {
        this.application { cleanDatabase() }

        val createResponse = client.post("/user/example@test.com/myName")
        val userId = JSONObject(createResponse.bodyAsText()).getString("id")
        val publishResponse = client.post("/write/$userId") { setBody("Hello, how are you?") }
        val msgId = JSONObject(publishResponse.bodyAsText()).getString("id")
        val date = JSONObject(publishResponse.bodyAsText()).getString("date")

        val response = client.get("/user/$userId")

        val responseBody = response.bodyAsText()
        val expected = """
            {
                "id": "$userId",
                "email": "example@test.com",
                "name": "myName",
                "messages": [
                    {
                        "id": "$msgId",
                        "userId": "$userId",
                        "user": "myName",
                        "message": "Hello, how are you?",
                        "date": "$date"
                    }
                ],
                "follows": []
            }
        """.trimMargin()
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        JSONAssert.assertEquals(expected, responseBody, true)
    }

    @Test
    fun `follow user`() = testApplication {
        this.application { cleanDatabase() }

        val createResponse1 = client.post("/user/example@test.com/myName")
        val userId1 = JSONObject(createResponse1.bodyAsText()).getString("id")
        val createResponse2 = client.post("/user/example2@test.com/anotherName")
        val userId2 = JSONObject(createResponse2.bodyAsText()).getString("id")
        val response = client.post("/follow/$userId1/$userId2")

        assertThat(response.status).isEqualTo(HttpStatusCode.NoContent)
        assertThat(response.bodyAsText()).isEmpty()
    }

    @Test
    fun `view user following another one`() = testApplication {
        this.application { cleanDatabase() }

        val createResponse1 = client.post("/user/example@test.com/myName")
        val userId1 = JSONObject(createResponse1.bodyAsText()).getString("id")

        val publishResponse = client.post("/write/$userId1") { setBody("Hello, how are you?") }
        val msgId = JSONObject(publishResponse.bodyAsText()).getString("id")

        val createResponse2 = client.post("/user/example2@test.com/anotherName")
        val userId2 = JSONObject(createResponse2.bodyAsText()).getString("id")

        client.post("/follow/$userId1/$userId2")
        val response = client.get("/user/$userId1")

        val responseBody = response.bodyAsText()
        val date = JSONObject(responseBody).getJSONArray("messages").getJSONObject(0).getString("date")
        val expected = """
            {
                "id": "$userId1",
                "email": "example@test.com",
                "name": "myName",
                "messages": [
                    {
                        "id": "$msgId",
                        "userId": "$userId1",
                        "user": "myName",
                        "message": "Hello, how are you?",
                        "date": "$date"
                    }
                ],
                "follows": [
                    {
                        "id": "$userId2",
                        "name": "anotherName"
                    }
                ]
            }
        """.trimMargin()
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        JSONAssert.assertEquals(expected, responseBody, true)
    }

    @Test
    fun `view user timeline`() = testApplication {
        this.application { cleanDatabase() }

        val createResponse1 = client.post("/user/example@test.com/myName")
        val userId1 = JSONObject(createResponse1.bodyAsText()).getString("id")

        val publishResponse1 = client.post("/write/$userId1") { setBody("Hello, how are you?") }
        val msgId1 = JSONObject(publishResponse1.bodyAsText()).getString("id")
        val dateMsg1 = JSONObject(publishResponse1.bodyAsText()).getString("date")

        val createResponse2 = client.post("/user/example2@test.com/anotherName")
        val userId2 = JSONObject(createResponse2.bodyAsText()).getString("id")

        client.post("/follow/$userId1/$userId2")

        val publishResponse2 = client.post("/write/$userId2") { setBody("I'm fine thanks!") }
        val msgId2 = JSONObject(publishResponse2.bodyAsText()).getString("id")
        val dateMsg2 = JSONObject(publishResponse2.bodyAsText()).getString("date")

        val response = client.get("/user/timeline/$userId1")

        val responseBody = response.bodyAsText()
        val expected = """
            {
                "id": "$userId1",
                "email": "example@test.com",
                "name": "myName",
                "messages": [
                    {
                        "id": "$msgId2",
                        "userId": "$userId2",
                        "user": "anotherName",
                        "message": "I'm fine thanks!",
                        "date": "$dateMsg2"
                    },
                    {
                        "id": "$msgId1",
                        "userId": "$userId1",
                        "user": "myName",
                        "message": "Hello, how are you?",
                        "date": "$dateMsg1"
                    }
                ]
            }
        """.trimMargin()
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        JSONAssert.assertEquals(expected, responseBody, true)
    }
}
