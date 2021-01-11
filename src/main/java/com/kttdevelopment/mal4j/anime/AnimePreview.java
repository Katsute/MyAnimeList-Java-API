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

package com.kttdevelopment.mal4j.anime;

import com.kttdevelopment.mal4j.MyAnimeList;
import com.kttdevelopment.mal4j.anime.property.*;
import com.kttdevelopment.mal4j.anime.property.time.Season;
import com.kttdevelopment.mal4j.property.MediaItem;

/**
 * <b>Documentation:</b> <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/anime_get">https://myanimelist.net/apiconfig/references/api/v2#operation/anime_get</a> <br>
 * Represents an Anime's full details.
 *
 * @see MyAnimeList#getAnime()
 * @see MyAnimeList#getAnimeSeason(int, Season)
 * @see MyAnimeList#getAnimeSuggestions()
 * @see Anime
 * @see MediaItem
 * @since 1.0.0
 * @version 1.0.0
 * @author Ktt Development
 */
public abstract class AnimePreview implements AnimeRetrievable,MediaItem<AnimeType,AnimeAirStatus,AnimeListStatus> {

    // API methods

    /**
     * Returns the total amount of episodes.
     *
     * @return total episodes
     *
     * @since 1.0.0
     */
    public abstract int getEpisodes();

    /**
     * Returns the Anime start season.
     *
     * @return start season
     *
     * @see StartSeason
     * @since 1.0.0
     */
    public abstract StartSeason getStartSeason();

    /**
     * Returns the Anime's broadcast time.
     *
     * @return broadcast time
     *
     * @see Broadcast
     * @since 1.0.0
     */
    public abstract Broadcast getBroadcast();

    /**
     * Returns the Anime's source material.
     *
     * @return source material
     *
     * @see AnimeSource
     * @since 1.0.0
     */
    public abstract AnimeSource getSource();

    /**
     * Returns the average episode length in seconds.
     *
     * @return episode length
     *
     * @since 1.0.0
     */
    public abstract int getAverageEpisodeLength();

    /**
     * Returns the Anime's TV viewing rating (ex: pg13).
     *
     * @return TV viewing rating
     *
     * @see AnimeRating
     * @since 1.0.0
     */
    public abstract AnimeRating getRating();

    /**
     * Returns the Anime's studios.
     *
     * @return studios
     *
     * @see Studio
     * @since 1.0.0
     */
    public abstract Studio[] getStudios();

}
