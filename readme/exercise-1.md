## Exercise 1 - Start

We want to create a new social network, but our development skills are limiting us! We have something, but seems that it won't be enough to
replace the actual social networks in place!

We have shown our code to a friend with some years of coding experience, and he has been horrified! `"At least, we have tests!"` he said!

Take a look at our code, and make your own mind!

<kbd> <img src="exercise_1_header.png" /> </kbd>

[Home](../README.md) | [Exercise 2 - Continue](exercise-2.md)

## Summary

Our initial idea, was a revolutionary concept of publishing public messages! Then, whoever wants to follow all your publications, can just
follow you, and your messages we'll be published in his timeline for easy access!

We wanted to be able to create/edit/delete users, publish/edit/delete messages, like other user messages, view other users and it's
messages (and the messages they liked from others), follow/unfollow other users and view our own timeline, with our messages and all the
messages of the people we follow and the ones they liked!

But... we just have a minimum set of actions... our knowledge is limited...

We can create users, publish messages, view other users with their messages, follow other users and view our timeline with the messages of
the people we follow.

## Exercise

Try to implement everything that is missing in our application... as you'll see, it will be complicated!

What problems will you face just for adding the missing features? And what about the performance, if the application reaches 100k users? or
100M users?

Let's move to the next exercise and start improving this code!

## HTTP Annex

- Create User `POST "/user/example@test.com/myName"`
  - Response `201 CREATED`
  - <details><summary>Response Body</summary>

    ```json
    {
      "id": 1,
      "email": "example@test.com",
      "name": "myName"
    }
    ```
    </details>

- Publish message `POST "/write/userId"`
  - Request Body `"Hello, how are you?"`
  - Response `201 CREATED`
  - <details><summary>Response Body</summary>

    ```json
    {
      "id": 1,
      "user": "myName",
      "userId": 1,
      "message": "Hello, how are you?",
      "date": "$date"
    }
    ```
    </details>

- Follow User `POST "/follow/{userId}/{followUserId}"` `201 CREATED`
    - Response `204 NO CONTENT`

- View User `GET "/user/{userId}"`
    - Response `200 OK`
    - <details><summary>Response Body</summary>

      ```json
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
      ```
    </details>

- User Timeline `GET "/user/timeline/1"`
  - Response `200 OK`
  - <details><summary>Response Body</summary>
  
    ```json
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
    ```
    </details>
