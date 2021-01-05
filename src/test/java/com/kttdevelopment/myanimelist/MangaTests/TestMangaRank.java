package com.kttdevelopment.myanimelist.MangaTests;

import com.kttdevelopment.myanimelist.MyAnimeList;
import com.kttdevelopment.myanimelist.TestProvider;
import com.kttdevelopment.myanimelist.manga.MangaRanking;
import com.kttdevelopment.myanimelist.manga.property.MangaRankingType;
import com.kttdevelopment.myanimelist.manga.property.MangaType;
import org.junit.jupiter.api.*;

import java.util.List;

public class TestMangaRank {

    private static MyAnimeList mal;

    @BeforeAll
    public static void beforeAll(){
        mal = TestProvider.getMyAnimeList();
    }

    @Test
    public void testRanking(){
        final List<MangaRanking> ranking =
            mal.getMangaRanking(MangaRankingType.Manga)
                .withLimit(1)
                .withFields(MyAnimeList.ALL_MANGA_FIELDS)
                .search();
        final MangaRanking first = ranking.get(0);
        Assertions.assertEquals(1,first.getRanking());
        Assertions.assertTrue(first.getPreviousRank() < 1);
        Assertions.assertEquals(MangaType.Manga, first.getMangaPreview().getType());
    }

    @Test @DisplayName("#5 - Ranking") @Disabled
    public void testNSFW(){
        // difficult to test since NSFW is unlikely to be in top ranking
    }

}
