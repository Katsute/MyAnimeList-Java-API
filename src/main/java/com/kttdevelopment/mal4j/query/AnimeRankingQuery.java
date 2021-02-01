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

package com.kttdevelopment.mal4j.query;

import com.kttdevelopment.mal4j.anime.AnimeRanking;
import com.kttdevelopment.mal4j.anime.property.AnimeRankingType;

/**
 * <b>Documentation:</b> <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/anime_ranking_get">https://myanimelist.net/apiconfig/references/api/v2#operation/anime_ranking_get</a> <br>
 * Represents an Anime ranking query.
 *
 * @see com.kttdevelopment.mal4j.MyAnimeList#getAnimeRanking(AnimeRankingType)
 * @see FieldQuery
 * @since 1.0.0
 * @version 1.0.0
 * @author Ktt Development
 */
public abstract class AnimeRankingQuery extends FieldQuery<AnimeRankingQuery,AnimeRanking> {

    protected final AnimeRankingType rankingType;
    protected Boolean nsfw;

    /**
     * Creates an Anime ranking query. Applications do not use this constructor.
     *
     * @param rankingType ranking type
     *
     * @see com.kttdevelopment.mal4j.MyAnimeList#getAnimeRanking(AnimeRankingType)
     * @see AnimeRankingType
     * @since 1.0.0
     */
    public AnimeRankingQuery(final AnimeRankingType rankingType) {
        this.rankingType = rankingType;
    }

    /**
     * Sets if the query will return NSFW results.
     *
     * @param nsfw nsfw
     * @return ranking query
     *
     * @since 1.0.0
     */
    public final AnimeRankingQuery includeNSFW(final boolean nsfw){
        this.nsfw = nsfw;
        return this;
    }

}
