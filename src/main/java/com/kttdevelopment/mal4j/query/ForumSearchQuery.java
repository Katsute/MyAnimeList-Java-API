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

import com.kttdevelopment.mal4j.MyAnimeList;
import com.kttdevelopment.mal4j.forum.ForumTopicDetail;
import com.kttdevelopment.mal4j.forum.property.ForumSort;

/**
 * <b>Documentation:</b> <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/forum_topics_get">https://myanimelist.net/apiconfig/references/api/v2#operation/forum_topics_get</a> <br>
 * Represents a forum topic search query.
 *
 * @see MyAnimeList#getForumTopics()
 * @since 1.0.0
 * @version 1.0.0
 * @author Ktt Development
 */
public abstract class ForumSearchQuery extends SearchQuery<ForumSearchQuery,ForumTopicDetail> {

    protected String query;
    @SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
    protected Long boardId, subboardId;
    @SuppressWarnings("CanBeFinal")
    protected ForumSort sort = ForumSort.Recent;
    protected String topicUsername, username;
    
    public ForumSearchQuery(){ }

    /**
     * Sets the search query.
     *
     * @param query query
     * @return search query
     *
     * @since 1.0.0
     */
    public final ForumSearchQuery withQuery(final String query){
        this.query = query;
        return this;
    }

    /**
     * Sets the board id.
     *
     * @param boardId board id
     * @return search query
     *
     * @since 1.0.0
     */
    public final ForumSearchQuery withBoardId(final long boardId){
        this.boardId = boardId;
        return this;
    }

    /**
     * Sets the subboard id.
     *
     * @param subboardId subboard id
     * @return search query
     *
     * @since 1.0.0
     */
    @SuppressWarnings("SpellCheckingInspection")
    public final ForumSearchQuery withSubboardId(final long subboardId){
        this.subboardId = subboardId;
        return this;
    }

    /**
     * Sets the topic username.
     *
     * @param topicUsername topic username
     * @return search query
     *
     * @since 1.0.0
     */
    public final ForumSearchQuery withTopicUsername(final String topicUsername){
        this.topicUsername = topicUsername;
        return this;
    }

    /**
     * Sets the username.
     *
     * @param username username
     * @return search query
     *
     * @since 1.0.0
     */
    public final ForumSearchQuery withUsername(final String username){
        this.username = username;
        return this;
    }

}
