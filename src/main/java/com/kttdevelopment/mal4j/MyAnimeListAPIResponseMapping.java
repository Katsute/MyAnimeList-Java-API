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

import com.kttdevelopment.mal4j.anime.*;
import com.kttdevelopment.mal4j.anime.property.*;
import com.kttdevelopment.mal4j.anime.property.time.*;
import com.kttdevelopment.mal4j.forum.*;
import com.kttdevelopment.mal4j.forum.property.*;
import com.kttdevelopment.mal4j.manga.*;
import com.kttdevelopment.mal4j.manga.property.*;
import com.kttdevelopment.mal4j.property.*;
import com.kttdevelopment.mal4j.query.AnimeListUpdate;
import com.kttdevelopment.mal4j.query.MangaListUpdate;
import com.kttdevelopment.mal4j.query.property.*;
import com.kttdevelopment.mal4j.user.UserAnimeStatistics;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.kttdevelopment.mal4j.Json.*;

/**
 * Represents the MyAnimeList API response as an object.
 *
 * @see MyAnimeListImpl
 */
@SuppressWarnings({"unused","unchecked","ConstantConditions"})
abstract class MyAnimeListAPIResponseMapping {

    static abstract class Anime {

        static AnimeStatistics asAnimeStatistics(final MyAnimeList mal, final JsonObject schema){
            return new AnimeStatistics() {

                private final int   watching    = requireNonNullElse(() -> schema.getJsonObject("status").getInt("watching"), 0),
                                    completed   = requireNonNullElse(() -> schema.getJsonObject("status").getInt("completed"), 0),
                                    onHold      = requireNonNullElse(() -> schema.getJsonObject("status").getInt("on_hold"), 0),
                                    dropped     = requireNonNullElse(() -> schema.getJsonObject("status").getInt("dropped"), 0),
                                    planToWatch = requireNonNullElse(() -> schema.getJsonObject("status").getInt("plan_to_watch"), 0),
                                    userCount   = requireNonNullElse(() -> schema.getInt("num_list_users"), -1);

                // API methods

                @Override
                public final int getWatching() {
                    return watching;
                }

                @Override
                public final int getCompleted() {
                    return completed;
                }

                @Override
                public final int getOnHold() {
                    return onHold;
                }

                @Override
                public final int getDropped() {
                    return dropped;
                }

                @Override
                public final int getPlanToWatch() {
                    return planToWatch;
                }

                @Override
                public final int getUserCount() {
                    return userCount;
                }

                // additional methods

                @Override
                public final String toString() {
                    return AutomatedToString(this);
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
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static StartSeason asStartSeason(final MyAnimeList mal, final JsonObject schema){
            return new StartSeason() {

                private final int year      = requireNonNullElse(() -> schema.getInt("year"), -1);
                private final Season season = requireNonNull(() -> Season.asEnum(schema.getString("season")));

                // API methods

                @Override
                public final int getYear() {
                    return year;
                }

                @Override
                public final Season getSeason() {
                    return season;
                }

                // additional methods

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static Studio asStudio(final MyAnimeList mal, final JsonObject schema){
            return new Studio() {

                private final long id       = requireNonNullElse(() -> schema.getLong("id"), -1L);
                private final String name   = requireNonNull(() -> schema.getString("name"));

                // API methods

                @Override
                public final long getID() {
                    return id;
                }

                @Override
                public final String getName() {
                    return name;
                }

                // additional methods

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static com.kttdevelopment.mal4j.anime.Anime asAnime(final MyAnimeList mal, final JsonObject schema){
            return new com.kttdevelopment.mal4j.anime.Anime() {

                private final long id               = requireNonNullElse(() -> schema.getLong("id"), -1L);
                private final String title          = requireNonNull(() -> schema.getString("title"));
                private final Picture mainPicture   = requireNonNull(() -> Common.asPicture(mal, schema.getJsonObject("main_picture")));
                private final AlternativeTitles alternativeTitles = requireNonNull(() -> Common.asAlternativeTitles(mal, schema.getJsonObject("alternative_titles")));
                private final long startDate        = requireNonNullElse(() -> parseDate(schema.getString("start_date")), -1L);
                private final long endDate          = requireNonNullElse(() -> parseDate(schema.getString("end_date")), -1L);
                private final String synopsis       = requireNonNull(() -> schema.getString("synopsis"));
                private final float meanRating      = requireNonNullElse(() -> schema.getFloat("mean"), 0f);
                private final int rank              = requireNonNullElse(() -> schema.getInt("rank"), -1);
                private final int popularity        = requireNonNullElse(() -> schema.getInt("popularity"), -1);
                private final int usersListing      = requireNonNullElse(() -> schema.getInt("num_list_users"), -1);
                private final int usersScoring      = requireNonNullElse(() -> schema.getInt("num_scoring_users"), -1);
                private final NSFW nsfw             = requireNonNull(() -> NSFW.asEnum(schema.getString("nsfw")));
                private final Genre[] genres        = requireNonNull(() -> adaptList(schema.getJsonArray("genres"), g -> Genre.asEnum(g.getInt("id")), Genre.class));
                private final long createdAt        = requireNonNullElse(() -> parseISO8601(schema.getString("created_at")), -1L);
                private final long updatedAt        = requireNonNullElse(() -> parseISO8601(schema.getString("updated_at")), -1L);
                private final AnimeType type        = requireNonNull(() -> AnimeType.asEnum(schema.getString("media_type")));
                private final AnimeAirStatus status = requireNonNull(() -> AnimeAirStatus.asEnum(schema.getString("status")));
                private final AnimeListStatus listStatus = requireNonNull(() -> asAnimeListStatus(mal, schema.getJsonObject("my_list_status"), this));
                private final int episodes          = requireNonNullElse(() -> schema.getInt("num_episodes"), -1);
                private final StartSeason startSeason = requireNonNull(() -> asStartSeason(mal, schema.getJsonObject("start_season")));
                private final Broadcast broadcast   = requireNonNull(() -> asBroadcast(mal, schema.getJsonObject("broadcast")));
                private final AnimeSource source    = requireNonNull(() -> AnimeSource.asEnum(schema.getString("source")));
                private final int episodeLength     = requireNonNullElse(() -> schema.getInt("average_episode_duration"), -1);
                private final AnimeRating rating    = requireNonNull(() -> AnimeRating.asEnum(schema.getString("rating")));
                private final Studio[] studios      = requireNonNull(() -> adaptList(schema.getJsonArray("studios"), s -> asStudio(mal, s), Studio.class));
                private final Picture[] pictures    = requireNonNull(() -> adaptList(schema.getJsonArray("pictures"), p -> Common.asPicture(mal, p), Picture.class));
                private final String background     = requireNonNull(() -> schema.getString("background"));
                private final RelatedAnime[] relatedAnime = requireNonNull(() -> adaptList(schema.getJsonArray("related_anime"), a -> asRelatedAnime(mal, a), RelatedAnime.class));
                private final RelatedManga[] relatedManga = requireNonNull(() -> adaptList(schema.getJsonArray("related_manga"), m -> Manga.asRelatedManga(mal, m), RelatedManga.class));
                private final AnimeRecommendation[] recommendations = requireNonNull(() -> adaptList(schema.getJsonArray("recommendations"), r -> asAnimeRecommendation(mal, r), AnimeRecommendation.class));
                private final AnimeStatistics statistics = requireNonNull(() -> asAnimeStatistics(mal, schema.getJsonObject("statistics")));

                // API methods

                @Override
                public final long getID() {
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
                    return new Date(startDate);
                }

                @Override
                public final long getStartDateEpochMillis(){
                    return startDate;
                }

                @Override
                public final Date getEndDate() {
                    return new Date(endDate);
                }

                @Override
                public final long getEndDateEpochMillis(){
                    return endDate;
                }

                @Override
                public final String getSynopsis() {
                    return synopsis;
                }

                @Override
                public final float getMeanRating() {
                    return meanRating;
                }

                @Override
                public final int getRank() {
                    return rank;
                }

                @Override
                public final int getPopularity() {
                    return popularity;
                }

                @Override
                public final int getUserListingCount() {
                    return usersListing;
                }

                @Override
                public final int getUserScoringCount() {
                    return usersScoring;
                }

                @Override
                public final NSFW getNSFW() {
                    return nsfw;
                }

                @Override
                public final Genre[] getGenres() {
                    return genres;
                }

                @Override
                public final long getCreatedAt() {
                    return createdAt;
                }

                @Override
                public final long getUpdatedAt() {
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
                public final int getEpisodes() {
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
                public final int getAverageEpisodeLength() {
                    return episodeLength;
                }

                @Override
                public final AnimeRating getRating() {
                    return rating;
                }

                @Override
                public final Studio[] getStudios() {
                    return studios;
                }

                @Override
                public final Picture[] getPictures() {
                    return pictures;
                }

                @Override
                public final String getBackground() {
                    return background;
                }

                @Override
                public final RelatedAnime[] getRelatedAnime() {
                    return relatedAnime;
                }

                @Override
                public final RelatedManga[] getRelatedManga() {
                    return relatedManga;
                }

                @Override
                public final AnimeRecommendation[] getRecommendations() {
                    return recommendations;
                }

                @Override
                public final AnimeStatistics getStatistics() {
                    return statistics;
                }

                // additional methods

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        @SuppressWarnings("SpellCheckingInspection") // make sure matches below
        static AnimeListStatus asAnimeListStatus(final MyAnimeList mal, final JsonObject schema, final long anime_id) {
            return new AnimeListStatus() {

                private final long id               = requireNonNullElse(() -> anime_id, -1L);
                private final AnimeStatus status    = requireNonNull(() -> AnimeStatus.asEnum(schema.getString("status")));
                private final int score             = requireNonNullElse(() -> schema.getInt("score"), -1);
                private final long startDate        = requireNonNullElse(() -> parseDate(schema.getString("start_date")), -1L);
                private final long finishDate       = requireNonNullElse(() -> parseDate(schema.getString("finish_date")), -1L);
                private final Priority priority     = requireNonNull(() -> Priority.asEnum(schema.getInt("priority")));
                private final String[] tags         = requireNonNull(() -> schema.getStringArray("tags"));
                private final String comments       = requireNonNull(() -> schema.getString("comments"));
                private final long updatedAt        = requireNonNull(() -> parseISO8601(schema.getString("updated_at")));
                private final int watchedEpisodes   = requireNonNullElse(() -> schema.getInt("num_episodes_watched"), -1);
                private final boolean rewatching    = requireNonNullElse(() -> (boolean) schema.get("is_rewatching"), false);
                private final int timesRewatched    = requireNonNullElse(() -> schema.getInt("num_times_rewatched"), -1);
                private final RewatchValue rewatchValue = requireNonNull(() -> RewatchValue.asEnum(schema.getInt("rewatch_value")));

                // API methods

                @Override
                public final AnimeStatus getStatus() {
                    return status;
                }

                @Override
                public final int getScore() {
                    return score;
                }

                @Override
                public final Date getStartDate() {
                    return new Date(startDate);
                }

                @Override
                public final long getStartDateEpochMillis(){
                    return startDate;
                }

                @Override
                public final Date getFinishDate() {
                    return new Date(finishDate);
                }

                @Override
                public final long getFinishDateEpochMillis(){
                    return finishDate;
                }

                @Override
                public final Priority getPriority() {
                    return priority;
                }

                @Override
                public final String[] getTags() {
                    return tags;
                }

                @Override
                public final String getComments() {
                    return comments;
                }

                @Override
                public final Date getUpdatedAt() {
                    return new Date(updatedAt);
                }

                @Override
                public final long getUpdatedAtEpochMillis(){
                    return updatedAt;
                }

                @Override
                public final int getWatchedEpisodes() {
                    return watchedEpisodes;
                }

                @Override
                public final boolean isRewatching() {
                    return rewatching;
                }

                @Override
                public final int getTimesRewatched() {
                    return timesRewatched;
                }

                @Override
                public final RewatchValue getRewatchValue() {
                    return rewatchValue;
                }

                // additional methods

                @Override
                public final com.kttdevelopment.mal4j.anime.Anime getAnime(){
                    return mal.getAnime(id);
                }

                @Override
                public final AnimePreview getAnimePreview(){
                    return mal.getAnime(id);
                }

                @Override
                public final AnimeListUpdate edit(){
                    return mal.updateAnimeListing(id);
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        @SuppressWarnings("SpellCheckingInspection")
        static AnimeListStatus asAnimeListStatus(final MyAnimeList mal, final JsonObject schema, final AnimePreview anime) {
            return new AnimeListStatus() {

                private final AnimeStatus status    = requireNonNull(() -> AnimeStatus.asEnum(schema.getString("status")));
                private final int score             = requireNonNullElse(() -> schema.getInt("score"), -1);
                private final long startDate        = requireNonNullElse(() -> parseDate(schema.getString("start_date")), -1L);
                private final long finishDate       = requireNonNullElse(() -> parseDate(schema.getString("finish_date")), -1L);
                private final Priority priority     = requireNonNull(() -> Priority.asEnum(schema.getInt("priority")));
                private final String[] tags         = requireNonNull(() -> schema.getStringArray("tags"));
                private final String comments       = requireNonNull(() -> schema.getString("comments"));
                private final long updatedAt        = requireNonNull(() -> parseISO8601(schema.getString("updated_at")));
                private final int watchedEpisodes   = requireNonNullElse(() -> schema.getInt("num_episodes_watched"), -1);
                private final boolean rewatching    = requireNonNullElse(() -> (boolean) schema.get("is_rewatching"), false);
                private final int timesRewatched    = requireNonNullElse(() -> schema.getInt("num_times_rewatched"), -1);
                private final RewatchValue rewatchValue = requireNonNull(() -> RewatchValue.asEnum(schema.getInt("rewatch_value")));

                // API methods

                @Override
                public final AnimeStatus getStatus() {
                    return status;
                }

                @Override
                public final int getScore() {
                    return score;
                }

                @Override
                public final Date getStartDate() {
                    return new Date(startDate);
                }

                @Override
                public final long getStartDateEpochMillis(){
                    return startDate;
                }

                @Override
                public final Date getFinishDate() {
                    return new Date(finishDate);
                }

                @Override
                public final long getFinishDateEpochMillis(){
                    return finishDate;
                }

                @Override
                public final Priority getPriority() {
                    return priority;
                }

                @Override
                public final String[] getTags() {
                    return tags;
                }

                @Override
                public final String getComments() {
                    return comments;
                }

                @Override
                public final Date getUpdatedAt() {
                    return new Date(updatedAt);
                }

                @Override
                public final long getUpdatedAtEpochMillis(){
                    return updatedAt;
                }

                @Override
                public final int getWatchedEpisodes() {
                    return watchedEpisodes;
                }

                @Override
                public final boolean isRewatching() {
                    return rewatching;
                }

                @Override
                public final int getTimesRewatched() {
                    return timesRewatched;
                }

                @Override
                public final RewatchValue getRewatchValue() {
                    return rewatchValue;
                }

                // additional methods

                @Override
                public final com.kttdevelopment.mal4j.anime.Anime getAnime(){
                    return anime.getAnime();
                }

                @Override
                public final AnimePreview getAnimePreview(){
                    return anime;
                }

                @Override
                public final AnimeListUpdate edit(){
                    return mal.updateAnimeListing(anime.getID());
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static AnimePreview asAnimePreview(final MyAnimeList mal, final JsonObject schema){
            return new AnimePreview() {

                private final long id               = requireNonNullElse(() -> schema.getLong("id"), -1L);
                private final String title          = requireNonNull(() -> schema.getString("title"));
                private final Picture mainPicture   = requireNonNull(() -> Common.asPicture(mal, schema.getJsonObject("main_picture")));
                private final AlternativeTitles alternativeTitles = requireNonNull(() -> Common.asAlternativeTitles(mal, schema.getJsonObject("alternative_titles")));
                private final long startDate        = requireNonNullElse(() -> parseDate(schema.getString("start_date")), -1L);
                private final long endDate          = requireNonNullElse(() -> parseDate(schema.getString("end_date")), -1L);
                private final String synopsis       = requireNonNull(() -> schema.getString("synopsis"));
                private final float meanRating      = requireNonNullElse(() -> schema.getFloat("mean"), 0f);
                private final int rank              = requireNonNullElse(() -> schema.getInt("rank"), -1);
                private final int popularity        = requireNonNullElse(() -> schema.getInt("popularity"), -1);
                private final int usersListing      = requireNonNullElse(() -> schema.getInt("num_list_users"), -1);
                private final int usersScoring      = requireNonNullElse(() -> schema.getInt("num_scoring_users"), -1);
                private final NSFW nsfw             = requireNonNull(() -> NSFW.asEnum(schema.getString("nsfw")));
                private final Genre[] genres        = requireNonNull(() -> adaptList(schema.getJsonArray("genres"), g -> Genre.asEnum(g.getInt("id")), Genre.class));
                private final long createdAt        = requireNonNullElse(() -> parseISO8601(schema.getString("created_at")), -1L);
                private final long updatedAt        = requireNonNullElse(() -> parseISO8601(schema.getString("updated_at")), -1L);
                private final AnimeType type        = requireNonNull(() -> AnimeType.asEnum(schema.getString("media_type")));
                private final AnimeAirStatus status = requireNonNull(() -> AnimeAirStatus.asEnum(schema.getString("status")));
                private final AnimeListStatus listStatus = requireNonNull(() -> asAnimeListStatus(mal, schema.getJsonObject("my_list_status"), this));
                private final int episodes          = requireNonNullElse(() -> schema.getInt("num_episodes"), -1);
                private final StartSeason startSeason = requireNonNull(() -> asStartSeason(mal, schema.getJsonObject("start_season")));
                private final Broadcast broadcast   = requireNonNull(() -> asBroadcast(mal, schema.getJsonObject("broadcast")));
                private final AnimeSource source    = requireNonNull(() -> AnimeSource.asEnum(schema.getString("source")));
                private final int episodeLength     = requireNonNullElse(() -> schema.getInt("average_episode_duration"), -1);
                private final AnimeRating rating    = requireNonNull(() -> AnimeRating.asEnum(schema.getString("rating")));
                private final Studio[] studios      = requireNonNull(() -> adaptList(schema.getJsonArray("studios"), s -> asStudio(mal, s), Studio.class));

                // API methods

                @Override
                public final long getID() {
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
                    return new Date(startDate);
                }

                @Override
                public final long getStartDateEpochMillis(){
                    return startDate;
                }

                @Override
                public final Date getEndDate() {
                    return new Date(endDate);
                }

                @Override
                public final long getEndDateEpochMillis(){
                    return endDate;
                }

                @Override
                public final String getSynopsis() {
                    return synopsis;
                }

                @Override
                public final float getMeanRating() {
                    return meanRating;
                }

                @Override
                public final int getRank() {
                    return rank;
                }

                @Override
                public final int getPopularity() {
                    return popularity;
                }

                @Override
                public final int getUserListingCount() {
                    return usersListing;
                }

                @Override
                public final int getUserScoringCount() {
                    return usersScoring;
                }

                @Override
                public final NSFW getNSFW() {
                    return nsfw;
                }

                @Override
                public final Genre[] getGenres() {
                    return genres;
                }

                @Override
                public final long getCreatedAt() {
                    return createdAt;
                }

                @Override
                public final long getUpdatedAt() {
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
                public final int getEpisodes() {
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
                public final int getAverageEpisodeLength() {
                    return episodeLength;
                }

                @Override
                public final AnimeRating getRating() {
                    return rating;
                }

                @Override
                public final Studio[] getStudios() {
                    return studios;
                }

                // additional methods

                @Override
                public final com.kttdevelopment.mal4j.anime.Anime getAnime() {
                    return mal.getAnime(id);
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static AnimeRanking asAnimeRanking(final MyAnimeList mal, final JsonObject schema){
            return new AnimeRanking() {

                private final AnimePreview anime    = requireNonNull(() -> asAnimePreview(mal, schema.getJsonObject("node")));
                private final int ranking           = requireNonNullElse(() -> schema.getJsonObject("ranking").getInt("rank"), -1);
                private final int previousRanking   = requireNonNullElse(() -> schema.getJsonObject("ranking").getInt("previous_rank"), -1);

                // API methods

                @Override
                public final AnimePreview getAnimePreview(){
                    return anime;
                }

                @Override
                public final int getRanking(){
                    return ranking;
                }

                @Override
                public final int getPreviousRank(){
                    return previousRanking;
                }

                // additional methods

                @Override
                public final com.kttdevelopment.mal4j.anime.Anime getAnime(){
                    return mal.getAnime(anime.getID());
                }

                @Override
                public final String toString(){
                    return AutomatedToString(this);
                }
            };
        }

        static AnimeRecommendation asAnimeRecommendation(final MyAnimeList mal, final JsonObject schema){
            return new AnimeRecommendation() {

                private final AnimePreview anime    = requireNonNull(() -> asAnimePreview(mal, schema.getJsonObject("node")));
                private final int recommendations   = requireNonNullElse(() -> schema.getInt("num_recommendations"), -1);

                // API methods

                @Override
                public final AnimePreview getAnimePreview() {
                    return anime;
                }

                @Override
                public final int getRecommendations() {
                    return recommendations;
                }

                // additional methods

                @Override
                public final com.kttdevelopment.mal4j.anime.Anime getAnime() {
                    return mal.getAnime(anime.getID());
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
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
                public final com.kttdevelopment.mal4j.anime.Anime getAnime() {
                    return mal.getAnime(anime.getID());
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

    }

    @SuppressWarnings("SpellCheckingInspection")
    static abstract class Forum {

        static ForumTopicCreator asForumTopicCreator(final MyAnimeList mal, final JsonObject schema){
            return new ForumTopicCreator() {

                private final long id = requireNonNullElse(() -> schema.getLong("id"), -1L);
                private final String name = requireNonNull(() -> schema.getString("name"));

                // API methods

                @Override
                public final long getID() {
                    return id;
                }

                @Override
                public final String getName() {
                    return name;
                }

                // additional methods


                @Override
                public final com.kttdevelopment.mal4j.user.User getUser(){
                    return mal.getUser(name);
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static ForumTopicDetail asForumTopicDetail(final MyAnimeList mal, final JsonObject schema){
            return new ForumTopicDetail() {

                private final long id                       = requireNonNullElse(() -> schema.getLong("id"), -1L);
                private final String title                  = requireNonNull(() -> schema.getString("title"));
                private final long createdAt                = requireNonNullElse(() -> parseISO8601(schema.getString("created_at")), -1L);
                private final ForumTopicCreator createdBy   = requireNonNull(() -> asForumTopicCreator(mal, schema.getJsonObject("created_by")));
                private final int posts                     = requireNonNullElse(() -> schema.getInt("number_of_posts"), -1);
                private final long lastPostedAt             = requireNonNullElse(() -> parseISO8601(schema.getString("last_post_created_at")), -1L);
                private final ForumTopicCreator lastPostedBy = requireNonNull(() -> asForumTopicCreator(mal, schema.getJsonObject("last_post_created_by")));
                private final boolean locked                = requireNonNullElse(() -> (boolean) schema.get("is_locked"), false);

                // APi methods

                @Override
                public final long getID() {
                    return id;
                }

                @Override
                public final String getTitle() {
                    return title;
                }

                @Override
                public final long getCreatedAt() {
                    return createdAt;
                }

                @Override
                public final ForumTopicCreator getCreatedBy() {
                    return createdBy;
                }

                @Override
                public final int getPostsCount() {
                    return posts;
                }

                @Override
                public final long getLastPostCreatedAt() {
                    return lastPostedAt;
                }

                @Override
                public final ForumTopicCreator getLastPostCreatedBy() {
                    return lastPostedBy;
                }

                @Override
                public final boolean isLocked() {
                    return locked;
                }

                // additional methods

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static Poll asPoll(final MyAnimeList mal, final JsonObject schema, final ForumTopic forumTopic){
            return new Poll() {

                private final long id = requireNonNullElse(() -> schema.getLong("id"), -1L);
                private final String question = requireNonNull(() -> schema.getString("question"));
                private final boolean isClosed = requireNonNullElse(() -> (boolean) schema.get("close"), false);
                private final PollOption[] options = requireNonNull(() -> adaptList(schema.getJsonArray("options"), o -> asPollOption(mal, o, this), PollOption.class));

                // API methods

                @Override
                public final long getID() {
                    return id;
                }

                @Override
                public final String getQuestion() {
                    return question;
                }

                @Override
                public final boolean isClosed() {
                    return isClosed;
                }

                @Override
                public final PollOption[] getOptions() {
                    return options;
                }

                // additional methods

                @Override
                public final ForumTopic getForumTopic() {
                    return forumTopic;
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static PollOption asPollOption(final MyAnimeList mal, final JsonObject schema, final Poll poll){
            return new PollOption() {

                private final long id       = requireNonNullElse(() -> schema.getLong("id"), -1L);
                private final String text   = requireNonNull(() -> schema.getString("text"));
                private final int votes     = requireNonNullElse(() -> schema.getInt("votes"), -1);

                // API methods

                @Override
                public final long getID() {
                    return 0;
                }

                @Override
                public final String getText() {
                    return text;
                }

                @Override
                public final int getVotes() {
                    return votes;
                }

                // additional methods

                @Override
                public final Poll getPoll() {
                    return poll;
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static PostAuthor asPostAuthor(final MyAnimeList mal, final JsonObject schema){
            return new PostAuthor() {

                private final long id               = requireNonNullElse(() -> schema.getLong("id"), -1L);
                private final String name           = requireNonNull(() -> schema.getString("name"));
                private final String forumAvatarURL = requireNonNull(() -> schema.getString("forum_avator"));

                // API methods

                @Override
                public final long getID() {
                    return id;
                }

                @Override
                public final String getName() {
                    return name;
                }

                @Override
                public final String getForumAvatarURL() {
                    return forumAvatarURL;
                }

                // additional methods

                @Override
                public final com.kttdevelopment.mal4j.user.User getUser() {
                    return mal.getUser(name);
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static ForumBoard asForumBoard(final MyAnimeList mal, final JsonObject schema, final ForumCategory forumCategory){
            return new ForumBoard() {

                private final long id                   = requireNonNullElse(() -> schema.getLong("id"), -1L);
                private final String title              = requireNonNull(() -> schema.getString("title"));
                private final String description        = requireNonNull(() -> schema.getString("description"));
                private final ForumSubBoard[] subBoards = adaptList(schema.getJsonArray("subboards"), b -> asForumSubBoard(mal, b, this), ForumSubBoard.class);

                // API methods

                @Override
                public final long getID() {
                    return id;
                }

                @Override
                public final String getTitle() {
                    return title;
                }

                @Override
                public final String getDescription() {
                    return description;
                }

                @Override
                public final ForumSubBoard[] getSubBoards() {
                    return subBoards;
                }

                // additional methods

                @Override
                public final ForumCategory getCategory() {
                    return forumCategory;
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static ForumCategory asForumCategory(final MyAnimeList mal, final JsonObject schema){
            return new ForumCategory() {

                private final String title              = requireNonNull(() -> schema.getString("title"));
                private final ForumBoard[] forumBoards  = adaptList(schema.getJsonArray("boards"), b -> asForumBoard(mal, b, this), ForumBoard.class);

                // API methods

                @Override
                public final String getTitle() {
                    return title;
                }

                @Override
                public final ForumBoard[] getForumBoards() {
                    return forumBoards;
                }

                // additional methods

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static ForumSubBoard asForumSubBoard(final MyAnimeList mal, final JsonObject schema, final ForumBoard forumBoard){
            return new ForumSubBoard() {

                private final long id       = requireNonNullElse(() -> schema.getLong("id"), -1L);
                private final String title  = requireNonNull(() -> schema.getString("title"));

                // API methods

                @Override
                public final long getID() {
                    return id;
                }

                @Override
                public final String getTitle() {
                    return title;
                }

                // additional methods

                @Override
                public final ForumBoard getBoard() {
                    return forumBoard;
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static ForumTopic asForumTopic(final MyAnimeList mal, final JsonObject schema){
            return new ForumTopic() {

                private final String title = requireNonNull(() -> schema.getString("title"));
                private final Post[] posts = requireNonNull(() -> adaptList(schema.getJsonArray("posts"), p -> asPost(mal, p, this), Post.class));
                private final Poll poll = requireNonNull(() -> asPoll(mal, schema.getJsonObject("poll"), this));

                // API methods

                @Override
                public final String getTitle() {
                    return title;
                }

                @Override
                public final Post[] getPosts() {
                    return posts;
                }

                @Override
                public final Poll getPoll() {
                    return poll;
                }

                // additional methods

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static Post asPost(final MyAnimeList mal, final JsonObject schema, final ForumTopic forumTopic){
            return new Post() {

                private final long id           = requireNonNullElse(() -> schema.getLong("id"), -1L);
                private final int number        = requireNonNullElse(() -> schema.getInt("number"), -1);
                private final long createdAt    = requireNonNullElse(() -> parseISO8601(schema.getString("created_at")), -1L);
                private final PostAuthor author = requireNonNull(() -> asPostAuthor(mal, schema.getJsonObject("created_by")));
                private final String body       = requireNonNull(() -> schema.getString("body"));
                private final String signature  = requireNonNull(() -> schema.getString("signature"));

                // API methods

                @Override
                public final long getID() {
                    return id;
                }

                @Override
                public final int getNumber() {
                    return number;
                }

                @Override
                public final long getCreatedAt() {
                    return createdAt;
                }

                @Override
                public final PostAuthor getAuthor() {
                    return author;
                }

                @Override
                public final String getBody() {
                    return body;
                }

                @Override
                public final String getSignature() {
                    return signature;
                }

                // additional methods

                @Override
                public final ForumTopic getForumTopic() {
                    return forumTopic;
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

    }

    static abstract class Manga {

        static Author asAuthor(final MyAnimeList mal, final JsonObject schema){
            return new Author() {

                private final long id           = requireNonNullElse(() -> schema.getJsonObject("node").getLong("id"), -1L);
                private final String firstName  = requireNonNull(() -> schema.getJsonObject("node").getString("first_name"));
                private final String lastName   = requireNonNull(() -> schema.getJsonObject("node").getString("last_name"));
                private final String role       = requireNonNull(() -> schema.getString("role"));

                // API methods

                @Override
                public final long getID() {
                    return id;
                }

                @Override
                public final String getFirstName() {
                    return firstName;
                }

                @Override
                public final String getLastName() {
                    return lastName;
                }

                @Override
                public final String getRole() {
                    return role;
                }

                // additional methods

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static Publisher asPublisher(final MyAnimeList mal, final JsonObject schema){
            return new Publisher() {

                private final long id       = requireNonNullElse(() -> schema.getJsonObject("node").getLong("id"), -1L);
                private final String name   = requireNonNull(() -> schema.getJsonObject("node").getString("name"));
                private final String role   = requireNonNull(() -> schema.getString("role"));

                // API methods

                @Override
                public final long getID() {
                    return id;
                }

               @Override
                public final String getName() {
                    return name;
                }

                @Override
                public final String getRole() {
                    return role;
                }

                // additional methods

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static com.kttdevelopment.mal4j.manga.Manga asManga(final MyAnimeList mal, final JsonObject schema){
            return new com.kttdevelopment.mal4j.manga.Manga() {

                private final long id               = requireNonNullElse(() -> schema.getLong("id"), -1L);
                private final String title          = requireNonNull(() -> schema.getString("title"));
                private final Picture mainPicture   = requireNonNull(() -> Common.asPicture(mal, schema.getJsonObject("main_picture")));
                private final AlternativeTitles alternativeTitles = requireNonNull(() -> Common.asAlternativeTitles(mal, schema.getJsonObject("alternative_titles")));
                private final long startDate        = requireNonNullElse(() -> parseDate(schema.getString("start_date")), -1L);
                private final long endDate          = requireNonNullElse(() -> parseDate(schema.getString("end_date")), -1L);
                private final String synopsis       = requireNonNull(() -> schema.getString("synopsis"));
                private final float meanRating      = requireNonNullElse(() -> schema.getFloat("mean"), 0F);
                private final int rank              = requireNonNullElse(() -> schema.getInt("rank"), -1);
                private final int popularity        = requireNonNullElse(() -> schema.getInt("popularity"), -1);
                private final int usersListing      = requireNonNullElse(() -> schema.getInt("num_list_users"), -1);
                private final int usersScoring      = requireNonNullElse(() -> schema.getInt("num_scoring_users"), -1);
                private final NSFW nsfw             = requireNonNull(() -> NSFW.asEnum(schema.getString("nsfw")));
                private final Genre[] genres        = requireNonNull(() -> adaptList(schema.getJsonArray("genres"), g -> Genre.asEnum(g.getInt("id")), Genre.class));
                private final long createdAt        = requireNonNullElse(() -> parseISO8601(schema.getString("created_at")), -1L);
                private final long updatedAt        = requireNonNullElse(() -> parseISO8601(schema.getString("updated_at")), -1L);
                private final MangaType type        = requireNonNull(() -> MangaType.asEnum(schema.getString("media_type")));
                private final MangaPublishStatus status = requireNonNull(() -> MangaPublishStatus.asEnum(schema.getString("status")));
                private final MangaListStatus listStatus = requireNonNull(() -> asMangaListStatus(mal, schema.getJsonObject("my_list_status"), this));
                private final int volumes           = requireNonNullElse(() -> schema.getInt("num_volumes"), -1);
                private final int chapters          = requireNonNullElse(() -> schema.getInt("num_chapters"), -1);
                private final Author[] authors      = requireNonNull(() -> adaptList(schema.getJsonArray("authors"), a -> asAuthor(mal, a), Author.class));
                private final Picture[] pictures    = requireNonNull(() -> adaptList(schema.getJsonArray("pictures"), p -> Common.asPicture(mal, p), Picture.class));
                private final String background     = requireNonNull(() -> schema.getString("background"));
                private final RelatedAnime[] relatedAnime = requireNonNull(() -> adaptList(schema.getJsonArray("related_anime"), a -> Anime.asRelatedAnime(mal, a), RelatedAnime.class));
                private final RelatedManga[] relatedManga = requireNonNull(() -> adaptList(schema.getJsonArray("related_manga"), m -> asRelatedManga(mal, m), RelatedManga.class));
                private final MangaRecommendation[] recommendations = requireNonNull(() -> adaptList(schema.getJsonArray("recommendations"), r -> asMangaRecommendation(mal, r), MangaRecommendation.class));
                private final Publisher[] serialization   = requireNonNull(() -> adaptList(schema.getJsonArray("serialization"), s -> asPublisher(mal, s), Publisher.class));

                // API methods

                @Override
                public final long getID() {
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
                    return new Date(startDate);
                }

                @Override
                public final long getStartDateEpochMillis(){
                    return startDate;
                }

                @Override
                public final Date getEndDate() {
                    return new Date(endDate);
                }

                @Override
                public final long getEndDateEpochMillis(){
                    return endDate;
                }

                @Override
                public final String getSynopsis() {
                    return synopsis;
                }

                @Override
                public final float getMeanRating() {
                    return meanRating;
                }

                @Override
                public final int getRank() {
                    return rank;
                }

                @Override
                public final int getPopularity() {
                    return popularity;
                }

                @Override
                public final int getUserListingCount() {
                    return usersListing;
                }

                @Override
                public final int getUserScoringCount() {
                    return usersScoring;
                }

                @Override
                public final NSFW getNSFW() {
                    return nsfw;
                }

                @Override
                public final Genre[] getGenres() {
                    return genres;
                }

                @Override
                public final long getCreatedAt() {
                    return createdAt;
                }

                @Override
                public final long getUpdatedAt() {
                    return updatedAt;
                }

                @Override
                public final MangaType getType() {
                    return type;
                }

                @Override
                public final MangaPublishStatus getStatus() {
                    return status;
                }

                @Override
                public final MangaListStatus getListStatus() {
                    return listStatus;
                }

                @Override
                public final int getVolumes() {
                    return volumes;
                }

                @Override
                public final int getChapters() {
                    return chapters;
                }

                @Override
                public final Author[] getAuthors() {
                    return authors;
                }

                @Override
                public final Picture[] getPictures() {
                    return pictures;
                }

                @Override
                public final String getBackground() {
                    return background;
                }

                @Override
                public final RelatedAnime[] getRelatedAnime() {
                    return relatedAnime;
                }

                @Override
                public final RelatedManga[] getRelatedManga() {
                    return relatedManga;
                }

                @Override
                public final MangaRecommendation[] getRecommendations() {
                    return recommendations;
                }

                @Override
                public final Publisher[] getSerialization() {
                    return serialization;
                }

                // additional methods

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        // make sure matches below
        static MangaListStatus asMangaListStatus(final MyAnimeList mal, final JsonObject schema, final long manga_id){
            return new MangaListStatus() {

                private final long id               = requireNonNullElse(() -> manga_id, -1L);
                private final MangaStatus status    = requireNonNull(() -> MangaStatus.asEnum(schema.getString("status")));
                private final int score             = requireNonNullElse(() -> schema.getInt("score"), -1);
                private final long startDate        = requireNonNullElse(() -> parseDate(schema.getString("start_date")), -1L);
                private final long finishDate       = requireNonNullElse(() -> parseDate(schema.getString("finish_date")), -1L);
                private final Priority priority     = requireNonNull(() -> Priority.asEnum(schema.getInt("priority")));
                private final String[] tags         = requireNonNull(() -> schema.getStringArray("tags"));
                private final String comments       = requireNonNull(() -> schema.getString("comments"));
                private final long updatedAt        = requireNonNull(() -> parseISO8601(schema.getString("updated_at")));
                private final int volumesRead       = requireNonNullElse(() -> schema.getInt("num_volumes_read"), -1);
                private final int chaptersRead      = requireNonNullElse(() -> schema.getInt("num_chapters_read"), -1);
                private final boolean rereading     = requireNonNullElse(() -> (boolean) schema.get("is_rereading"), false);
                private final int timesReread       = requireNonNullElse(() -> schema.getInt("num_times_reread"), -1);
                private final RereadValue rereadValue  = requireNonNull(() -> RereadValue.asEnum(schema.getInt("reread_value")));

                // API methods

                @Override
                public final MangaStatus getStatus() {
                    return status;
                }

                @Override
                public final int getScore() {
                    return score;
                }

                @Override
                public final Date getStartDate() {
                    return new Date(startDate);
                }

                @Override
                public final long getStartDateEpochMillis(){
                    return startDate;
                }

                @Override
                public final Date getFinishDate() {
                    return new Date(finishDate);
                }

                @Override
                public final long getFinishDateEpochMillis(){
                    return finishDate;
                }

                @Override
                public final Priority getPriority() {
                    return priority;
                }

                @Override
                public final String[] getTags() {
                    return tags;
                }

                @Override
                public final String getComments() {
                    return comments;
                }

                @Override
                public final Date getUpdatedAt() {
                    return new Date(updatedAt);
                }

                @Override
                public final long getUpdatedAtEpochMillis(){
                    return updatedAt;
                }

                @Override
                public final int getVolumesRead() {
                    return volumesRead;
                }

                @Override
                public final int getChaptersRead() {
                    return chaptersRead;
                }

                @Override
                public final boolean isRereading() {
                    return rereading;
                }

                @Override
                public final int getTimesReread() {
                    return timesReread;
                }

                @Override
                public final RereadValue getRereadValue() {
                    return rereadValue;
                }

                // additional methods

                @Override
                public final com.kttdevelopment.mal4j.manga.Manga getManga(){
                    return mal.getManga(id);
                }

                @Override
                public final MangaPreview getMangaPreview(){
                    return mal.getManga(id);
                }

                @Override
                public final MangaListUpdate edit(){
                    return mal.updateMangaListing(id);
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static MangaListStatus asMangaListStatus(final MyAnimeList mal, final JsonObject schema, final MangaPreview manga){
            return new MangaListStatus() {

                private final MangaStatus status    = requireNonNull(() -> MangaStatus.asEnum(schema.getString("status")));
                private final int score             = requireNonNullElse(() -> schema.getInt("score"), -1);
                private final long startDate        = requireNonNullElse(() -> parseDate(schema.getString("start_date")), -1L);
                private final long finishDate       = requireNonNullElse(() -> parseDate(schema.getString("finish_date")), -1L);
                private final Priority priority     = requireNonNull(() -> Priority.asEnum(schema.getInt("priority")));
                private final String[] tags         = requireNonNull(() -> schema.getStringArray("tags"));
                private final String comments       = requireNonNull(() -> schema.getString("comments"));
                private final long updatedAt        = requireNonNull(() -> parseISO8601(schema.getString("updated_at")));
                private final int volumesRead       = requireNonNullElse(() -> schema.getInt("num_volumes_read"), -1);
                private final int chaptersRead      = requireNonNullElse(() -> schema.getInt("num_chapters_read"), -1);
                private final boolean rereading     = requireNonNullElse(() -> (boolean) schema.get("is_rereading"), false);
                private final int timesReread       = requireNonNullElse(() -> schema.getInt("num_times_reread"), -1);
                private final RereadValue rereadValue = requireNonNull(() -> RereadValue.asEnum(schema.getInt("reread_value")));

                // API methods

                @Override
                public final MangaStatus getStatus() {
                    return status;
                }

                @Override
                public final int getScore() {
                    return score;
                }

                @Override
                public final Date getStartDate() {
                    return new Date(startDate);
                }

                @Override
                public final long getStartDateEpochMillis(){
                    return startDate;
                }

                @Override
                public final Date getFinishDate() {
                    return new Date(finishDate);
                }

                @Override
                public final long getFinishDateEpochMillis(){
                    return finishDate;
                }

                @Override
                public final Priority getPriority() {
                    return priority;
                }

                @Override
                public final String[] getTags() {
                    return tags;
                }

                @Override
                public final String getComments() {
                    return comments;
                }

                @Override
                public final Date getUpdatedAt() {
                    return new Date(updatedAt);
                }

                @Override
                public final long getUpdatedAtEpochMillis(){
                    return updatedAt;
                }

                @Override
                public final int getVolumesRead() {
                    return volumesRead;
                }

                @Override
                public final int getChaptersRead() {
                    return chaptersRead;
                }

                @Override
                public final boolean isRereading() {
                    return rereading;
                }

                @Override
                public final int getTimesReread() {
                    return timesReread;
                }

                @Override
                public final RereadValue getRereadValue() {
                    return rereadValue;
                }

                // additional methods

                @Override
                public final com.kttdevelopment.mal4j.manga.Manga getManga(){
                    return manga.getManga();
                }

                @Override
                public final MangaPreview getMangaPreview(){
                    return manga;
                }

                @Override
                public final MangaListUpdate edit(){
                    return mal.updateMangaListing(manga.getID());
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static MangaPreview asMangaPreview(final MyAnimeList mal, final JsonObject schema){
            return new MangaPreview() {

                private final long id               = requireNonNullElse(() -> schema.getLong("id"), -1L);
                private final String title          = requireNonNull(() -> schema.getString("title"));
                private final Picture mainPicture   = requireNonNull(() -> Common.asPicture(mal, schema.getJsonObject("main_picture")));
                private final AlternativeTitles alternativeTitles = requireNonNull(() -> Common.asAlternativeTitles(mal, schema.getJsonObject("alternative_titles")));
                private final long startDate        = requireNonNullElse(() -> parseDate(schema.getString("start_date")), -1L);
                private final long endDate          = requireNonNullElse(() -> parseDate(schema.getString("end_date")), -1L);
                private final String synopsis       = requireNonNull(() -> schema.getString("synopsis"));
                private final float meanRating      = requireNonNullElse(() -> schema.getFloat("mean"), 0F);
                private final int rank              = requireNonNullElse(() -> schema.getInt("rank"), -1);
                private final int popularity        = requireNonNullElse(() -> schema.getInt("popularity"), -1);
                private final int usersListing      = requireNonNullElse(() -> schema.getInt("num_list_users"), -1);
                private final int usersScoring      = requireNonNullElse(() -> schema.getInt("num_scoring_users"), -1);
                private final NSFW nsfw             = requireNonNull(() -> NSFW.asEnum(schema.getString("nsfw")));
                private final Genre[] genres        = requireNonNull(() -> adaptList(schema.getJsonArray("genres"), g -> Genre.asEnum(g.getInt("id")), Genre.class));
                private final long createdAt        = requireNonNullElse(() -> parseISO8601(schema.getString("created_at")), -1L);
                private final long updatedAt        = requireNonNullElse(() -> parseISO8601(schema.getString("updated_at")), -1L);
                private final MangaType type        = requireNonNull(() -> MangaType.asEnum(schema.getString("media_type")));
                private final MangaPublishStatus status = requireNonNull(() -> MangaPublishStatus.asEnum(schema.getString("status")));
                private final MangaListStatus listStatus = requireNonNull(() -> asMangaListStatus(mal, schema.getJsonObject("my_list_status"), this));
                private final int volumes           = requireNonNullElse(() -> schema.getInt("num_volumes"), -1);
                private final int chapters          = requireNonNullElse(() -> schema.getInt("num_chapters"), -1);
                private final Author[] authors      = requireNonNull(() -> adaptList(schema.getJsonArray("authors"), a -> asAuthor(mal, a), Author.class));

                // API methods

                @Override
                public final long getID() {
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
                    return new Date(startDate);
                }

                @Override
                public final long getStartDateEpochMillis(){
                    return startDate;
                }

                @Override
                public final Date getEndDate() {
                    return new Date(endDate);
                }

                @Override
                public final long getEndDateEpochMillis(){
                    return endDate;
                }

                @Override
                public final String getSynopsis() {
                    return synopsis;
                }

                @Override
                public final float getMeanRating() {
                    return meanRating;
                }

                @Override
                public final int getRank() {
                    return rank;
                }

                @Override
                public final int getPopularity() {
                    return popularity;
                }

                @Override
                public final int getUserListingCount() {
                    return usersListing;
                }

                @Override
                public final int getUserScoringCount() {
                    return usersScoring;
                }

                @Override
                public final NSFW getNSFW() {
                    return nsfw;
                }

                @Override
                public final Genre[] getGenres() {
                    return genres;
                }

                @Override
                public final long getCreatedAt() {
                    return createdAt;
                }

                @Override
                public final long getUpdatedAt() {
                    return updatedAt;
                }

                @Override
                public final MangaType getType() {
                    return type;
                }

                @Override
                public final MangaPublishStatus getStatus() {
                    return status;
                }

                @Override
                public final MangaListStatus getListStatus() {
                    return listStatus;
                }

                @Override
                public final int getVolumes() {
                    return volumes;
                }

                @Override
                public final int getChapters() {
                    return chapters;
                }

                @Override
                public final Author[] getAuthors() {
                    return authors;
                }

                // additional methods

                @Override
                public final com.kttdevelopment.mal4j.manga.Manga getManga() {
                    return mal.getManga(id);
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static MangaRanking asMangaRanking(final MyAnimeList mal, final JsonObject schema){
            return new MangaRanking() {

                private final MangaPreview manga    = requireNonNull(() -> asMangaPreview(mal, schema.getJsonObject("node")));
                private final int ranking           = requireNonNullElse(() -> schema.getJsonObject("ranking").getInt("rank"), -1);
                private final int previousRanking   = requireNonNullElse(() -> schema.getJsonObject("ranking").getInt("previous_rank"), -1);

                // API method

                @Override
                public final MangaPreview getMangaPreview() {
                    return manga;
                }

                @Override
                public final int getRanking() {
                    return ranking;
                }

                @Override
                public final int getPreviousRank() {
                    return previousRanking;
                }

                // API methods

                @Override
                public final com.kttdevelopment.mal4j.manga.Manga getManga() {
                    return mal.getManga(manga.getID());
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static MangaRecommendation asMangaRecommendation(final MyAnimeList mal, final JsonObject schema){
            return new MangaRecommendation() {

                private final MangaPreview manga    = requireNonNull(() -> asMangaPreview(mal, schema.getJsonObject("node")));
                private final int recommendations   = requireNonNullElse(() -> schema.getInt("num_recommendations"), -1);

                // API methods

                @Override
                public final MangaPreview getMangaPreview() {
                    return manga;
                }

                @Override
                public final int getRecommendations() {
                    return recommendations;
                }

                // additional methods

                @Override
                public final com.kttdevelopment.mal4j.manga.Manga getManga() {
                    return mal.getManga(manga.getID());
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static RelatedManga asRelatedManga(final MyAnimeList mal, final JsonObject schema){
            return new RelatedManga() {

                private final MangaPreview manga            = requireNonNull(() -> asMangaPreview(mal, schema.getJsonObject("node")));
                private final RelationType relationType     = requireNonNull(() -> RelationType.asEnum(schema.getString("relation_type")));
                private final String relationTypeFormatted  = requireNonNull(() -> schema.getString("relation_type_formatted"));

                // API methods

                @Override
                public final MangaPreview getMangaPreview() {
                    return manga;
                }

                @Override
                public final RelationType getRelationType() {
                    return relationType;
                }

                @Override
                public final String getRelationTypeFormat() {
                    return relationTypeFormatted;
                }

                // API methods

                @Override
                public final com.kttdevelopment.mal4j.manga.Manga getManga() {
                    return mal.getManga(manga.getID());
                }

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

    }

    static abstract class User {

        static com.kttdevelopment.mal4j.user.User asUser(final MyAnimeList mal, final JsonObject schema){
            return new com.kttdevelopment.mal4j.user.User() {

                private final long id           = requireNonNullElse(() -> schema.getLong("id"), -1L);
                private final String name       = requireNonNull(() -> schema.getString("name"));
                private final String picture    = requireNonNull(() -> schema.getString("picture"));
                private final String gender     = requireNonNull(() -> schema.getString("gender"));
                private final long birthday     = requireNonNullElse(() -> parseDate(schema.getString("birthday")), -1L);
                private final String location   = requireNonNull(() -> schema.getString("location"));
                private final long joinedAt     = requireNonNullElse(() -> parseISO8601(schema.getString("joined_at")), -1L);
                private final UserAnimeStatistics animeStatistics = requireNonNull(() -> asUserAnimeStatistics(mal, schema.getJsonObject("anime_statistics")));
                private final String timezone   = requireNonNull(() -> schema.getString("time_zone"));
                private final boolean supporter = requireNonNullElse(() -> (boolean) schema.get("is_supporter"), false);

                // API methods

                @Override
                public final long getID(){
                    return id;
                }

                @Override
                public final String getName(){
                    return name;
                }

                @Override
                public final String getPictureURL(){
                    return picture;
                }

                @Override
                public final String getGender(){
                    return gender;
                }

                @Override
                public final long getBirthday(){
                    return birthday;
                }

                @Override
                public final String getLocation(){
                    return location;
                }

                @Override
                public final Date getJoinedAt(){
                    return new Date(joinedAt);
                }

                @Override
                public final long getJoinedAtEpochMillis(){
                    return joinedAt;
                }

                @Override
                public final UserAnimeStatistics getAnimeStatistics(){
                    return animeStatistics;
                }

                @Override
                public final String getTimeZone(){
                    return timezone;
                }

                @Override
                public final boolean isSupporter(){
                    return supporter;
                }

                // additional methods

                @Override
                public final String toString(){
                    return AutomatedToString(this);
                }

            };
        }

        static UserAnimeStatistics asUserAnimeStatistics(final MyAnimeList mal, final JsonObject schema){
            return new UserAnimeStatistics() {

                private final int watching          = requireNonNullElse(() -> schema.getInt("num_items_watching"), -1);
                private final int completed         = requireNonNullElse(() -> schema.getInt("num_items_completed"), -1);
                private final int onHold            = requireNonNullElse(() -> schema.getInt("num_items_on_hold"), -1);
                private final int dropped           = requireNonNullElse(() -> schema.getInt("num_items_dropped"), -1);
                private final int planToWatch       = requireNonNullElse(() -> schema.getInt("num_items_plan_to_watch"), -1);
                private final int items             = requireNonNullElse(() -> schema.getInt("num_items"), -1);
                private final float daysWatching    = requireNonNullElse(() -> schema.getFloat("num_days_watching"), 0f);
                private final float daysCompleted   = requireNonNullElse(() -> schema.getFloat("num_days_completed"), 0f);
                private final float daysOnHold      = requireNonNullElse(() -> schema.getFloat("num_days_on_hold"), 0f);
                private final float daysDropped     = requireNonNullElse(() -> schema.getFloat("num_days_dropped"), 0f);
                private final float days            = requireNonNullElse(() -> schema.getFloat("num_days"), 0f);
                private final int episodesWatched   = requireNonNullElse(() -> schema.getInt("num_episodes"), -1);
                @SuppressWarnings("SpellCheckingInspection")
                private final int timesRewatched    = requireNonNullElse(() -> schema.getInt("num_times_rewatched"), -1);
                private final float meanScore       = requireNonNullElse(() -> schema.getFloat("mean_score"), 0f);

                // API methods

                @Override
                public final int getItemsWatching(){
                    return watching;
                }

                @Override
                public final int getItemsCompleted(){
                    return completed;
                }

                @Override
                public final int getItemsOnHold(){
                    return onHold;
                }

                @Override
                public final int getItemsDropped(){
                    return dropped;
                }

                @Override
                public final int getItemsPlanToWatch(){
                    return planToWatch;
                }

                @Override
                public final int getItems(){
                    return items;
                }

                @Override
                public final float getDaysWatched(){
                    return days;
                }

                @Override
                public final float getDaysWatching(){
                    return daysWatching;
                }

                @Override
                public final float getDaysCompleted(){
                    return daysCompleted;
                }

                @Override
                public final float getDaysOnHold(){
                    return daysOnHold;
                }

                @Override
                public final float getDaysDropped(){
                    return daysDropped;
                }

                @Override
                public final float getDays(){
                    return days;
                }

                @Override
                public final int getEpisodes(){
                    return episodesWatched;
                }

                @SuppressWarnings("SpellCheckingInspection")
                @Override
                public final int getTimesRewatched(){
                    return timesRewatched;
                }

                @Override
                public final float getMeanScore(){
                    return meanScore;
                }

                // additional methods

                @Override
                public final String toString(){
                    return AutomatedToString(this);
                }

            };
        }

    }

    static abstract class Common {

        static AlternativeTitles asAlternativeTitles(final MyAnimeList mal, final JsonObject schema){
            return new AlternativeTitles() {

                private final String[] synonyms = requireNonNull(() -> schema.getStringArray("synonyms"));
                private final String english    = requireNonNull(() -> schema.getString("en"));
                private final String japanese   = requireNonNull(() -> schema.getString("ja"));

                // API methods

                @Override
                public final String[] getSynonyms() {
                    return synonyms;
                }

                @Override
                public final String getEnglish() {
                    return english;
                }

                @Override
                public final String getJapanese() {
                    return japanese;
                }

                //

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

        static Picture asPicture(final MyAnimeList mal, final JsonObject schema){
            return new Picture() {

                private final String medium = requireNonNull(() -> schema.getString("medium"));
                private final String large  = requireNonNull(() -> schema.getString("large"));

                // API methods

                @Override
                public final String getMediumURL() {
                    return medium;
                }

                @Override
                public final String getLargeURL() {
                    return large;
                }

                // additional methods

                @Override
                public final String toString() {
                    return AutomatedToString(this);
                }

            };
        }

    }

    //

    private static <R> R[] adaptList(final JsonObject[] list, final Function<JsonObject,R> adapter, final Class<R> Class){
        final List<R> li = new ArrayList<>();
        for(final JsonObject o : list)
            li.add(adapter.apply(o));

        final R[] array = (R[]) Array.newInstance(Class, list.length);

        final int len = array.length;
        for(int i = 0; i < len; i++)
            array[i] = li.get(i);

        return array;
    }

    static final String YMD = "yyyy-MM-dd";
    static final String YM  = "yyyy-MM";
    static final String Y   = "yyyy";

    private static long parseDate(final String date){
        if(date == null) return -1;

        final int len = date.length();
        final DateFormat df = new SimpleDateFormat(len == 10 ? YMD : len == 7 ? YM : Y);

        try{
            return df.parse(date).getTime();
        }catch(final ParseException ignored){
            return -1;
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ssXXX";

    private static long parseISO8601(final String timestamp){
        if(timestamp == null) return -1;

        try{
            return new SimpleDateFormat(ISO8601).parse(timestamp).getTime();
        }catch(final ParseException ignored){
            return -1;
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static Time asTime(final String time){
        final String[] hhmm = time.split(":");
        final int h         = Integer.parseInt(hhmm[0]);
        final int m         = Integer.parseInt(hhmm[1]);

        return new Time() {

            private final int hour   = h;
            private final int hour12 = h > 12 ? h - 12 : h == 0 ? 12 : h;
            private final boolean am = hour <= 12;
            private final int minute = m;

            @Override
            public final int getHour() {
                return hour;
            }

            @Override
            public final int get12Hour() {
                return hour12;
            }

            @Override
            public final boolean isAM() {
                return am;
            }

            @Override
            public final boolean isPM() {
                return !am;
            }

            @Override
            public final int getMinute() {
                return minute;
            }

            //

            @Override
            public final String toString() {
                return AutomatedToString(this);
            }

        };
    }

    //

    private static <T> T requireNonNull(final Supplier<T> obj){
        return requireNonNullElse(obj, null);
    }

    private static <T> T requireNonNullElse(final Supplier<T> obj, T defaultObj){
        try{
            final T response = obj.get();
            return response != null ? response : defaultObj;
        }catch (final NullPointerException ignored){
            return defaultObj;
        }
    }

    private static String AutomatedToString(final Object obj){
        final Class<?> _class = obj.getClass();

        final List<Field[]> fieldSets = new ArrayList<>();
        fieldSets.add(_class.getDeclaredFields());

        Class<?> _super = _class; // add inherited fields
        while((_super = _super.getSuperclass()) != null)
            fieldSets.add(_super.getDeclaredFields());

        Collections.reverse(fieldSets);

        final StringBuilder OUT = new StringBuilder();
        OUT.append(_class.getSimpleName()).append('{');
        for(final Field[] set : fieldSets){ // print all fields
            for(final Field field : set){
                if(!field.getName().contains("$"))
                    try{
                        final Object value = field.get(obj);
                        OUT.append(field.getName()).append('=');
                        if(value instanceof String)
                            OUT.append('\'').append(value).append('\'');
                        else if(value instanceof Object[])
                            OUT.append(Arrays.toString((Object[]) value));
                        else
                            OUT.append(value);
                        OUT.append(", ");
                    }catch(final IllegalAccessException ignored){ }
            }
        }

        if(OUT.toString().contains(", "))
            OUT.delete(OUT.lastIndexOf(", "), OUT.length());
        OUT.append('}');
        return OUT.toString();
    }

}
