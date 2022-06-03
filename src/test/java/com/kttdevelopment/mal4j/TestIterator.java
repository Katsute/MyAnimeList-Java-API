package com.kttdevelopment.mal4j;

import com.kttdevelopment.mal4j.anime.AnimePreview;
import com.kttdevelopment.mal4j.forum.Post;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class TestIterator {

    private static MyAnimeList mal;

    @BeforeAll
    public static void beforeAll(){
        mal = TestProvider.getMyAnimeList();
    }

    @Test
    final void testIterator(){
        final PaginatedIterator<AnimePreview> iterator = mal
            .getAnime()
            .withQuery(TestProvider.AnimeQuery)
            .withNoFields()
            .withLimit(100)
            .searchAll();
        assertNotEquals(0, iterator.toList().size());
        assertEquals(TestProvider.AnimeID, iterator.toList().get(0).getID());

        final AnimePreview first = iterator.next();
        assertEquals(TestProvider.AnimeID, first.getID());
        iterator.forEachRemaining(animePreview -> assertNotEquals(TestProvider.AnimeID, animePreview.getID()));
    }

    @Test
    final void testPostIterator(){
        final PaginatedIterator<Post> iterator = mal
            .getForumTopicDetailPostQuery(481)
            .searchAll();
        assertNotEquals(0, iterator.toList().size());
        assertEquals(481, iterator.toList().get(0).getForumTopicDetail().getID());

        iterator.forEachRemaining(post -> assertEquals(481, post.getForumTopicDetail().getID()));
    }

}
