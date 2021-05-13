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

import com.kttdevelopment.mal4j.Json.JsonObject;
import com.kttdevelopment.mal4j.anime.*;
import com.kttdevelopment.mal4j.anime.property.*;
import com.kttdevelopment.mal4j.anime.property.time.*;
import com.kttdevelopment.mal4j.manga.RelatedManga;
import com.kttdevelopment.mal4j.property.*;
import com.kttdevelopment.mal4j.query.AnimeListUpdate;

import java.util.*;

@SuppressWarnings("unused")
abstract class MyAnimeListSchema_Anime extends MyAnimeListSchema {
    
    static AnimeStatistics asAnimeStatistics(final MyAnimeList mal, final JsonObject schema){
        return new AnimeStatistics() {

            private final Integer   watching    = requireNonNull(() -> schema.getJsonObject("status").getInt("watching")),
                                    completed   = requireNonNull(() -> schema.getJsonObject("status").getInt("completed")),
                                    onHold      = requireNonNull(() -> schema.getJsonObject("status").getInt("on_hold")),
                                    dropped     = requireNonNull(() -> schema.getJsonObject("status").getInt("dropped")),
                                    planToWatch = requireNonNull(() -> schema.getJsonObject("status").getInt("plan_to_watch")),
                                    userCount   = requireNonNull(() -> schema.getInt("num_list_users"));

            // API methods

            @Override
            public final Integer getWatching() {
                return watching;
            }

            @Override
            public final Integer getCompleted() {
                return completed;
            }

            @Override
            public final Integer getOnHold() {
                return onHold;
            }

            @Override
            public final Integer getDropped() {
                return dropped;
            }

            @Override
            public final Integer getPlanToWatch() {
                return planToWatch;
            }

            @Override
            public final Integer getUserCount() {
                return userCount;
            }

            // additional methods

            @Override
            public final String toString(){
                return "AnimeStatistics{" +
                       "watching=" + watching +
                       ", completed=" + completed +
                       ", onHold=" + onHold +
                       ", dropped=" + dropped +
                       ", planToWatch=" + planToWatch +
                       ", userCount=" + userCount +
                       '}';
            }

        };
    }

    static Broadcast asBroadcast(final MyAnimeList mal, final JsonObject schema){
        return new Broadcast() {

            private final DayOfWeek dayOfWeek   = requireNonNull(() -> DayOfWeek.asEnum(schema.getString("day_of_the_week")));
            private final Time time             = requireNonNull(() -> asTime(schema.getString("start_time")));

            // API methods

            @Override
            public final DayOfWeek getDayOfWeek() {
                return dayOfWeek;
            }

            @Override
            public final Time getStartTime() {
                return time;
            }

            // additional methods

            @Override
            public String toString(){
                return "Broadcast{" +
                       "dayOfWeek=" + dayOfWeek +
                       ", time=" + time +
                       '}';
            }

        };
    }

    static StartSeason asStartSeason(final MyAnimeList mal, final JsonObject schema){
        return new StartSeason() {

            private final Integer year  = requireNonNull(() -> schema.getInt("year"));
            private final Season season = requireNonNull(() -> Season.asEnum(schema.getString("season")));

            // API methods

            @Override
            public final Integer getYear() {
                return year;
            }

            @Override
            public final Season getSeason() {
                return season;
            }

            // additional methods

            @Override
            public final String toString(){
                return "StartSeason{" +
                       "year=" + year +
                       ", season=" + season +
                       '}';
            }

        };
    }

    static Studio asStudio(final MyAnimeList mal, final JsonObject schema){
        return new Studio() {

            private final Long id       = requireNonNull(() -> schema.getLong("id"));
            private final String name   = requireNonNull(() -> schema.getString("name"));

            // API methods

            @Override
            public final Long getID() {
                return id;
            }

            @Override
            public final String getName() {
                return name;
            }

            // additional methods


            @Override
            public final String toString(){
                return "Studio{" +
                       "id=" + id +
                       ", name='" + name + '\'' +
                       '}';
            }

        };
    }

    static OpeningTheme asOpeningTheme(final MyAnimeList mal, final JsonObject schema, Anime anime){
        return new OpeningTheme() {

            private final Long id       = requireNonNull(() -> schema.getLong("id"));
            private final String text   = requireNonNull(() -> schema.getString("text"));

            // API methods

            @Override
            public final Long getID(){
                return id;
            }

            @Override
            public final String getText(){
                return text;
            }

            // additional methods

            @Override
            public final Anime getAnime(){
                return anime;
            }

            @Override
            public final String toString(){
                return "OpeningTheme{" +
                       "id=" + id +
                       ", text='" + text + '\'' +
                       '}';
            }

        };
    }

    static EndingTheme asEndingTheme(final MyAnimeList mal, final JsonObject schema, Anime anime){
        return new EndingTheme() {

            private final Long id       = requireNonNull(() -> schema.getLong("id"));
            private final String text   = requireNonNull(() -> schema.getString("text"));

            // API methods

            @Override
            public final Long getID(){
                return id;
            }

            @Override
            public final String getText(){
                return text;
            }

            // additional methods

            @Override
            public final Anime getAnime(){
                return anime;
            }

            @Override
            public final String toString(){
                return "EndingTheme{" +
                       "id=" + id +
                       ", text='" + text + '\'' +
                       '}';
            }

        };
    }

    static Anime asAnime(final MyAnimeList mal, final JsonObject schema){
        return new Anime() {

            private final Long id               = requireNonNull(() -> schema.getLong("id"));
            private final String title          = requireNonNull(() -> schema.getString("title"));
            private final Picture mainPicture   = requireNonNull(() -> MyAnimeListSchema_Common.asPicture(mal, schema.getJsonObject("main_picture")));
            private final AlternativeTitles alternativeTitles
                                                = requireNonNull(() -> MyAnimeListSchema_Common.asAlternativeTitles(mal, schema.getJsonObject("alternative_titles")));
            private final Long startDate        = requireNonNull(() -> parseDate(schema.getString("start_date")));
            private final Long endDate          = requireNonNull(() -> parseDate(schema.getString("end_date")));
            private final String synopsis       = requireNonNull(() -> schema.getString("synopsis"));
            private final Float meanRating      = requireNonNull(() -> schema.getFloat("mean"));
            private final Integer rank          = requireNonNull(() -> schema.getInt("rank"));
            private final Integer popularity    = requireNonNull(() -> schema.getInt("popularity"));
            private final Integer usersListing  = requireNonNull(() -> schema.getInt("num_list_users"));
            private final Integer usersScoring  = requireNonNull(() -> schema.getInt("num_scoring_users"));
            private final NSFW nsfw             = requireNonNull(() -> NSFW.asEnum(schema.getString("nsfw")));
            private final Genre[] genres        = requireNonNull(() -> adaptList(schema.getJsonArray("genres"), g -> Genre.asAnimeGenre(g.getInt("id")), Genre.class));
            private final Long createdAt        = requireNonNull(() -> parseISO8601(schema.getString("created_at")));
            private final Long updatedAt        = requireNonNull(() -> parseISO8601(schema.getString("updated_at")));
            private final AnimeType type        = requireNonNull(() -> AnimeType.asEnum(schema.getString("media_type")));
            private final AnimeAirStatus status = requireNonNull(() -> AnimeAirStatus.asEnum(schema.getString("status")));
            private final AnimeListStatus listStatus
                                                = requireNonNull(() -> asAnimeListStatus(mal, schema.getJsonObject("my_list_status"), id, this));
            private final Integer episodes      = requireNonNull(() -> schema.getInt("num_episodes"));
            private final StartSeason startSeason
                                                = requireNonNull(() -> asStartSeason(mal, schema.getJsonObject("start_season")));
            private final Broadcast broadcast   = requireNonNull(() -> asBroadcast(mal, schema.getJsonObject("broadcast")));
            private final AnimeSource source    = requireNonNull(() -> AnimeSource.asEnum(schema.getString("source")));
            private final Integer episodeLength = requireNonNull(() -> schema.getInt("average_episode_duration"));
            private final AnimeRating rating    = requireNonNull(() -> AnimeRating.asEnum(schema.getString("rating")));
            private final Studio[] studios      = requireNonNull(() -> adaptList(schema.getJsonArray("studios"), s -> asStudio(mal, s), Studio.class));
            private final Picture[] pictures    = requireNonNull(() -> adaptList(schema.getJsonArray("pictures"), p -> MyAnimeListSchema_Common.asPicture(mal, p), Picture.class));
            private final String background     = requireNonNull(() -> schema.getString("background"));
            private final RelatedAnime[] relatedAnime
                                                = requireNonNull(() -> adaptList(schema.getJsonArray("related_anime"), a -> asRelatedAnime(mal, a), RelatedAnime.class));
            private final RelatedManga[] relatedManga
                                                = requireNonNull(() -> adaptList(schema.getJsonArray("related_manga"), m -> MyAnimeListSchema_Manga.asRelatedManga(mal, m), RelatedManga.class));
            private final AnimeRecommendation[] recommendations
                                                = requireNonNull(() -> adaptList(schema.getJsonArray("recommendations"), r -> asAnimeRecommendation(mal, r), AnimeRecommendation.class));
            private final AnimeStatistics statistics
                                                = requireNonNull(() -> asAnimeStatistics(mal, schema.getJsonObject("statistics")));
            private final OpeningTheme[] openingThemes
                                                = requireNonNull(() -> adaptList(schema.getJsonArray("opening_themes"), o -> asOpeningTheme(mal, o, this), OpeningTheme.class));
            private final EndingTheme[] endingThemes
                                                = requireNonNull(() -> adaptList(schema.getJsonArray("ending_themes"), o -> asEndingTheme(mal, o, this), EndingTheme.class));

            // API methods

            @Override
            public final Long getID() {
                return id;
            }

            @Override
            public final String getTitle() {
                return title;
            }

            @Override
            public final Picture getMainPicture() {
                return mainPicture;
            }

            @Override
            public final AlternativeTitles getAlternativeTitles() {
                return alternativeTitles;
            }

            @Override
            public final Date getStartDate() {
                return startDate == null ? null : new Date(startDate);
            }

            @Override
            public final Date getEndDate() {
                return endDate == null ? null : new Date(endDate);
            }

            @Override
            public final String getSynopsis() {
                return synopsis;
            }

            @Override
            public final Float  getMeanRating() {
                return meanRating;
            }

            @Override
            public final Integer getRank() {
                return rank;
            }

            @Override
            public final Integer getPopularity() {
                return popularity;
            }

            @Override
            public final Integer getUserListingCount() {
                return usersListing;
            }

            @Override
            public final Integer getUserScoringCount() {
                return usersScoring;
            }

            @Override
            public final NSFW getNSFW() {
                return nsfw;
            }

            @Override
            public final Genre[] getGenres() {
                return copyArray(genres, Genre.class);
            }

            @Override
            public final Date getCreatedAt() {
                return createdAt == null ? null : new Date(createdAt);
            }

            @Override
            public final Long getCreatedAtEpochMillis(){
                return createdAt;
            }

            @Override
            public final Date getUpdatedAt() {
                return updatedAt == null ? null : new Date(updatedAt);
            }

            @Override
            public final Long getUpdatedAtEpochMillis(){
                return updatedAt;
            }

            @Override
            public final AnimeType getType() {
                return type;
            }

            @Override
            public final AnimeAirStatus getStatus() {
                return status;
            }

            @Override
            public final AnimeListStatus getListStatus() {
                return listStatus;
            }

            @Override
            public final Integer getEpisodes() {
                return episodes;
            }

            @Override
            public final StartSeason getStartSeason() {
                return startSeason;
            }

            @Override
            public final Broadcast getBroadcast() {
                return broadcast;
            }

            @Override
            public final AnimeSource getSource() {
                return source;
            }

            @Override
            public final Integer getAverageEpisodeLength() {
                return episodeLength;
            }

            @Override
            public final AnimeRating getRating() {
                return rating;
            }

            @Override
            public final Studio[] getStudios() {
                return copyArray(studios, Studio.class);
            }

            @Override
            public final Picture[] getPictures() {
                return copyArray(pictures, Picture.class);
            }

            @Override
            public final String getBackground() {
                return background;
            }

            @Override
            public final RelatedAnime[] getRelatedAnime() {
                return copyArray(relatedAnime, RelatedAnime.class);
            }

            @Override
            public final RelatedManga[] getRelatedManga() {
                return copyArray(relatedManga, RelatedManga.class);
            }

            @Override
            public final AnimeRecommendation[] getRecommendations() {
                return copyArray(recommendations, AnimeRecommendation.class);
            }

            @Override
            public final AnimeStatistics getStatistics() {
                return statistics;
            }

            @Override
            public final OpeningTheme[] getOpeningThemes(){
                return copyArray(openingThemes, OpeningTheme.class);
            }

            @Override
            public final EndingTheme[] getEndingThemes(){
                return copyArray(endingThemes, EndingTheme.class);
            }

            // additional methods

            @Override
            public final String toString(){
                return "Anime{" +
                       "id=" + id +
                       ", title='" + title + '\'' +
                       ", mainPicture=" + mainPicture +
                       ", alternativeTitles=" + alternativeTitles +
                       ", startDate=" + startDate +
                       ", endDate=" + endDate +
                       ", synopsis='" + synopsis + '\'' +
                       ", meanRating=" + meanRating +
                       ", rank=" + rank +
                       ", popularity=" + popularity +
                       ", usersListing=" + usersListing +
                       ", usersScoring=" + usersScoring +
                       ", nsfw=" + nsfw +
                       ", genres=" + Arrays.toString(genres) +
                       ", createdAt=" + createdAt +
                       ", updatedAt=" + updatedAt +
                       ", type=" + type +
                       ", status=" + status +
                       ", listStatus=" + listStatus +
                       ", episodes=" + episodes +
                       ", startSeason=" + startSeason +
                       ", broadcast=" + broadcast +
                       ", source=" + source +
                       ", episodeLength=" + episodeLength +
                       ", rating=" + rating +
                       ", studios=" + Arrays.toString(studios) +
                       ", pictures=" + Arrays.toString(pictures) +
                       ", background='" + background + '\'' +
                       ", relatedAnime=" + Arrays.toString(relatedAnime) +
                       ", relatedManga=" + Arrays.toString(relatedManga) +
                       ", recommendations=" + Arrays.toString(recommendations) +
                       ", statistics=" + statistics +
                       ", openingThemes=" + Arrays.toString(openingThemes) +
                       ", endingThemes=" + Arrays.toString(endingThemes) +
                       '}';
            }

        };
    }

    static AnimeListStatus asAnimeListStatus(final MyAnimeList mal, final JsonObject schema, final long anime_id){
        return asAnimeListStatus(mal, schema, anime_id, null);
    }

    static AnimeListStatus asAnimeListStatus(final MyAnimeList mal, final JsonObject schema, final AnimePreview anime_preview){
        return asAnimeListStatus(mal, schema, null, Objects.requireNonNull(anime_preview, "Anime preview must not be null"));
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static AnimeListStatus asAnimeListStatus(final MyAnimeList mal, final JsonObject schema, final Long anime_id, final AnimePreview anime_preview) {
        if(anime_id == null && anime_preview == null)
            throw new NullPointerException("Anime id and anime preview must not both be null");
        return new AnimeListStatus() {

            private final AnimePreview anime        = anime_preview;
            private final Long id                   = anime_id;

            private final AnimeStatus status        = requireNonNull(() -> AnimeStatus.asEnum(schema.getString("status")));
            private final Integer score             = requireNonNull(() -> schema.getInt("score"));
            private final Long startDate            = requireNonNull(() -> parseDate(schema.getString("start_date")));
            private final Long finishDate           = requireNonNull(() -> parseDate(schema.getString("finish_date")));
            private final Priority priority         = requireNonNull(() -> Priority.asEnum(schema.getInt("priority")));
            private final String[] tags             = requireNonNull(() -> schema.getStringArray("tags"));
            private final String comments           = requireNonNull(() -> schema.getString("comments"));
            private final Long updatedAt            = requireNonNull(() -> parseISO8601(schema.getString("updated_at")));
            private final Integer watchedEpisodes   = requireNonNull(() -> schema.getInt("num_episodes_watched"));
            private final Boolean rewatching        = requireNonNull(() -> schema.getBoolean("is_rewatching"));
            private final Integer timesRewatched    = requireNonNull(() -> schema.getInt("num_times_rewatched"));
            private final RewatchValue rewatchValue = requireNonNull(() -> RewatchValue.asEnum(schema.getInt("rewatch_value")));

            // API methods

            @Override
            public final AnimeStatus getStatus() {
                return status;
            }

            @Override
            public final Integer getScore() {
                return score;
            }

            @Override
            public final Date getStartDate() {
                return startDate == null ? null : new Date(startDate);
            }

            @Override
            public final Date getFinishDate() {
                return finishDate == null ? null : new Date(finishDate);
            }

            @Override
            public final Priority getPriority() {
                return priority;
            }

            @Override
            public final String[] getTags() {
                return copyArray(tags, String.class);
            }

            @Override
            public final String getComments() {
                return comments;
            }

            @Override
            public final Date getUpdatedAt() {
                return updatedAt == null ? null : new Date(updatedAt);
            }

            @Override
            public final Long getUpdatedAtEpochMillis(){
                return updatedAt;
            }

            @Override
            public final Integer getWatchedEpisodes() {
                return watchedEpisodes;
            }

            @Override
            public final Boolean isRewatching() {
                return rewatching;
            }

            @Override
            public final Integer getTimesRewatched() {
                return timesRewatched;
            }

            @Override
            public final RewatchValue getRewatchValue() {
                return rewatchValue;
            }

            // additional methods

            @Override
            public final Anime getAnime(){
                return anime != null ? anime.getAnime() : mal.getAnime(id);
            }

            @Override
            public final AnimePreview getAnimePreview(){
                return anime != null ? anime : mal.getAnime(id);
            }

            @Override
            public final AnimeListUpdate edit(){
                return mal.updateAnimeListing(id != null ? id : anime.getID());
            }

            @Override
            public final String toString(){
                return "AnimeListStatus{" +
                       "id=" + id +
                       ", status=" + status +
                       ", score=" + score +
                       ", startDate=" + startDate +
                       ", finishDate=" + finishDate +
                       ", priority=" + priority +
                       ", tags=" + Arrays.toString(tags) +
                       ", comments='" + comments + '\'' +
                       ", updatedAt=" + updatedAt +
                       ", watchedEpisodes=" + watchedEpisodes +
                       ", rewatching=" + rewatching +
                       ", timesRewatched=" + timesRewatched +
                       ", rewatchValue=" + rewatchValue +
                       '}';
            }

        };
    }

    static AnimePreview asAnimePreview(final MyAnimeList mal, final JsonObject schema){
        return new AnimePreview() {

            private final Long id               = requireNonNull(() -> schema.getLong("id"));
            private final String title          = requireNonNull(() -> schema.getString("title"));
            private final Picture mainPicture   = requireNonNull(() -> MyAnimeListSchema_Common.asPicture(mal, schema.getJsonObject("main_picture")));
            private final AlternativeTitles alternativeTitles
                                                = requireNonNull(() -> MyAnimeListSchema_Common.asAlternativeTitles(mal, schema.getJsonObject("alternative_titles")));
            private final Long startDate        = requireNonNull(() -> parseDate(schema.getString("start_date")));
            private final Long endDate          = requireNonNull(() -> parseDate(schema.getString("end_date")));
            private final String synopsis       = requireNonNull(() -> schema.getString("synopsis"));
            private final Float meanRating      = requireNonNull(() -> schema.getFloat("mean"));
            private final Integer rank          = requireNonNull(() -> schema.getInt("rank"));
            private final Integer popularity    = requireNonNull(() -> schema.getInt("popularity"));
            private final Integer usersListing  = requireNonNull(() -> schema.getInt("num_list_users"));
            private final Integer usersScoring  = requireNonNull(() -> schema.getInt("num_scoring_users"));
            private final NSFW nsfw             = requireNonNull(() -> NSFW.asEnum(schema.getString("nsfw")));
            private final Genre[] genres        = requireNonNull(() -> adaptList(schema.getJsonArray("genres"), g -> Genre.asAnimeGenre(g.getInt("id")), Genre.class));
            private final Long createdAt        = requireNonNull(() -> parseISO8601(schema.getString("created_at")));
            private final Long updatedAt        = requireNonNull(() -> parseISO8601(schema.getString("updated_at")));
            private final AnimeType type        = requireNonNull(() -> AnimeType.asEnum(schema.getString("media_type")));
            private final AnimeAirStatus status = requireNonNull(() -> AnimeAirStatus.asEnum(schema.getString("status")));
            private final AnimeListStatus listStatus
                                                = requireNonNull(() -> asAnimeListStatus(mal, schema.getJsonObject("my_list_status"), id, this));
            private final Integer episodes      = requireNonNull(() -> schema.getInt("num_episodes"));
            private final StartSeason startSeason
                                                = requireNonNull(() -> asStartSeason(mal, schema.getJsonObject("start_season")));
            private final Broadcast broadcast   = requireNonNull(() -> asBroadcast(mal, schema.getJsonObject("broadcast")));
            private final AnimeSource source    = requireNonNull(() -> AnimeSource.asEnum(schema.getString("source")));
            private final Integer episodeLength = requireNonNull(() -> schema.getInt("average_episode_duration"));
            private final AnimeRating rating    = requireNonNull(() -> AnimeRating.asEnum(schema.getString("rating")));
            private final Studio[] studios      = requireNonNull(() -> adaptList(schema.getJsonArray("studios"), s -> asStudio(mal, s), Studio.class));

            // API methods

            @Override
            public final Long getID() {
                return id;
            }

            @Override
            public final String getTitle() {
                return title;
            }

            @Override
            public final Picture getMainPicture() {
                return mainPicture;
            }

            @Override
            public final AlternativeTitles getAlternativeTitles() {
                return alternativeTitles;
            }

            @Override
            public final Date getStartDate() {
                return startDate == null ? null : new Date(startDate);
            }

            @Override
            public final Date getEndDate() {
                return endDate == null ? null : new Date(endDate);
            }

            @Override
            public final String getSynopsis() {
                return synopsis;
            }

            @Override
            public final Float  getMeanRating() {
                return meanRating;
            }

            @Override
            public final Integer getRank() {
                return rank;
            }

            @Override
            public final Integer getPopularity() {
                return popularity;
            }

            @Override
            public final Integer getUserListingCount() {
                return usersListing;
            }

            @Override
            public final Integer getUserScoringCount() {
                return usersScoring;
            }

            @Override
            public final NSFW getNSFW() {
                return nsfw;
            }

            @Override
            public final Genre[] getGenres() {
                return copyArray(genres, Genre.class);
            }

            @Override
            public final Date getCreatedAt() {
                return createdAt == null ? null : new Date(createdAt);
            }

            @Override
            public final Long getCreatedAtEpochMillis(){
                return createdAt;
            }

            @Override
            public final Date getUpdatedAt() {
                return updatedAt == null ? null : new Date(updatedAt);
            }

            @Override
            public final Long getUpdatedAtEpochMillis(){
                return updatedAt;
            }

            @Override
            public final AnimeType getType() {
                return type;
            }

            @Override
            public final AnimeAirStatus getStatus() {
                return status;
            }

            @Override
            public final AnimeListStatus getListStatus() {
                return listStatus;
            }

            @Override
            public final Integer getEpisodes() {
                return episodes;
            }

            @Override
            public final StartSeason getStartSeason() {
                return startSeason;
            }

            @Override
            public final Broadcast getBroadcast() {
                return broadcast;
            }

            @Override
            public final AnimeSource getSource() {
                return source;
            }

            @Override
            public final Integer getAverageEpisodeLength() {
                return episodeLength;
            }

            @Override
            public final AnimeRating getRating() {
                return rating;
            }

            @Override
            public final Studio[] getStudios() {
                return copyArray(studios, Studio.class);
            }

            // additional methods

            @Override
            public final Anime getAnime() {
                return mal.getAnime(id);
            }

            @Override
            public String toString(){
                return "AnimePreview{" +
                       "id=" + id +
                       ", title='" + title + '\'' +
                       ", mainPicture=" + mainPicture +
                       ", alternativeTitles=" + alternativeTitles +
                       ", startDate=" + startDate +
                       ", endDate=" + endDate +
                       ", synopsis='" + synopsis + '\'' +
                       ", meanRating=" + meanRating +
                       ", rank=" + rank +
                       ", popularity=" + popularity +
                       ", usersListing=" + usersListing +
                       ", usersScoring=" + usersScoring +
                       ", nsfw=" + nsfw +
                       ", genres=" + Arrays.toString(genres) +
                       ", createdAt=" + createdAt +
                       ", updatedAt=" + updatedAt +
                       ", type=" + type +
                       ", status=" + status +
                       ", listStatus=" + listStatus +
                       ", episodes=" + episodes +
                       ", startSeason=" + startSeason +
                       ", broadcast=" + broadcast +
                       ", source=" + source +
                       ", episodeLength=" + episodeLength +
                       ", rating=" + rating +
                       ", studios=" + Arrays.toString(studios) +
                       '}';
            }

        };
    }

    static AnimeRanking asAnimeRanking(final MyAnimeList mal, final JsonObject schema){
        return new AnimeRanking() {

            private final AnimePreview anime        = requireNonNull(() -> asAnimePreview(mal, schema.getJsonObject("node")));
            private final Integer ranking           = requireNonNull(() -> schema.getJsonObject("ranking").getInt("rank"));
            private final Integer previousRanking   = requireNonNull(() -> schema.getJsonObject("ranking").getInt("previous_rank"));

            // API methods

            @Override
            public final AnimePreview getAnimePreview(){
                return anime;
            }

            @Override
            public final Integer getRanking(){
                return ranking;
            }

            @Override
            public final Integer getPreviousRank(){
                return previousRanking;
            }

            // additional methods

            @Override
            public final Anime getAnime(){
                return mal.getAnime(anime.getID());
            }

            @Override
            public final String toString(){
                return "AnimeRanking{" +
                       "anime=" + anime +
                       ", ranking=" + ranking +
                       ", previousRanking=" + previousRanking +
                       '}';
            }

        };
    }

    static AnimeRecommendation asAnimeRecommendation(final MyAnimeList mal, final JsonObject schema){
        return new AnimeRecommendation() {

            private final AnimePreview anime        = requireNonNull(() -> asAnimePreview(mal, schema.getJsonObject("node")));
            private final Integer recommendations   = requireNonNull(() -> schema.getInt("num_recommendations"));

            // API methods

            @Override
            public final AnimePreview getAnimePreview() {
                return anime;
            }

            @Override
            public final Integer getRecommendations() {
                return recommendations;
            }

            // additional methods

            @Override
            public final Anime getAnime() {
                return mal.getAnime(anime.getID());
            }

            @Override
            public final String toString(){
                return "AnimeRecommendation{" +
                       "anime=" + anime +
                       ", recommendations=" + recommendations +
                       '}';
            }

        };
    }

    static RelatedAnime asRelatedAnime(final MyAnimeList mal, final JsonObject schema){
        return new RelatedAnime() {

            private final AnimePreview anime            = requireNonNull(() -> asAnimePreview(mal, schema.getJsonObject("node")));
            private final RelationType relationType     = requireNonNull(() -> RelationType.asEnum(schema.getString("relation_type")));
            private final String relationTypeFormatted  = requireNonNull(() -> schema.getString("relation_type_formatted"));

            // API methods

            @Override
            public final AnimePreview getAnimePreview() {
                return anime;
            }

            @Override
            public final RelationType getRelationType() {
                return relationType;
            }

            @Override
            public final String getRelationTypeFormat() {
                return relationTypeFormatted;
            }

            // additional methods

            @Override
            public final Anime getAnime() {
                return mal.getAnime(anime.getID());
            }

            @Override
            public final String toString(){
                return "RelatedAnime{" +
                       "anime=" + anime +
                       ", relationType=" + relationType +
                       ", relationTypeFormatted='" + relationTypeFormatted + '\'' +
                       '}';
            }

        };
    }

}
