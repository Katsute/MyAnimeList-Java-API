/*
 * Copyright (C) 2021 Ktt Development
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.kttdevelopment.mal4j;

import com.kttdevelopment.mal4j.APIStruct.Response;
import com.sun.net.httpserver.*;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.GZIPOutputStream;

import static com.kttdevelopment.mal4j.Json.*;

/**
 * <b>Documentation:</b> <a href="https://myanimelist.net/apiconfig/references/authorization">https://myanimelist.net/apiconfig/references/authorization</a> <br>
 * Authenticator used to retrieve OAuth2 tokens given a client id and client secret.
 *
 * @since 1.0.0
 * @version 1.1.0
 * @author Ktt Development
 */
public final class MyAnimeListAuthenticator {

    // required fields
    private static final String authUrl =
        "https://myanimelist.net/v1/oauth2/authorize" +
        "?response_type=code" +
        "&client_id=%s" +
        "&code_challenge=%s" +
        "&code_challenge_method=plain";

    // optional fields
    private static final String authState   = "&state=%s";
    private static final String redirectURI = "&redirect_uri=%s";

    private final MyAnimeListAuthenticationService authService = MyAnimeListAuthenticationService.create();

    @Deprecated
    private static final int DEFAULT_TIMEOUT = 60 * 3;
    @Deprecated
    private static final boolean DEFAULT_OPEN_BROWSER = false;

    @SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
    private final String client_id, client_secret, authorizationCode, pkce;
    private AccessToken token;

    /**
     * Creates a MyAnimeListAuthenticator and deploys a server to retrieve the OAuth2 token. Local server will be active until timeout.
     *
     * @deprecated use {@link LocalServerBuilder}
     * @param client_id client id
     * @param client_secret client secret (optional)
     * @param port port to run the retrieval server
     * @throws IllegalArgumentException if port was invalid, or client id or redirect URI was malformed
     * @throws BindException if port was blocked
     * @throws IOException if server could not be started
     * @throws UnauthorizedAccessException if request was intercepted by an unauthorized source
     * @throws NullPointerException if server was closed before the client could be authorized
     * @throws HttpException if client request failed
     *
     * @see #MyAnimeListAuthenticator(String, String, int, boolean)
     * @see #MyAnimeListAuthenticator(String, String, int, long)
     * @see #MyAnimeListAuthenticator(String, String, int, String)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean, String)
     * @see #MyAnimeListAuthenticator(String, String, int, long, String)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean, long, String)
     * @see MyAnimeList#withAuthorization(MyAnimeListAuthenticator)
     * @since 1.0.0
     */
    @Deprecated
    public MyAnimeListAuthenticator(final String client_id, final String client_secret, final int port) throws IOException{
        this(client_id, client_secret, port, DEFAULT_OPEN_BROWSER, DEFAULT_TIMEOUT, null);
    }

    /**
     * Creates a MyAnimeListAuthenticator and deploys a server to retrieve the OAuth2 token. Local server will be active until timeout.
     *
     * @deprecated use {@link LocalServerBuilder}
     * @param client_id client id
     * @param client_secret client secret (optional)
     * @param port port to run the retrieval server
     * @param openBrowser if the code should automatically open a browser window
     * @throws IllegalArgumentException if port was invalid, or client id or redirect URI was malformed
     * @throws BindException if port was blocked
     * @throws IOException if server could not be started
     * @throws UnauthorizedAccessException if request was intercepted by an unauthorized source
     * @throws NullPointerException if server was closed before the client could be authorized
     * @throws HttpException if client request failed
     *
     * @see #MyAnimeListAuthenticator(String, String, int)
     * @see #MyAnimeListAuthenticator(String, String, int, long)
     * @see #MyAnimeListAuthenticator(String, String, int, String)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean, String)
     * @see #MyAnimeListAuthenticator(String, String, int, long, String)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean, long, String)
     * @see MyAnimeList#withAuthorization(MyAnimeListAuthenticator)
     * @since 1.0.0
     */
    @Deprecated
    public MyAnimeListAuthenticator(final String client_id, final String client_secret, final int port, final boolean openBrowser) throws IOException{
        this(client_id, client_secret, port, openBrowser, DEFAULT_TIMEOUT, null);
    }

    /**
     * Creates a MyAnimeListAuthenticator and deploys a server to retrieve the OAuth2 token. Local server will be active until timeout.
     *
     * @deprecated use {@link LocalServerBuilder}
     * @param client_id client id
     * @param client_secret client secret (optional)
     * @param port port to run the retrieval server
     * @param timeout how long in seconds that the local authentication server will live for
     * @throws IllegalArgumentException if port was invalid, or client id or redirect URI was malformed
     * @throws BindException if port was blocked
     * @throws IOException if server could not be started
     * @throws UnauthorizedAccessException if request was intercepted by an unauthorized source
     * @throws NullPointerException if server was closed before the client could be authorized
     * @throws HttpException if client request failed
     *
     * @see #MyAnimeListAuthenticator(String, String, int)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean)
     * @see #MyAnimeListAuthenticator(String, String, int, String)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean, String)
     * @see #MyAnimeListAuthenticator(String, String, int, long, String)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean, long, String)
     * @see MyAnimeList#withAuthorization(MyAnimeListAuthenticator)
     * @since 1.0.0
     */
    @Deprecated
    public MyAnimeListAuthenticator(final String client_id, final String client_secret, final int port, final long timeout) throws IOException{
        this(client_id, client_secret, port, DEFAULT_OPEN_BROWSER, timeout, null);
    }

    /**
     * Creates a MyAnimeListAuthenticator and deploys a server to retrieve the OAuth2 token. Local server will be active until timeout.
     *
     * @deprecated use {@link LocalServerBuilder}
     * @param client_id client id
     * @param client_secret client secret (optional)
     * @param port port to run the retrieval server
     * @throws IllegalArgumentException if port was invalid, or client id or redirect URI was malformed
     * @throws BindException if port was blocked
     * @throws IOException if server could not be started
     * @throws UnauthorizedAccessException if request was intercepted by an unauthorized source
     * @throws NullPointerException if server was closed before the client could be authorized
     * @throws HttpException if client request failed
     *
     * @see #MyAnimeListAuthenticator(String, String, int)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean)
     * @see #MyAnimeListAuthenticator(String, String, int, long)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean, String)
     * @see #MyAnimeListAuthenticator(String, String, int, long, String)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean, long, String)
     * @see MyAnimeList#withAuthorization(MyAnimeListAuthenticator)
     * @since 1.0.0
     */
    @Deprecated
    public MyAnimeListAuthenticator(final String client_id, final String client_secret, final int port, final String redirectURI) throws IOException{
        this(client_id, client_secret, port, DEFAULT_OPEN_BROWSER, DEFAULT_TIMEOUT, redirectURI);
    }

    /**
     * Creates a MyAnimeListAuthenticator and deploys a server to retrieve the OAuth2 token. Local server will be active until timeout.
     *
     * @deprecated use {@link LocalServerBuilder}
     * @param client_id client id
     * @param client_secret client secret (optional)
     * @param port port to run the retrieval server
     * @param openBrowser if the code should automatically open a browser window
     * @throws IllegalArgumentException if port was invalid, or client id or redirect URI was malformed
     * @throws BindException if port was blocked
     * @throws IOException if server could not be started
     * @throws UnauthorizedAccessException if request was intercepted by an unauthorized source
     * @throws NullPointerException if server was closed before the client could be authorized
     * @throws HttpException if client request failed
     *
     * @see #MyAnimeListAuthenticator(String, String, int)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean)
     * @see #MyAnimeListAuthenticator(String, String, int, long)
     * @see #MyAnimeListAuthenticator(String, String, int, String)
     * @see #MyAnimeListAuthenticator(String, String, int, long, String)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean, long, String)
     * @see MyAnimeList#withAuthorization(MyAnimeListAuthenticator)
     * @since 1.0.0
     */
    @Deprecated
    public MyAnimeListAuthenticator(final String client_id, final String client_secret, final int port, final boolean openBrowser, final String redirectURI) throws IOException{
        this(client_id, client_secret, port, openBrowser, DEFAULT_TIMEOUT, redirectURI);
    }

    /**
     * Creates a MyAnimeListAuthenticator and deploys a server to retrieve the OAuth2 token. Local server will be active until timeout.
     *
     * @deprecated use {@link LocalServerBuilder}
     * @param client_id client id
     * @param client_secret client secret (optional)
     * @param port port to run the retrieval server
     * @param timeout how long in seconds that the local authentication server will live for
     * @throws IllegalArgumentException if port was invalid, or client id or redirect URI was malformed
     * @throws BindException if port was blocked
     * @throws IOException if server could not be started
     * @throws UnauthorizedAccessException if request was intercepted by an unauthorized source
     * @throws NullPointerException if server was closed before the client could be authorized
     * @throws HttpException if client request failed
     *
     * @see #MyAnimeListAuthenticator(String, String, int)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean)
     * @see #MyAnimeListAuthenticator(String, String, int, long)
     * @see #MyAnimeListAuthenticator(String, String, int, String)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean, String)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean, long, String)
     * @see MyAnimeList#withAuthorization(MyAnimeListAuthenticator)
     * @since 1.0.0
     */
    @Deprecated
    public MyAnimeListAuthenticator(final String client_id, final String client_secret, final int port, final long timeout, final String redirectURI) throws IOException{
        this(client_id, client_secret, port, DEFAULT_OPEN_BROWSER, timeout, redirectURI);
    }

    /**
     * Creates a MyAnimeListAuthenticator and deploys a server to retrieve the OAuth2 token. Local server will be active until timeout.
     *
     * @deprecated use {@link LocalServerBuilder}
     * @param client_id client id
     * @param client_secret client secret (optional)
     * @param port port to run the retrieval server
     * @param openBrowser if the code should automatically open a browser window
     * @param timeout how long in seconds that the local authentication server will live for
     * @param redirect_URI redirect URI to use
     * @throws IllegalArgumentException if port was invalid, or client id or redirect URI was malformed
     * @throws BindException if port was blocked
     * @throws IOException if server could not be started
     * @throws UnauthorizedAccessException if request was intercepted by an unauthorized source
     * @throws NullPointerException if server was closed before the client could be authorized
     * @throws HttpException if client request failed
     *
     * @see #MyAnimeListAuthenticator(String, String, int)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean)
     * @see #MyAnimeListAuthenticator(String, String, int, long)
     * @see #MyAnimeListAuthenticator(String, String, int, String)
     * @see #MyAnimeListAuthenticator(String, String, int, boolean, String)
     * @see #MyAnimeListAuthenticator(String, String, int, long, String)
     * @see MyAnimeList#withAuthorization(MyAnimeListAuthenticator)
     * @since 1.0.0
     */
    @Deprecated
    public MyAnimeListAuthenticator(final String client_id, final String client_secret, final int port, final boolean openBrowser, final long timeout, final String redirect_URI) throws IOException{
        this(client_id, client_secret, port, null, openBrowser, timeout, redirect_URI);
        System.out.println("The MyAnimeListAuthenticator server constructor is deprecated and likely to be removed in the future. Please use MyAnimeListAuthenticator.LocalServerBuilder for local servers instead.");
    }

    @SuppressWarnings("SpellCheckingInspection")
    private MyAnimeListAuthenticator(
        final String client_id,
        final String client_secret,
        final int port,
        final AuthResponseHandler handler,
        final boolean openBrowser,
        final long timeout,
        final String redirect_URI
    ) throws IOException{
        // [authorization, PKCE verify]
        final String[] auth = authenticateWithLocalServer(
            client_id,
            port,
            handler,
            openBrowser,
            timeout,
            redirect_URI
        );

        this.client_id          = client_id;
        this.client_secret      = client_secret;
        this.authorizationCode  = auth[0];
        this.pkce               = auth[1];

        token = parseToken(authService
            .getToken(
                client_id,
                client_secret,
                "authorization_code",
                authorizationCode,
                pkce
            )
        );
    }

    /**
     * Creates a MyAnimeListAuthenticator.
     *
     * @param client_id client id
     * @param client_secret client secret, null if application has none
     * @param authorization_code authorization code
     * @param PKCE_code_challenge PKCE code challenge used to obtain authorization code. Must be between 43 and 128 characters.
     * @throws HttpException if request failed
     * @throws UncheckedIOException if client failed to execute request
     *
     * @see MyAnimeList#withAuthorization(MyAnimeListAuthenticator)
     * @since 1.0.0
     */
    @SuppressWarnings("SpellCheckingInspection")
    public MyAnimeListAuthenticator(final String client_id, final String client_secret, final String authorization_code, final String PKCE_code_challenge){
        Objects.requireNonNull(client_id, "Client ID must not be null");
        Objects.requireNonNull(authorization_code, "Authorization code must not be null");
        Objects.requireNonNull(PKCE_code_challenge, "PKCE code challenge must not be null");
        if(PKCE_code_challenge.length() < 43 || PKCE_code_challenge.length() > 128)
            throw new IllegalArgumentException("PKCE code challenge must be between 43 and 128 characters");

        this.client_id          = client_id;
        this.client_secret      = client_secret;
        this.authorizationCode  = authorization_code;
        this.pkce               = PKCE_code_challenge;

        token = parseToken(authService
            .getToken(
                client_id,
                client_secret,
                "authorization_code",
                authorization_code,
                PKCE_code_challenge
            )
        );
    }

// access token

    /**
     * Returns the access token.
     *
     * @return access token
     *
     * @see AccessToken
     * @since 1.0.0
     */
    public final AccessToken getAccessToken(){
        return token;
    }

    /**
     * Refreshes the access token.
     *
     * @return updated access token
     * @throws HttpException if request failed
     * @throws UncheckedIOException if client failed to execute request
     *
     * @see AccessToken
     * @since 1.0.0
     */
    public final AccessToken refreshAccessToken(){
        return token = parseToken(authService
            .refreshToken(
                client_id,
                client_secret,
                "refresh_token",
                authorizationCode,
                pkce,
                token.getRefreshToken()
            )
        );
    }

// URL

    /**
     * Returns the authorization URL for a client id.
     *
     * @param client_id client id. <i>required</i>
     * @param PKCE_code_challenge PKCE code challenge. <i>required</i>
     * @return authorization URL
     *
     * @see #getAuthorizationURL(String, String, String)
     * @see #getAuthorizationURL(String, String, String, String)
     * @since 1.0.0
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static String getAuthorizationURL(final String client_id, final String PKCE_code_challenge){
        return getAuthorizationURL(client_id, PKCE_code_challenge, null, null);
    }

    /**
     * Returns the authorization URL for a client id.
     *
     * @param client_id client id. <i>required</i>
     * @param PKCE_code_challenge PKCE code challenge. <i>required</i>
     * @param redirect_URI preregistered URI, only needed if you want to select a specific application redirect URI. <i>optional</i>
     * @return authorization URL
     *
     * @see #getAuthorizationURL(String, String)
     * @see #getAuthorizationURL(String, String, String, String)
     * @since 1.0.0
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static String getAuthorizationURL(final String client_id, final String PKCE_code_challenge, final String redirect_URI){
        return getAuthorizationURL(client_id, PKCE_code_challenge, redirect_URI, null);
    }

    /**
     * Returns the authorization URL for a client id.
     *
     * @param client_id client id. <i>required</i>
     * @param PKCE_code_challenge PKCE code challenge. <i>required</i>
     * @param redirect_URI preregistered URI, only needed if you want to select a specific application redirect URI. <i>optional</i>
     * @param state 0Auth2 state. <i>optional</i>
     * @return authorization URL
     *
     * @see #getAuthorizationURL(String, String)
     * @see #getAuthorizationURL(String, String, String)
     * @since 1.0.0
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static String getAuthorizationURL(final String client_id, final String PKCE_code_challenge, final String redirect_URI, final String state){
        return
            String.format(authUrl, client_id, PKCE_code_challenge) +
            (redirect_URI != null ? String.format(redirectURI, URLEncoder.encode(redirect_URI, StandardCharsets.UTF_8)) : "") +
            (state != null ? String.format(authState, state) : "");
    }

    /**
     * Creates a MyAnimeList Authenticator by deploying a local server to authenticate the user.
     *
     * @see MyAnimeListAuthenticator
     * @since 1.0.0
     * @version 1.0.0
     * @author Ktt Development
     */
    public static final class LocalServerBuilder {

        private final String client_id, client_secret;
        private final int port;

        private boolean openBrowser = false;
        private long timeout = 60 * 3;
        private String redirect_URI = null;

        private AuthResponseHandler responseHandler = null;

        /**
         * Instantiates a local server builder with a client id and port.
         *
         * @param client_id client id
         * @param port port
         *
         * @see #LocalServerBuilder(String, String, int)
         * @since 1.1.0
         */
        public LocalServerBuilder(final String client_id, final int port){
            this(client_id, null, port);
        }

        /**
         * Instantiates a local server builder with a client id, client secret, and port.
         *
         * @param client_id client id
         * @param client_secret client secret
         * @param port port
         *
         * @see #LocalServerBuilder(String, int)
         * @since 1.1.0
         */
        public LocalServerBuilder(final String client_id, final String client_secret, final int port){
            Objects.requireNonNull(client_id, "Client ID must not be null");

            this.client_id = client_id;
            this.client_secret = client_secret;
            this.port = port;
        }

        /**
         * Indicates that the authorization page should be opened in the user's browser automatically.
         *
         * @return builder
         *
         * @see #openBrowser(boolean)
         * @since 1.1.0
         */
        public final LocalServerBuilder openBrowser(){
            return openBrowser(true);
        }

        /**
         * If true, a browser will automatically be opened to the authorization page. Note that if this is false you will have to go to the page manually using {@link #getAuthorizationURL(String, String)}, {@link #getAuthorizationURL(String, String, String)}, or {@link #getAuthorizationURL(String, String, String, String)}.
         *
         * @param openBrowser if browser should be opened
         * @return builder
         *
         * @see #openBrowser()
         * @since 1.1.0
         */
        public final LocalServerBuilder openBrowser(final boolean openBrowser){
            this.openBrowser = openBrowser;
            return this;
        }

        /**
         * Sets how long (in seconds) that the server will expire in. Once the timeout has passed a new authentication builder must be used.
         *
         * @param timeout server timeout
         * @return builder
         *
         * @since 1.1.0
         */
        public final LocalServerBuilder setTimeout(final int timeout){
            this.timeout = timeout;
            return this;
        }

        /**
         * Sets the application redirect URI. Only required if your application has more than one redirect URI registered.
         *
         * @param redirectURI redirect URI
         * @return builder
         *
         * @since 1.1.0
         */
        public final LocalServerBuilder setRedirectURI(final String redirectURI){
            this.redirect_URI = redirectURI;
            return this;
        }

        /**
         * Sets the response handler.
         *
         * @param responseHandler response handler
         * @return builder
         *
         * @see AuthResponseHandler
         * @since 1.1.0
         */
        public final LocalServerBuilder setResponseHandler(final AuthResponseHandler responseHandler){
            this.responseHandler = responseHandler;
            return this;
        }

        /**
         * Returns the built authenticator.
         *
         * @return authenticator
         * @throws IllegalArgumentException if port was invalid, or client id or redirect URI was malformed
         * @throws BindException if port was blocked
         * @throws IOException if server could not be started
         * @throws UnauthorizedAccessException if request was intercepted by an unauthorized source
         * @throws NullPointerException if server was closed before the client could be authorized
         * @throws HttpException if client request failed
         *
         * @see MyAnimeListAuthenticator
         * @since 1.1.0
         */
        public final MyAnimeListAuthenticator build() throws IOException{
            return new MyAnimeListAuthenticator(
                client_id,
                client_secret,
                port,
                responseHandler,
                openBrowser,
                timeout,
                redirect_URI
            );
        }

        @Override
        public String toString(){
            return "LocalServerBuilder{" +
                   "port=" + port +
                   ", openBrowser=" + openBrowser +
                   ", timeout=" + timeout +
                   ", redirect_URI='" + redirect_URI + '\'' +
                   ", responseHandler=" + responseHandler +
                   '}';
        }

    }

// local server

    /**
     * Returns if the code is allowed to open the client browser.
     *
     * @return if code can open browser
     *
     * @since 1.0.0
     */
    public static boolean canOpenBrowser(){
        return Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE);
    }

    // [authorization, PKCE verify]
    @SuppressWarnings({"ResultOfMethodCallIgnored", "SpellCheckingInspection"})
    private static String[] authenticateWithLocalServer(
        final String client_id,
        final int port,
        final AuthResponseHandler responseHandler,
        final boolean openBrowser,
        final long timeout,
        final String redirectURI
    ) throws IOException{
        Objects.requireNonNull(client_id, "Client ID must not be null");

        final String verify = generatePKCECodeVerifier();
        final String state  = generateSha256(client_id + '&' + verify);
        final String url    = getAuthorizationURL(client_id, verify, redirectURI, state);

        // get auth call back from local server
        final ExecutorService exec = Executors.newSingleThreadExecutor();
        final CountDownLatch latch = new CountDownLatch(1);

        final HttpServer server    = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(exec);
        final AuthHandler handler = new AuthHandler(latch, responseHandler);
        server.createContext("/", handler);
        server.start();

        if(openBrowser)
            if(!canOpenBrowser())
                System.out.println("Desktop is not supported on this operating system. Please go to this URL manually: " + url);
            else
                try{ Desktop.getDesktop().browse(new URI(url));
                }catch(final URISyntaxException ignored){
                    exec.shutdownNow();
                    server.stop(0);
                    throw new IllegalArgumentException("URL syntax was invalid (most likely the client id or redirect URI wasn't encoded correctly)");
                }

        try{ latch.await(timeout, TimeUnit.SECONDS);
        }catch(final InterruptedException ignored){ } // soft failure
        exec.shutdownNow();
        server.stop(0);

        if(handler.getAuth() == null)
            throw new NullPointerException("Failed to authorize request (server was closed before a response could be received)");
        if(!state.equals(handler.getState()))
            throw new UnauthorizedAccessException("Failed to authorize request (packet was intercepted by an unauthorized source)");

        return new String[]{handler.getAuth(), verify};
    }

    private AccessToken parseToken(final Response<JsonObject> response){
        final JsonObject body = response.body();
        if(response.code() == HttpURLConnection.HTTP_OK)
            return new AccessToken(
                body.getString("token_type"),
                body.getLong("expires_in"),
                body.getString("access_token"),
                body.getString("refresh_token")
            );
        else
            throw new HttpException(response.URL(), response.code(), (body.getString("body") + ' ' + body.getString("error")).trim());

    }

    private static final class AuthHandler implements HttpHandler {

        private Map<String,String> parseWwwFormEnc(final String query){
            final Map<String,String> OUT = new HashMap<>();
            final String[] pairs = query.split("&");

            for(final String pair : pairs){
                if(pair.contains("=")){
                    final String[] kv = pair.split("=");
                    OUT.put(
                            URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                        kv.length == 2 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : null
                    );
                }
            }
            return OUT;
        }

        private final CountDownLatch latch;
        private final AuthResponseHandler handler;

        public AuthHandler(final CountDownLatch latch){
            this(latch, defaultHandler);
        }

        AuthHandler(final CountDownLatch latch, final AuthResponseHandler handler){
            this.latch = latch;
            this.handler = handler == null ? defaultHandler : handler;
        }

        private transient final AtomicReference<String> auth    = new AtomicReference<>();
        private transient final AtomicReference<String> state   = new AtomicReference<>();

        @Override
        public final void handle(final HttpExchange exchange) throws IOException{
            final Map<String,String> query = parseWwwFormEnc(exchange.getRequestURI().getRawQuery());
            state.set(query.get("state"));
            auth.set(query.get("code"));

            /* send */ {
                exchange.getResponseHeaders().set("Accept-Encoding", "gzip");
                exchange.getResponseHeaders().set("Content-Encoding", "gzip");
                exchange.getResponseHeaders().set("Connection", "keep-alive");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                try(GZIPOutputStream OUT = new GZIPOutputStream(exchange.getResponseBody())){
                    OUT.write(
                        handler.getResponse(
                            query.get("code"),
                            query.get("error"),
                            query.get("message"),
                            query.get("hint")
                        ).getBytes(StandardCharsets.UTF_8)
                    );
                    OUT.finish();
                    OUT.flush();
                }
            }

            latch.countDown();
        }

        public final String getAuth(){
            return auth.get();
        }

        public final String getState(){
            return state.get();
        }

    }

    private static final AuthResponseHandler defaultHandler = new AuthResponseHandler(){

        @SuppressWarnings("SpellCheckingInspection")
        private static final String HTML = "<!DOCTYPE html><html><head><title>MyAnimeList Authenticator</title><style>html,body{width:100%;height:100%;-webkit-user-select: none;-ms-user-select: none;user-select: none;}body{display:flex;align-items:center;justify-content:center;background-color:#2E51A2;margin:0px;*{width:100%}}*{font-family:Helvetica,Arial,sans-serif;color:white;text-align:center}</style></head><body><div><h1>Authentication {{ state }}</h1><p title=\"{{ hint }}\">{{ message }}</p></div></body></html>";

        private static final String OK   = "&#10004;&#65039;";
        private static final String FAIL = "&#10060;";

        @Override
        public final String getResponse(final String code, final String error, final String message, final String hint){
            final String pass = code == null ? "Failed " + FAIL : "Completed " + OK;
            final String err = error == null ? "" : error;
            final String msg = code == null
                ? "<b>" + err.substring(0, 1).toUpperCase() + err.substring(1).replace('_', ' ') + "</b>: " + (message == null ? "" : message)
                : "You may now close the window.";

            return HTML
                .replace("{{ state }}", pass)
                .replace("{{ hint }}", hint != null ? hint : "")
                .replace("{{ message }}", msg);
        }

    };

// generator methods

    private static String generatePKCECodeVerifier(){
        final SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifier = new byte[64];
        secureRandom.nextBytes(codeVerifier);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
    }

    private static String generateSha256(final String str){
        final MessageDigest digest;
        try{
            digest = MessageDigest.getInstance("SHA-256");
        }catch(final NoSuchAlgorithmException e){ // should NEVER occur
            throw new RuntimeException(e);
        }

        final byte[] encodedHash        = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        final StringBuilder hexString   = new StringBuilder(2 * encodedHash.length);
        for(final byte hash : encodedHash){
            final String hex = Integer.toHexString(0xff & hash);
            if(hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
