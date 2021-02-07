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

/**
 *
 *
 * @since 1.1.0
 * @version 1.0.0
 * @author Ktt Development
 */
public abstract class Fields {

    public static final String[] NO_FIELDS = new String[0];

    // shared

    static class Common {

        public static final String id = "id";

        public static final String title = "title";

        public static final String main_picture = "main_picture";

        public static final String alternative_titles = "alternative_titles";

        public static final String start_date = "start_date";

        public static final String end_date = "end_date";

        public static final String synopsis = "synopsis";

        public static final String mean = "mean";

        public static final String rank = "rank";

        public static final String popularity = "popularity";

        public static final String list_users = "num_list_users";

        public static final String scoring_users = "num_scoring_users";

        public static final String nsfw = "nsfw";

        public static final String created_at = "created_at";

        public static final String updated_at = "updated_at";

        public static final String media_type = "media_type";

        public static final String status = "status";

        public static final String genres = "genres";

        public static final String pictures = "pictures";

        public static final String background = "background";

        public static final String related_anime = "related_anime";

        public static final String related_manga = "related_manga";

        public static final String recommendations = "recommendations";

        static class ListStatus {

            public static final String start_date = "start_date";

            public static final String finish_date = "finish_date";

            public static final String priority = "priority";

            public static final String tags = "tags";

            public static final String comments = "comment";

        }

    }

    // anime

    public static class Anime extends Common {

        public static final String episodes = "num_episodes";

        public static final String start_season = "start_season";

        public static final String broadcast = "broadcast";

        public static final String source = "source";

        public static final String average_episode_duration = "average_episode_duration";

        public static final String rating = "rating";

        public static final String studios = "studios";

        public static final String statistics = "statistics";

        public static final String opening_themes = "opening_themes";

        public static final String ending_themes = "ending_themes";

        public static final String list_status = list_status(
            Common.ListStatus.start_date,
            Common.ListStatus.finish_date,
            Common.ListStatus.priority,
            Common.ListStatus.tags,
            Common.ListStatus.comments,
            ListStatus.times_rewatched,
            ListStatus.rewatch_value
        );

        @SuppressWarnings("SpellCheckingInspection")
        public static class ListStatus extends Common.ListStatus {

            public static final String times_rewatched = "num_times_rewatched";

            public static final String rewatch_value = "rewatch_value";

        }

        public static class MyListStatus extends ListStatus { }

        public static String list_status(final String... fields){
            return "list_status{" + String.join(",", fields == null ? new String[0] : fields) + '}';
        }

        public static final String my_list_status = "my_" + list_status;

        public static String my_list_status(final String... fields){
            return "my_" + list_status(fields);
        }

    }

    public static final String anime = String.join(",",
        Anime.id,
        Anime.title,
        Anime.main_picture,
        Anime.alternative_titles,
        Anime.start_date,
        Anime.end_date,
        Anime.synopsis,
        Anime.mean,
        Anime.rank,
        Anime.popularity,
        Anime.list_users,
        Anime.scoring_users,
        Anime.nsfw,
        Anime.created_at,
        Anime.updated_at,
        Anime.media_type,
        Anime.status,
        Anime.genres,
        Anime.pictures,
        Anime.background,
        Anime.related_anime,
        Anime.related_manga,
        Anime.recommendations,
        Anime.episodes,
        Anime.start_season,
        Anime.broadcast,
        Anime.source,
        Anime.average_episode_duration,
        Anime.rating,
        Anime.studios,
        Anime.statistics,
        Anime.opening_themes,
        Anime.ending_themes,
        Anime.list_status,
        Anime.my_list_status
    );

    // manga

    public static class Manga extends Common {

        public static final String volumes = "num_volumes";

        public static final String chapters = "num_chapters";

        public static class ListStatus extends Common.ListStatus {

            public static final String times_reread = "num_times_reread";

            public static final String reread_value = "reread_value";

        }

        public static class MyListStatus extends Manga.ListStatus { }

        public static final String list_status = list_status(
            Common.ListStatus.start_date,
            Common.ListStatus.finish_date,
            Common.ListStatus.priority,
            Common.ListStatus.tags,
            Common.ListStatus.comments,
            ListStatus.times_reread,
            ListStatus.reread_value
        );

        public static String list_status(final String... fields){
            return "list_status{" + String.join(",", fields == null ? new String[0] : fields) + '}';
        }

        public static final String my_list_status = "my_" + list_status;

        public static String my_list_status(final String... fields){
            return "my_" + list_status(fields);
        }

        public static class Authors {

            public static final String first_name = "first_name";

            public static final String last_name = "last_name";

        }

        public static final String authors = authors(
            Authors.first_name,
            Authors.last_name
        );

        public static String authors(final String... fields){
            return "authors{" + String.join(",", fields == null ? new String[0] : fields) + '}';
        }

        public static class Serialization {

            public static final String name = "name";

            public static final String role = "role";

        }

        public static final String serialization = serialization(
            Serialization.name,
            Serialization.role
        );

        public static String serialization(final String... fields){
            return "serialization{" + String.join(",", fields == null ? new String[0] : fields) + '}';
        }

    }

    public static final String manga = String.join(",",
        Manga.id,
        Manga.title,
        Manga.main_picture,
        Manga.alternative_titles,
        Manga.start_date,
        Manga.end_date,
        Manga.synopsis,
        Manga.mean,
        Manga.rank,
        Manga.popularity,
        Manga.list_users,
        Manga.scoring_users,
        Manga.nsfw,
        Manga.created_at,
        Manga.updated_at,
        Manga.media_type,
        Manga.status,
        Manga.genres,
        Manga.pictures,
        Manga.background,
        Manga.related_anime,
        Manga.related_manga,
        Manga.recommendations,
        Manga.volumes,
        Manga.chapters,
        Manga.list_status,
        Manga.my_list_status,
        Manga.authors,
        Manga.serialization
   );

    // user

    public static class User {

        public static final String birthday = "birthday";

        public static final String timezone = "time_zone";

        public static final String anime_statistics = "anime_statistics";

        public static final String supporter = "is_supporter";

    }

    public static String user = String.join(",",
        User.birthday,
        User.timezone,
        User.anime_statistics,
        User.supporter
    );

}
