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
import com.kttdevelopment.mal4j.anime.*;
import com.kttdevelopment.mal4j.anime.property.AnimeRankingType;
import com.kttdevelopment.mal4j.anime.property.time.Season;
import com.kttdevelopment.mal4j.forum.*;
import com.kttdevelopment.mal4j.manga.*;
import com.kttdevelopment.mal4j.manga.property.MangaRankingType;
import com.kttdevelopment.mal4j.query.*;
import com.kttdevelopment.mal4j.user.User;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kttdevelopment.mal4j.Json.*;
import static com.kttdevelopment.mal4j.MyAnimeListAPIResponseMapping.Anime.*;
import static com.kttdevelopment.mal4j.MyAnimeListAPIResponseMapping.Forum.*;
import static com.kttdevelopment.mal4j.MyAnimeListAPIResponseMapping.Manga.*;
import static com.kttdevelopment.mal4j.MyAnimeListAPIResponseMapping.User.*;

/**
 * Implements the {@link MyAnimeList} interface with the {@link MyAnimeListService}.
 *
 * @see MyAnimeList
 * @see MyAnimeListService
 * @since 1.0.0
 * @version 1.0.0
 * @author Ktt Development
 */
final class MyAnimeListImpl extends MyAnimeList{

    private transient String auth;
    private MyAnimeListAuthenticator authenticator;

    private final MyAnimeListService service = MyAnimeListService.create();

    MyAnimeListImpl(final String auth){
        this.auth = auth;
    }

    MyAnimeListImpl(final MyAnimeListAuthenticator authenticator){
        this.authenticator = authenticator;
        this.auth = authenticator.getAccessToken().getToken();
    }

    @Override
    public synchronized final void refreshOAuthToken(){
        if(authenticator == null)
            throw new UnsupportedOperationException("OAuth token refresh can only be used with authorization");
        this.auth = authenticator.refreshAccessToken().getToken();
    }

    //

    @Override
    public final AnimeSearchQuery getAnime(){
        return new AnimeSearchQuery() {

            @Override
            public final List<Anime> search(){
                final JsonObject response = handleResponse(
                    () -> service.getAnime(
                        auth,
                        query,
                        limit,
                        offset,
                        asFieldList(toCommaSeparatedString(fields), ALL_ANIME_FIELDS),
                        nsfw
                    )
                );
                if(response == null) return null;

                final List<Anime> anime = new ArrayList<>();
                for(final JsonObject iterator : response.getJsonArray("data"))
                    anime.add(asAnime(MyAnimeListImpl.this, iterator.getJsonObject("node")));
                return anime;
            }

            @Override
            public final PaginatedIterator<Anime> searchAll(){
                return new PagedIterator<>(
                    offset,
                    offset -> service.getAnime(
                        auth,
                        query,
                        limit,
                        offset,
                        asFieldList(toCommaSeparatedString(fields), ALL_ANIME_FIELDS),
                        nsfw
                    ),
                    iterator -> asAnime(MyAnimeListImpl.this, iterator.getJsonObject("node"))
                );
            }
        };
    }

    @Override
    public final Anime getAnime(final long id){
        return getAnime(id, (String[]) null);
    }

    @Override
    public final Anime getAnime(final long id, final String... fields){
        return asAnime(this,
        handleResponse(
            () -> service.getAnime(
                auth,
                id,
                asFieldList(toCommaSeparatedString(fields), ALL_ANIME_FIELDS)
            )
        ));
    }

    @Override
    public final AnimeRankingQuery getAnimeRanking(final AnimeRankingType rankingType){
        return new AnimeRankingQuery(Objects.requireNonNull(rankingType)) {

            @Override
            public final List<AnimeRanking> search(){
                final JsonObject response = handleResponse(
                    () -> service.getAnimeRanking(
                        auth,
                        rankingType.field(),
                        limit,
                        offset,
                        asFieldList(toCommaSeparatedString(fields), ALL_ANIME_FIELDS),
                        nsfw
                    )
                );
                if(response == null) return null;

                final List<AnimeRanking> anime = new ArrayList<>();
                for(final JsonObject iterator : response.getJsonArray("data"))
                    anime.add(asAnimeRanking(MyAnimeListImpl.this, iterator));
                return anime;
            }

            @Override
            public final PaginatedIterator<AnimeRanking> searchAll(){
                return new PagedIterator<>(
                    offset,
                    offset -> service.getAnimeRanking(
                        auth,
                        rankingType.field(),
                        limit,
                        offset,
                        asFieldList(toCommaSeparatedString(fields), ALL_ANIME_FIELDS),
                        nsfw
                    ),
                    iterator -> asAnimeRanking(MyAnimeListImpl.this, iterator)
                );
            }
        };
    }

    @Override
    public final AnimeSeasonQuery getAnimeSeason(final int year, final Season season){
        return new AnimeSeasonQuery(year, Objects.requireNonNull(season)) {

            @Override
            public final List<Anime> search(){
                final JsonObject response = handleResponse(
                    () -> service.getAnimeSeason(
                        auth,
                        year,
                        season.field(),
                        sort != null ? sort.field() : null,
                        limit,
                        offset,
                        asFieldList(toCommaSeparatedString(fields), ALL_ANIME_FIELDS),
                        nsfw
                    )
                );
                if(response == null) return null;

                final List<Anime> anime = new ArrayList<>();
                for(final JsonObject iterator : response.getJsonArray("data"))
                    anime.add(asAnime(MyAnimeListImpl.this, iterator.getJsonObject("node")));
                return anime;
            }

            @Override
            public final PaginatedIterator<Anime> searchAll(){
                return new PagedIterator<>(
                    offset,
                    offset -> service.getAnimeSeason(
                        auth,
                        year,
                        season.field(),
                        sort != null ? sort.field() : null,
                        limit,
                        offset,
                        asFieldList(toCommaSeparatedString(fields), ALL_ANIME_FIELDS),
                        nsfw
                    ),
                    iterator -> asAnime(MyAnimeListImpl.this, iterator.getJsonObject("node"))
                );
            }

        };
    }

    @Override
    public final AnimeSuggestionQuery getAnimeSuggestions(){
        return new AnimeSuggestionQuery() {

            @Override
            public final List<Anime> search(){
                final JsonObject response = handleResponse(
                    () -> service.getAnimeSuggestions(
                        auth,
                        limit,
                        offset,
                        asFieldList(toCommaSeparatedString(fields), ALL_ANIME_FIELDS),
                        nsfw
                    )
                );
                if(response == null) return null;

                final List<Anime> anime = new ArrayList<>();
                for(final JsonObject iterator : response.getJsonArray("data"))
                    anime.add(asAnime(MyAnimeListImpl.this, iterator.getJsonObject("node")));
                return anime;
            }

            @Override
            public final PaginatedIterator<Anime> searchAll(){
                return new PagedIterator<>(
                    offset,
                    offset -> service.getAnimeSuggestions(
                        auth,
                        limit,
                        offset,
                        asFieldList(toCommaSeparatedString(fields), ALL_ANIME_FIELDS),
                        nsfw
                    ),
                    iterator -> asAnime(MyAnimeListImpl.this, iterator.getJsonObject("node"))
                );
            }

        };
    }

    @Override
    public final AnimeListUpdate updateAnimeListing(final long id){
        return new AnimeListUpdate(id) {

            @Override
            public synchronized final AnimeListStatus update(){
                final JsonObject response = handleResponse(
                    () -> service.updateAnimeListing(
                        auth,
                        id,
                        status != null ? status.field() : null,
                        rewatching,
                        score,
                        asYMD(startDate),
                        asYMD(finishDate),
                        watchedEpisodes,
                        priority.value(),
                        timesRewatched,
                        rewatchValue.value(),
                        toCommaSeparatedString(tags),
                        comments
                    )
                );
                if(response == null) return null;

                return asAnimeListStatus(MyAnimeListImpl.this, response, id);
            }

        };
    }

    @Override
    public synchronized final void deleteAnimeListing(final long id){
        handleVoidResponse(
            () -> service.deleteAnimeListing(
                auth,
                (int) id
            )
        );
    }

    @Override
    public final UserAnimeListQuery getUserAnimeListing(){
        return getUserAnimeListing("@me");
    }

    @Override
    public final UserAnimeListQuery getUserAnimeListing(final String username){
        return new UserAnimeListQuery(Objects.requireNonNull(username)) {

            @Override
            public final List<AnimeListStatus> search(){
                final JsonObject response = handleResponse(
                    () -> service.getUserAnimeListing(
                        auth,
                        username.equals("@me") ? "@me" : URLEncoder.encode(username, StandardCharsets.UTF_8),
                        status != null ? status.field() : null,
                        sort != null ? sort.field() : null,
                        limit,
                        offset,
                        asFieldList(toCommaSeparatedString(fields), ALL_ANIME_FIELDS)
                    )
                );
                if(response == null) return null;

                final List<AnimeListStatus> anime = new ArrayList<>();
                for(final JsonObject iterator : response.getJsonArray("data"))
                    anime.add(asAnimeListStatus(MyAnimeListImpl.this, iterator.getJsonObject("list_status"), asAnimePreview(MyAnimeListImpl.this, iterator.getJsonObject("node"))));
                return anime;
            }

            @Override
            public final PaginatedIterator<AnimeListStatus> searchAll(){
                return new PagedIterator<>(
                    offset,
                    offset -> service.getUserAnimeListing(
                        auth,
                        username.equals("@me") ? "@me" : URLEncoder.encode(username, StandardCharsets.UTF_8),
                        status != null ? status.field() : null,
                        sort != null ? sort.field() : null,
                        limit,
                        offset,
                        asFieldList(toCommaSeparatedString(fields), ALL_ANIME_FIELDS)
                    ),
                    iterator -> asAnimeListStatus(MyAnimeListImpl.this, iterator.getJsonObject("list_status"), asAnimePreview(MyAnimeListImpl.this, iterator.getJsonObject("node")))
                );
            }

        };
    }

    @Override
    public final List<ForumCategory> getForumBoards(){
        final JsonObject response = handleResponse(
            () -> service.getForumBoards(
                auth)
        );
        if(response == null) return null;

        final List<ForumCategory> categories = new ArrayList<>();
        for(final JsonObject iterator : response.getJsonArray("categories"))
            categories.add(asForumCategory(MyAnimeListImpl.this, iterator));
        return categories;
    }

    @Override
    public final ForumTopic getForumTopicDetail(final long id){
        return getForumTopicDetail(id, -1, -1);
    }

    @Override
    public final ForumTopic getForumTopicDetail(final long id, final int limit){
        return getForumTopicDetail(id, limit, -1);
    }

    @Override
    public final ForumTopic getForumTopicDetail(final long id, final int limit, final int offset){
        final JsonObject response = handleResponse(
            () -> service.getForumBoard(
                auth,
                id,
                limit == -1 ? null : limit,
                offset == -1 ? null : offset
            )
        );
        if(response == null) return null;

        return asForumTopic(MyAnimeListImpl.this, response.getJsonObject("data"));
    }

    @Override
    public final ForumSearchQuery getForumTopics(){
        return new ForumSearchQuery() {

            @Override
            public final List<ForumTopicDetail> search(){
                final JsonObject response = handleResponse(
                    () -> service.getForumTopics(
                        auth,
                        boardId,
                        subboardId,
                        limit,
                        offset,
                        sort,
                        query,
                        topicUsername,
                        username
                    )
                );
                if(response == null) return null;

                final List<ForumTopicDetail> topics = new ArrayList<>();
                for(final JsonObject iterator : response.getJsonArray("data"))
                    topics.add(asForumTopicDetail(MyAnimeListImpl.this, iterator));
                return topics;
            }

            @Override
            public synchronized final PaginatedIterator<ForumTopicDetail> searchAll(){
                return new PagedIterator<>(
                    offset,
                    offset -> service.getForumTopics(
                        auth,
                        boardId,
                        subboardId,
                        limit,
                        offset,
                        sort,
                        query,
                        topicUsername,
                        username
                    ),
                    iterator -> asForumTopicDetail(MyAnimeListImpl.this, iterator)
                );
            }

        };
    }

    @Override
    public final MangaSearchQuery getManga(){
        return new MangaSearchQuery() {

            @Override
            public final List<Manga> search(){
                final JsonObject response = handleResponse(
                    () -> service.getManga(
                        auth,
                        query,
                        limit,
                        offset,
                        asFieldList(toCommaSeparatedString(fields), ALL_MANGA_FIELDS),
                        nsfw
                    )
                );
                if(response == null) return null;

                final List<Manga> manga = new ArrayList<>();
                for(final JsonObject iterator : response.getJsonArray("data"))
                    manga.add(asManga(MyAnimeListImpl.this, iterator.getJsonObject("node")));
                return manga;
            }

            @Override
            public final PaginatedIterator<Manga> searchAll(){
                return new PagedIterator<>(
                    offset,
                    offset -> service.getManga(
                        auth,
                        query,
                        limit,
                        offset,
                        asFieldList(toCommaSeparatedString(fields), ALL_MANGA_FIELDS),
                        nsfw
                    ),
                    iterator -> asManga(MyAnimeListImpl.this, iterator.getJsonObject("node"))
                );
            }

        };
    }

    @Override
    public final Manga getManga(final long id){
        return getManga(id, (String[]) null);
    }

    @Override
    public final Manga getManga(final long id, final String... fields){
        return asManga(this,
        handleResponse(
            () -> service.getManga(
                auth,
                id,
                asFieldList(toCommaSeparatedString(fields), ALL_MANGA_FIELDS)
            )
        ));
    }

    @Override
    public final MangaRankingQuery getMangaRanking(final MangaRankingType rankingType){
        return new MangaRankingQuery(Objects.requireNonNull(rankingType)) {

            @Override
            public final List<MangaRanking> search(){
                final JsonObject response = handleResponse(
                    () -> service.getMangaRanking(
                        auth,
                        rankingType != null ? rankingType.field() : null,
                        limit,
                        offset,
                        asFieldList(toCommaSeparatedString(fields), ALL_MANGA_FIELDS),
                        nsfw
                    )
                );
                if(response == null) return null;

                final List<MangaRanking> manga = new ArrayList<>();
                for(final JsonObject iterator : response.getJsonArray("data"))
                    manga.add(asMangaRanking(MyAnimeListImpl.this, iterator));
                return manga;
            }

            @Override
            public final PaginatedIterator<MangaRanking> searchAll(){
                return new PagedIterator<>(
                    offset,
                    offset -> service.getMangaRanking(
                        auth,
                        rankingType != null ? rankingType.field() : null,
                        limit,
                        offset,
                        asFieldList(toCommaSeparatedString(fields), ALL_MANGA_FIELDS),
                        nsfw
                    ),
                    iterator -> asMangaRanking(MyAnimeListImpl.this, iterator)
                );
            }

        };
    }

    @Override
    public final MangaListUpdate updateMangaListing(final long id){
        return new MangaListUpdate(id) {

            @Override
            public synchronized final MangaListStatus update(){
                final JsonObject response = handleResponse(
                    () -> service.updateMangaListing(
                        auth,
                        id,
                        status != null ? status.field() : null,
                        rereading,
                        score,
                        asYMD(startDate),
                        asYMD(finishDate),
                        volumesRead,
                        chaptersRead,
                        priority.value(),
                        timesReread,
                        rereadValue.value(),
                        toCommaSeparatedString(tags),
                        comments
                    )
                );
                if(response == null) return null;

                return asMangaListStatus(MyAnimeListImpl.this, response, id);
            }

        };
    }

    @Override
    public synchronized final void deleteMangaListing(final long id){
        handleVoidResponse(
            () -> service.deleteMangaListing(
                auth,
                id
            )
        );
    }

    @Override
    public final UserMangaListQuery getUserMangaListing(){
        return getUserMangaListing("@me");
    }

    @Override
    public final UserMangaListQuery getUserMangaListing(final String username){
        return new UserMangaListQuery(Objects.requireNonNull(username)) {

            @Override
            public final List<MangaListStatus> search(){
                final JsonObject response = handleResponse(
                    () -> service.getUserMangaListing(
                        auth,
                        username.equals("@me") ? "@me" : URLEncoder.encode(username, StandardCharsets.UTF_8),
                        status != null ? status.field() : null,
                        sort != null ? sort.field() : null,
                        limit,
                        offset,
                        asFieldList(toCommaSeparatedString(fields), ALL_MANGA_FIELDS)
                    )
                );
                if(response == null) return null;

                final List<MangaListStatus> manga = new ArrayList<>();
                for(final JsonObject iterator : response.getJsonArray("data"))
                    manga.add(asMangaListStatus(MyAnimeListImpl.this, iterator.getJsonObject("list_status"), asMangaPreview(MyAnimeListImpl.this, iterator.getJsonObject("node"))));
                return manga;
            }

            @Override
            public final PaginatedIterator<MangaListStatus> searchAll(){
                return new PagedIterator<>(
                    offset,
                    offset -> service.getUserMangaListing(
                        auth,
                        username.equals("@me") ? "@me" : URLEncoder.encode(username, StandardCharsets.UTF_8),
                        status != null ? status.field() : null,
                        sort != null ? sort.field() : null,
                        limit,
                        offset,
                        asFieldList(toCommaSeparatedString(fields), ALL_MANGA_FIELDS)
                    ),
                    iterator -> asMangaListStatus(MyAnimeListImpl.this, iterator.getJsonObject("list_status"), asMangaPreview(MyAnimeListImpl.this, iterator.getJsonObject("node")))
                );
            }

        };
    }

    @Override
    public final User getMyself(){
        return getUser("@me", (String[]) null);
    }

    @Override
    public final User getMyself(final String... fields){
        return getUser("@me", fields);
    }

    @Override
    public final User getUser(final String username){
        return getUser(username, (String[]) null);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public final User getUser(final String username, final String... fields){
        if(!username.equals("@me"))
            throw new UnsupportedOperationException("The MyAnimeList API currently only supports user @me.");
        return asUser(this,
        handleResponse(
            () -> service.getUser(
                auth,
                username.equals("@me") ? "@me" : URLEncoder.encode(username, StandardCharsets.UTF_8),
                asFieldList(toCommaSeparatedString(fields), ALL_USER_FIELDS)
            )
        ));
    }

    //
    
    private static void handleVoidResponse(final ExceptionSupplier<Response<?>,IOException> supplier){
        handleResponseCodes(supplier);
    }
    
    private static JsonObject handleResponse(final ExceptionSupplier<Response<?>,IOException> supplier){
        final Response<?> response = handleResponseCodes(supplier);
        return response.code() == HttpURLConnection.HTTP_OK ? (JsonObject) response.body() : null;
    }

    private static Response<?> handleResponseCodes(final ExceptionSupplier<Response<?>,IOException> supplier){
        try{
            final Response<?> response = supplier.get();

            if(response.code() == HttpURLConnection.HTTP_OK)
                return response;
            else
                try{
                    throw new HTTPException(response.URL(), response.code(), (((JsonObject) response.body()).getString("message") + ' ' + ((JsonObject) response.body()).getString("error")).trim());
                }catch(final ClassCastException ignored){
                    throw new HTTPException(response.URL(), response.code(), response.raw());
                }
        }catch(final IOException e){ // client side failure
            throw new UncheckedIOException(e);
        }
    }

    private interface ExceptionSupplier<T,E extends Throwable>{

        T get() throws E;

    }

    @Override
    public String toString(){
        return "MyAnimeListImpl{" +
               "authenticator=" + authenticator +
               ", service=" + service +
               '}';
    }

    //

    @SuppressWarnings("SpellCheckingInspection")
    private static class PagedIterator<T> extends PaginatedIterator<T> {

        // ^\Qhttps://api.myanimelist.net/v2/\E.+?[?&]\Qoffset=\E(\d+)(?:&.*$|$)
        private static final Pattern nextPageRegex = Pattern.compile("^\\Q" + MyAnimeListService.baseURL + "\\E.+?[?&]\\Qoffset=\\E(\\d+)(?:&.*$|$)");

        private final Matcher nextPageMatcher = nextPageRegex.matcher("");

        private final Function<Integer,Response<JsonObject>> fullPageSupplier;
        private final Function<JsonObject,T> listAdapter;

        private final AtomicReference<Integer> nextOffset = new AtomicReference<>();
        private final List<T> firstPage;
        private final AtomicReference<Boolean> isFirstPage = new AtomicReference<>(null);

        PagedIterator(
            final Integer offset,
            final Function<Integer,Response<JsonObject>> fullPageSupplier,
            final Function<JsonObject,T> listAdapter
        ){
            this.fullPageSupplier   = fullPageSupplier;
            this.listAdapter        = listAdapter;

            // handle first page
            nextOffset.set(offset);
            firstPage = getNextPage();
            isFirstPage.set(true);
        }

        @Override
        final boolean hasNextPage(){
            return nextOffset.get() != -1;
        }

        @Override
        synchronized final List<T> getNextPage(){
            if(isFirstPage.get() != null && isFirstPage.get()){
                isFirstPage.set(false);
                return firstPage;
            }else{
                final JsonObject response = handleResponse(() -> fullPageSupplier.apply(nextOffset.get()));

                if(response == null){
                    nextOffset.set(-1);
                    return null;
                }

                final List<T> list = new ArrayList<>();
                for(final JsonObject data : response.getJsonArray("data"))
                    list.add(listAdapter.apply(data));

                if(response.getJsonObject("paging").containsKey("next")){
                    nextPageMatcher.reset(response.getJsonObject("paging").getString("next"));
                    if(nextPageMatcher.matches()){
                        nextOffset.set(Integer.parseInt(nextPageMatcher.group(1)));
                        return list;
                    }
                }
                nextOffset.set(-1);

                return list;
            }
        }

    }

    //

    /**
     * Handles how fields are finally sent to the server.
     *
     * Current behavior sends all if fields are null and none if an empty array is supplied
     *
     * @param fields comma separated fields
     * @param fieldsIfNull fallback fields
     * @return fields
     */
    private static String asFieldList(final String fields, final String fieldsIfNull){
        return fields == null ? fieldsIfNull : fields;
    }

    private static String toCommaSeparatedString(final List<String> fields){
        return toCommaSeparatedString(fields == null ? null : fields.toArray(new String[0]));
    }

    private static String toCommaSeparatedString(final String... fields){
        if(fields != null){
            if(fields.length == 0) return ""; // return blank for empty array

            final StringBuilder SB = new StringBuilder();
            for(final String field : fields)
                if(!field.isBlank())
                    SB.append(field).append(',');

            final String str = SB.toString();
            if(!str.isBlank())
                return str.substring(0, str.length() -1);
        }
        return null;
    }

    private static String asISO8601(final Long millis){
        return millis == null ? null : new SimpleDateFormat(MyAnimeListAPIResponseMapping.ISO8601).format(new Date(millis));
    }

    private static String asYMD(final Long millis){
        return millis == null ? null : new SimpleDateFormat(MyAnimeListAPIResponseMapping.YMD).format(new Date(millis));
    }

}
