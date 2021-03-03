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

package com.kttdevelopment.mal4j.manga;

import com.kttdevelopment.mal4j.manga.property.*;
import com.kttdevelopment.mal4j.property.FullMediaItem;

/**
 * <b>Documentation:</b> <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/manga_get">https://myanimelist.net/apiconfig/references/api/v2#operation/manga_get</a> <br>
 * Represents an Manga's full details.
 *
 * @see com.kttdevelopment.mal4j.MyAnimeList#getManga(long)
 * @see com.kttdevelopment.mal4j.MyAnimeList#getManga(long, String...)
 * @see MangaPreview
 * @see FullMediaItem
 * @since 1.0.0
 * @version 1.0.0
 * @author Ktt Development
 */
@SuppressWarnings("deprecation")
public abstract class Manga extends MangaPreview implements FullMediaItem<MangaType,MangaPublishStatus,MangaListStatus,MangaRecommendation,MangaStatistics> {

    // API methods

    /**
     * Returns the Manga's statistics.
     *
     * @deprecated Does not exist in the API currently
     * @return Manga statistics
     *
     * @see MangaStatistics
     * @since 1.0.0
     */
    // API doesn't return this
    @Override @Deprecated
    public final MangaStatistics getStatistics() {
        return null;
    }

    /**
     * Returns the publishers of the Manga.
     *
     * @return publishers
     *
     * @see Publisher
     * @since 1.0.0
     */
    public abstract Publisher[] getSerialization();

    // additional methods

    /**
     * Returns this.
     *
     * @return Manga
     *
     * @see Manga
     * @since 1.0.0
     */
    @Override
    public final Manga getManga() {
        return this;
    }

}
