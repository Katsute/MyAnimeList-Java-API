package com.kttdevelopment.mal4j.AnimeTests;

import com.kttdevelopment.mal4j.*;
import com.kttdevelopment.mal4j.anime.AnimeRanking;
import com.kttdevelopment.mal4j.anime.property.AnimeRankingType;
import com.kttdevelopment.mal4j.anime.property.AnimeType;
import dev.katsute.jcore.Workflow;
import org.junit.jupiter.api.*;

import java.util.List;

public class TestAnimeRank {

    private static MyAnimeList mal;

    @BeforeAll
    public static void beforeAll(){
        mal = TestProvider.getMyAnimeList();
    }

    @SuppressWarnings("CommentedOutCode")
    @Test
    public void testRanking(){
        final List<AnimeRanking> ranking =
            mal.getAnimeRanking(AnimeRankingType.Movie)
                .withLimit(1)
                .withFields(Fields.Anime.rank, Fields.Anime.media_type)
                .search();
        final AnimeRanking first = ranking.get(0);
        Assertions.assertEquals(1, first.getRanking(),
                                Workflow.errorSupplier("Expected first ranking to be 1"));
        Assertions.assertEquals(AnimeType.Movie, first.getAnimePreview().getType(),
                                Workflow.errorSupplier("Expected ranking type to match"));
        // Assertions.assertNotNull(first.getPreviousRank(),
        //                          Workflow.warningSupplier("Failed to get previous rank for Anime"));
        Assumptions.assumeTrue(first.getPreviousRank() != null,
                               Workflow.warningSupplier("Failed to get previous rank for Anime (this is an external issue, ignore this)"));
    }

    @SuppressWarnings("EmptyMethod")
    @Test @Disabled
    public void testNSFW(){
        // difficult to test since NSFW is unlikely to be in top ranking
    }

}
