package com.kttdevelopment.mal4j.AnimeTests;

import com.kttdevelopment.mal4j.*;
import com.kttdevelopment.mal4j.anime.AnimePreview;
import com.kttdevelopment.mal4j.anime.property.AnimeSeasonSort;
import com.kttdevelopment.mal4j.anime.property.time.Season;
import com.kttdevelopment.mal4j.property.NSFW;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

final class TestAnimeSeason {

    private static MyAnimeList mal;

    @BeforeAll
    static void beforeAll(){
        mal = TestProvider.getMyAnimeList();
    }

    @Test
    final void testSeason(){
        final int year = 2019;
        final List<AnimePreview> season =
            mal.getAnimeSeason(year, Season.Summer)
                .withField(Fields.Anime.start_season)
                .search();

        int thisYear = 0;
        int otherYear = 0;

        AnimePreview anime = null;
        for(final AnimePreview iterator : season)
            if(iterator.getStartSeason().getYear() == year){
                thisYear++;
                anime = iterator;
            }else
                otherYear++;

        final int finalThisYear  = thisYear;
        final int finalOtherYear = otherYear;
        assertTrue(finalThisYear > finalOtherYear, "Expected seasonal search to return mostly from selected year (search contained mostly results from other years)");

        final AnimePreview finalAnime = anime;
        assertNotNull(finalAnime, "Expected seasonal search to return an Anime from selected year");
        assertTrue(
            finalAnime.getStartSeason().getSeason() == Season.Summer || finalAnime.getStartSeason().getSeason() == Season.Spring,
            "Anime start season was supposed to be either Summer or Spring but was " + finalAnime.getStartSeason().getSeason().name()
        );
    }

    @Test
    final void testSort(){
        final List<AnimePreview> season =
            mal.getAnimeSeason(2020, Season.Winter)
                .withLimit(2)
                .sortBy(AnimeSeasonSort.Users)
                .withFields(Fields.Anime.scoring_users)
                .search();
        final AnimePreview first = season.get(0);
        final AnimePreview second = season.get(1);
        assertTrue(first.getUserScoringCount() > second.getUserScoringCount(), "Expected season to be sorted");
    }

    @Test
    final void testNSFW(){
        final List<AnimePreview> season =
            mal.getAnimeSeason(2014, Season.Winter)
                .includeNSFW(true)
                .withFields(Fields.Anime.nsfw)
                .withLimit(100)
                .search();
        boolean hasNSFW = false;
        for(final AnimePreview animePreview : season){
            if(animePreview.getNSFW() != NSFW.White){
                hasNSFW = true;
                break;
            }
        }

        final boolean finalHasNSFW = hasNSFW;
        assertTrue(finalHasNSFW, "Failed to find NSFW seasonal Anime");
    }

}
