# Installation

Dependencies are hosted on Maven Central and compiled builds can be found in releases.

**Mal4J requires at least Java 8.**

## Maven Central

Compiled binaries can be found on Maven Central → [![Maven Central](https://img.shields.io/maven-central/v/com.kttdevelopment/mal4j)](https://mvnrepository.com/artifact/com.kttdevelopment/mal4j)

## Local

For projects built locally, jars can be found in releases → [![Releases](https://img.shields.io/github/v/release/KatsuteDev/Mal4J)](https://github.com/KatsuteDev/Mal4J/releases)

Mal4J is a standalone library and requires no additional dependencies.

# API setup

The below method is massively simplified, for a more detailed explanation on OAuth2.0 works check this blog post: [OAuth2.0 authorization for MAL](https://myanimelist.net/blog.php?eid=835707).

If you want to quickly generate a token you can use this [python script](https://gitlab.com/-/snippets/2039434).

## 1. Create new Client ID

  - In order to use the MyAnimeList API you must retrieve your own client ID.

    You can create a new Client ID at [https://myanimelist.net/apiconfig](https://myanimelist.net/apiconfig).

    ![Create ID](https://raw.githubusercontent.com/KatsuteDev/Mal4J/main/setup_1.png)

## 2. Register application

  - Fill in all required fields and **App Redirect URL**, this is what we use to retrieve the authorization code.

    For users using local server authentication ([below](#authenticate-with-client-id-using-a-local-server)) set this to `http://localhost:5050` or whatever port you are using (port must be between `1` - `65535`).

    ![Register application](https://raw.githubusercontent.com/KatsuteDev/Mal4J/main/setup_2.png)

## 3. Retrieve Client ID

  - Copy **client ID** and **client secret** (if the application has a client secret), this will be used to generate an authorization code.

    ![Copy client id and client secret](https://raw.githubusercontent.com/KatsuteDev/Mal4J/main/setup_3.png)

## 4. Authentication

This library has a simplified authentication method based of the MyAnimeList [authorization documentation](https://myanimelist.net/apiconfig/references/authorization#client-registration). You can implement your own authentication methods or use the ones provided here.

This library also supports OAuth token refresh.

It is suggested that you save the OAuth token that is generated so you don't have to authenticate with MyAnimeList each time.

### Authenticate using token

  - For developers using their own [authorization](https://myanimelist.net/apiconfig/references/authorization#client-registration) you can simply use the OAuth token that you generate.

    ```java
    MyAnimeList mal = MyAnimeList.withOAuthToken("oauth_token");
    ```

### Authenticate with client id using OAuth 2.0

For developers using their own [authorization](https://myanimelist.net/apiconfig/references/authorization#step-1-generate-a-code-verifier-and-challenge) methods you can use the [`MyAnimeListAuthenticator`](https://docs.katsute.dev/mal4j/javadoc/Mal4J/com/kttdevelopment/mal4j/MyAnimeListAuthenticator.html) to generate an OAuth token from a client id and PKCE code challenge.

  - The URL to obtain the authorization code can be generated using [`MyAnimeListAuthenticator#getAuthorizationURL(String,String)`](https://docs.katsute.dev/mal4j/javadoc/Mal4J/com/kttdevelopment/mal4j/MyAnimeListAuthenticator.html#getAuthorizationURL(java.lang.String,java.lang.String)).

    The client secret will be `null` if your application does not have one.

    This url is **not** your code.

  - After you authenticate with the above URL you will be redirected to the page you set in [step 2](#2-register-application). The URL that you are redirected to contains your **authorization code**.

    Example: `http://localhost:5050?code=AUTHORIZATION_CODE`

    Typically this is where you would have a local server to process the request and retrieve the code from the query.

    ```java
    String authorization_url = MyAnimeListAuthenticator.getAuthorizationURL("client_id", "PKCE_code_challenge");

    MyAnimeList mal = MyAnimeList.withAuthorization(new MyAnimeListAuthenticator("client_id", "client_secret", "authorization_code", "PKCE_code_challenge"));
    ```

### Authenticate with client id using a local server

  - For developers without domain for the app redirect url (using *localhost*), authorization can be completed using the [`MyAnimeListAuthenticator`](https://docs.katsute.dev/mal4j/javadoc/Mal4J/com/kttdevelopment/mal4j/MyAnimeListAuthenticator.html).

    The app redirect url should be `http://localhost:5050` or whatever port you set it as in [step 2](#2-register-application).

    The client secret will be `null` if your application does not have one.

  - When this method is run it will launch your web browser to authenticate with MyAnimeList and then return with the OAuth key.

  - If [`openBrowser()`](https://docs.katsute.dev/mal4j/javadoc/Mal4J/com/kttdevelopment/mal4j/MyAnimeListAuthenticator.LocalServerBuilder.html#openBrowser()) is not supported then you can use [`setURLCallback(Consumer<String>)`](https://docs.katsute.dev/mal4j/javadoc/Mal4J/com/kttdevelopment/mal4j/MyAnimeListAuthenticator.LocalServerBuilder.html#setURLCallback(java.util.function.Consumer)) to handle the generated URL. Refer to [OAuth2.0 authentication](#authenticate-with-client-id-using-oauth-20) for steps on how to generate a token from the authorization URL.

    ```java
    MyAnimeList mal = MyAnimeList.withAuthorization(new MyAnimeListAuthenticator.LocalServerBuilder("client_id", "client_secret", 5050).openBrowser().build());
    ```

<hr>

The above methods are massively simplified, for a more detailed guide on how OAuth2.0 works check this blog post: [OAuth2.0 authorization for MAL](https://myanimelist.net/blog.php?eid=835707).
