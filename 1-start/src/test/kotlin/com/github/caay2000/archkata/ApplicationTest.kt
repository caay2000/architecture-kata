package com.github.caay2000.archkata

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

class ApplicationTest {

    @Test
    fun `create user`() = testApplication {
        this.application { start() }

        val response = client.post("/user/example@test.com/myName")

        val expected = """ { "id": 1, "email": "example@test.com", "name": "myName" } """
        assertThat(response.status).isEqualTo(HttpStatusCode.Created)
        JSONAssert.assertEquals(response.bodyAsText(), expected, true)
    }

    @Test
    fun `create multiple users`() = testApplication {
        this.application { start() }

        val response1 = client.post("/user/example@test.com/myName")
        val response2 = client.post("/user/example2@test.com/anotherName")

        val expected1 = """ { "id": 1, "email": "example@test.com", "name": "myName" } """
        val expected2 = """ { "id": 2, "email": "example2@test.com", "name": "anotherName" } """
        assertThat(response1.status).isEqualTo(HttpStatusCode.Created)
        assertThat(response2.status).isEqualTo(HttpStatusCode.Created)
        JSONAssert.assertEquals(response1.bodyAsText(), expected1, true)
        JSONAssert.assertEquals(response2.bodyAsText(), expected2, true)
    }

    @Test
    fun `write post`() = testApplication {
        this.application { start() }

        client.post("/user/example@test.com/myName")
        val response = client.post("/write/1") {
            setBody("Hello, how are you?")
        }

        val responseBody = response.bodyAsText()
        val date = JSONObject(responseBody).getString("date")
        val expected = """ { "id": 1, "user": "myName", "userId": 1, "message": "Hello, how are you?", "date": "$date" } """
        assertThat(response.status).isEqualTo(HttpStatusCode.Created)
        JSONAssert.assertEquals(responseBody, expected, true)
    }

    @Test
    fun `view user`() = testApplication {
        this.application { start() }

        client.post("/user/example@test.com/myName")
        client.post("/write/1") { setBody("Hello, how are you?") }
        val response = client.get("/user/1")

        val responseBody = response.bodyAsText()
        val date = JSONObject(responseBody).getJSONArray("messages").getJSONObject(0).getString("date")
        val expected = """
            {
                "id": 1,
                "email": "example@test.com",
                "name": "myName",
                "messages": [
                    {
                        "date": "$date",
                        "id": 1,
                        "message": "Hello, how are you?",
                        "user": "myName",
                        "userId": 1
                    }
                ],

                "follows": []
            }
        """.trimMargin()
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        JSONAssert.assertEquals(responseBody, expected, true)
    }

    @Test
    fun `follow user`() = testApplication {
        this.application { start() }

        client.post("/user/example@test.com/myName")
        client.post("/user/example2@test.com/anotherName")
        val response = client.post("/follow/1/2")

        assertThat(response.status).isEqualTo(HttpStatusCode.NoContent)
        assertThat(response.bodyAsText()).isEmpty()
    }

    @Test
    fun `view user following another one`() = testApplication {
        this.application { start() }

        client.post("/user/example@test.com/myName")
        client.post("/write/1") { setBody("Hello, how are you?") }
        client.post("/user/example2@test.com/anotherName")
        client.post("/follow/1/2")
        val response = client.get("/user/1")

        val responseBody = response.bodyAsText()
        val date = JSONObject(responseBody).getJSONArray("messages").getJSONObject(0).getString("date")
        val expected = """
            {
                "id": 1,
                "email": "example@test.com",
                "name": "myName",
                "messages": [
                    {
                        "date": "$date",
                        "id": 1,
                        "message": "Hello, how are you?",
                        "user": "myName",
                        "userId": 1
                    }
                ],
                "follows": [
                    {
                        "id": "2",
                        "name": "anotherName"
                    }
                ]
            }
        """.trimMargin()
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        JSONAssert.assertEquals(responseBody, expected, true)
    }

    @Test
    fun `view user timeline`() = testApplication {
        this.application { start() }

        client.post("/user/example@test.com/myName")
        client.post("/write/1") { setBody("Hello, how are you?") }
        client.post("/user/example2@test.com/anotherName")
        client.post("/follow/1/2")
        client.post("/write/2") { setBody("I'm fine thanks!") }
        val response = client.get("/user/timeline/1")

        val responseBody = response.bodyAsText()
        val dateMsg2 = JSONObject(responseBody).getJSONArray("messages").getJSONObject(0).getString("date")
        val dateMsg1 = JSONObject(responseBody).getJSONArray("messages").getJSONObject(1).getString("date")
        val expected = """
            {
                "id": 1,
                "email": "example@test.com",
                "name": "myName",
                "messages": [
                    {
                        "date": "$dateMsg2",
                        "id": 2,
                        "message": "I'm fine thanks!",
                        "user": "anotherName",
                        "userId": 2
                    },
                    {
                        "date": "$dateMsg1",
                        "id": 1,
                        "message": "Hello, how are you?",
                        "user": "myName",
                        "userId": 1
                    }
                ]
            }
        """.trimMargin()
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        JSONAssert.assertEquals(responseBody, expected, true)
    }
}
