/*
 * Copyright (C) 2021-2022 Katsute <https://github.com/Katsute>
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

import com.kttdevelopment.mal4j.MyAnimeList;
import com.kttdevelopment.mal4j.manga.Manga;

/**
 * <b>Documentation:</b> <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/manga_get">https://myanimelist.net/apiconfig/references/api/v2#operation/manga_get</a> <br>
 * Represents a Manga search query.
 *
 * @see MyAnimeList#getManga()
 * @see SearchQuery
 * @since 1.0.0
 * @version 2.12.0
 * @author Katsute
 */
public abstract class MangaSearchQuery extends NSFWSearchQuery<MangaSearchQuery,Manga> {

    /**
     * Creates a Manga search query.
     * <br>
     * Do not use this constructor, use {@link MyAnimeList#getManga()} instead.
     *
     * @see MyAnimeList#getManga()
     * @since 1.0.0
     */
    public MangaSearchQuery() {
    }

}
