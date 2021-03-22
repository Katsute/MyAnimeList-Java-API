package com.kttdevelopment.mal4j.AnimeTests;

import com.kttdevelopment.mal4j.*;
import com.kttdevelopment.mal4j.anime.Anime;
import com.kttdevelopment.mal4j.anime.AnimePreview;
import com.kttdevelopment.mal4j.anime.property.AnimeSeasonSort;
import com.kttdevelopment.mal4j.anime.property.time.Season;
import com.kttdevelopment.mal4j.property.NSFW;
import org.junit.jupiter.api.*;

import java.util.List;

public class TestAnimeSeason {

    private static MyAnimeList mal;

    @BeforeAll
    public static void beforeAll(){
        mal = TestProvider.getMyAnimeList();
    }

    @Test
    public void testSeason(){
        final List<Anime> season =
            mal.getAnimeSeason(2019, Season.Summer)
                .withLimit(1)
                .withField(Fields.Anime.start_season)
                .search();
        final AnimePreview anime = season.get(0);
        Assertions.assertEquals(2019, anime.getStartSeason().getYear());
        Assertions.assertTrue(
            anime.getStartSeason().getSeason() == Season.Summer || anime.getStartSeason().getSeason() == Season.Spring,
            "Anime start season was supposed to be either Summer or Spring but was " + anime.getStartSeason().getSeason().name()
        );
    }

    @Test
    public void testSort(){
        final List<Anime> season =
            mal.getAnimeSeason(2020, Season.Winter)
                .withLimit(2)
                .sortBy(AnimeSeasonSort.Users)
                .withFields(Fields.Anime.scoring_users)
                .search();
        final AnimePreview first = season.get(0);
        final AnimePreview second = season.get(1);
        Assertions.assertTrue(first.getUserScoringCount() > second.getUserScoringCount());
    }

    @Test
    public void testNSFW(){
        final List<Anime> season =
            mal.getAnimeSeason(2014, Season.Winter)
                .includeNSFW(true)
                .withFields(Fields.Anime.nsfw)
                .search();
        boolean hasNSFW = false;
        for(final Anime animePreview : season)
            if(animePreview.getNSFW() != NSFW.White){
                hasNSFW = true;
                break;
            }
        Assertions.assertTrue(hasNSFW, "Failed to find NSFW seasonal Anime (external issue, disregard fail)");
    }

}
