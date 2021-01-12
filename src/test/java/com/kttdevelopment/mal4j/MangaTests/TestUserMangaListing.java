package com.kttdevelopment.mal4j.MangaTests;

import com.kttdevelopment.mal4j.MyAnimeList;
import com.kttdevelopment.mal4j.TestProvider;
import com.kttdevelopment.mal4j.manga.MangaListStatus;
import com.kttdevelopment.mal4j.manga.property.MangaSort;
import com.kttdevelopment.mal4j.manga.property.MangaStatus;
import org.junit.jupiter.api.*;

import java.util.List;

public class TestUserMangaListing {

    private static MyAnimeList mal;

    @BeforeAll
    public static void beforeAll(){
        mal = TestProvider.getMyAnimeList();
    }

    @Test
    public void testStatus(){
        final List<MangaListStatus> list =
            mal.getUserMangaListing()
               .withStatus(MangaStatus.PlanToRead)
               .withLimit(1)
               .withField(MyAnimeList.ALL_MANGA_FIELDS)
               .search();
        Assertions.assertEquals(MangaStatus.PlanToRead, list.get(0).getStatus());
    }

    @Test
    public void testSort(){
        final List<MangaListStatus> list =
            mal.getUserMangaListing()
               .sortBy(MangaSort.UpdatedAt)
               .withLimit(2)
               .withField(MyAnimeList.ALL_MANGA_FIELDS)
               .search();
        Assertions.assertTrue(list.get(0).getUpdatedAt().getTime() > list.get(1).getUpdatedAt().getTime());
    }

}
