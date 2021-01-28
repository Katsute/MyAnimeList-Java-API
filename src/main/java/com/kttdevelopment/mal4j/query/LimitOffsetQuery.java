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

import com.kttdevelopment.mal4j.*;

import java.io.UncheckedIOException;
import java.util.List;

/**
 * Represents a query with a limit and offset.
 *
 * @param <T> this
 * @param <R> response
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Ktt Development
 */
@SuppressWarnings({"unchecked"})
abstract class LimitOffsetQuery<T extends LimitOffsetQuery<T,R>,R> {

    protected Integer limit;
    protected Integer offset;

    LimitOffsetQuery() { }

    /**
     * Sets maximum amount of listings to return.
     *
     * @param limit limit
     * @return search query
     *
     * @since 1.0.0
     */
    public final T withLimit(final int limit){
        this.limit = limit;
        return (T) this;
    }

    /**
     * Sets the offset.
     *
     * @param offset offset
     * @return search query
     *
     * @since 1.0.0
     */
    public final T withOffset(final int offset){
        this.offset = offset;
        return (T) this;
    }

    /**
     * Runs the search query.
     *
     * @return search listings
     * @throws HttpException if request failed
     * @throws UncheckedIOException if client failed to execute request
     *
     * @since 1.0.0
     */
    public abstract List<R> search();

    /**
     * Runs the search query and returns an iterable.
     *
     * @return search iterable
     * @throws HttpException if request failed
     * @throws UncheckedIOException if client failed to execute request
     *
     * @since 1.0.0
     */
    public abstract PaginatedIterator<R> searchAll();

}
