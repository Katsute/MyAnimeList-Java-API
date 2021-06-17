package com.kttdevelopment.mal4j.AnimeTests;

import com.kttdevelopment.jcore.Workflow;
import com.kttdevelopment.mal4j.*;
import com.kttdevelopment.mal4j.anime.AnimeListStatus;
import com.kttdevelopment.mal4j.anime.property.AnimeStatus;
import com.kttdevelopment.mal4j.property.Priority;
import com.kttdevelopment.mal4j.anime.property.RewatchValue;
import org.junit.jupiter.api.*;

import java.util.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAnimeListStatus {

    private static MyAnimeList mal;

    @BeforeAll
    public static void beforeAll(){
        mal = TestProvider.getMyAnimeList();
    }

    @SuppressWarnings("SpellCheckingInspection")
    @AfterAll
    public static void afterAll(){
        if(mal == null) return;

        mal.deleteAnimeListing(TestProvider.AnimeID);

        if(mal.getAuthenticatedUser().getID() != 8316239) return;

        final AnimeListStatus status = mal.updateAnimeListing(TestProvider.AnimeID)
            .status(AnimeStatus.Completed)
            .score(10)
            .episodesWatched(24)
            .rewatching(false)
            .priority(Priority.Low)
            .timesRewatched(0)
            .rewatchValue(RewatchValue.None)
            .tags("")
            .comments("")
            .update();

        Assertions.assertEquals(AnimeStatus.Completed, status.getStatus(),
                                Workflow.errorSupplier("Expected status to match"));
        Assertions.assertEquals(10, status.getScore(),
                                Workflow.errorSupplier("Expected score to match"));
        Assertions.assertEquals(24, status.getWatchedEpisodes(),
                                Workflow.errorSupplier("Expected episodes to match"));
        Assertions.assertFalse(status.isRewatching(),
                               Workflow.errorSupplier("Expected rewatching to be false"));
        Assertions.assertEquals(Priority.Low, status.getPriority(),
                                Workflow.errorSupplier("Expected priority to match"));
        Assertions.assertEquals(0, status.getTimesRewatched(),
                                Workflow.errorSupplier("Expected times rewatched to match"));
        Assertions.assertEquals(RewatchValue.None, status.getRewatchValue(),
                                Workflow.errorSupplier("Expected rewatch value to match"));
        Assertions.assertEquals(0, status.getTags().length,
                                Workflow.errorSupplier("Expected tags to match"));
        Assertions.assertEquals("", status.getComments(),
                                Workflow.errorSupplier("Expected comments to match"));
    }

    @Test @Order(1)
    public void testDelete(){
        mal.deleteAnimeListing(TestProvider.AnimeID);
        Assertions.assertDoesNotThrow(() -> mal.deleteAnimeListing(TestProvider.AnimeID),
                                      Workflow.errorSupplier("Deleting a deleted listing should not throw an exception"));
        Assertions.assertNull(mal.getAnime(TestProvider.AnimeID, Fields.Anime.my_list_status).getListStatus().getUpdatedAtEpochMillis(),
                              Workflow.errorSupplier("Expected a deleted listing to be null"));
    }

    private static boolean passedUpdate = false;
    @Test @Order(2)
    public void testUpdate(){
        final Date now = new Date();
        final AnimeListStatus status = mal.updateAnimeListing(TestProvider.AnimeID)
            .status(AnimeStatus.Completed)
            .score(10)
            .startDate(now)
            .finishDate(now)
            .episodesWatched(24)
            .rewatching(true)
            .priority(Priority.High)
            .timesRewatched(0)
            .rewatchValue(RewatchValue.VeryHigh)
            .tags(TestProvider.testTags())
            .comments(TestProvider.testComment)
            .update();

        testStatus(status);
        passedUpdate = true;
    }

    @Test @Order(3)
    public void testGet(){
        Assertions.assertTrue(passedUpdate,
                              Workflow.errorSupplier("Failed to start test (test requires update test to pass)"));

        final List<AnimeListStatus> list =
            mal.getUserAnimeListing()
                .withStatus(AnimeStatus.Watching)
                .withLimit(1000)
                .withFields(Fields.Anime.list_status)
                .includeNSFW()
                .search();

        for(final AnimeListStatus listStatus : list)
            if(listStatus.getAnimePreview().getID() == TestProvider.AnimeID){
                testStatus(listStatus);
                return;
            }
        Assertions.fail(Workflow.errorSupplier("Anime list status not found"));
    }

    @Test @Order(3)
    public void testGetUsername(){
        Assertions.assertTrue(passedUpdate,
                              Workflow.errorSupplier("Failed to start test (test requires update test to pass)"));

        final List<AnimeListStatus> list =
            mal.getUserAnimeListing("KatsuteDev")
                .withStatus(AnimeStatus.Watching)
                .withLimit(1000)
                .withFields(Fields.Anime.list_status)
                .includeNSFW()
                .search();

        for(final AnimeListStatus listStatus : list)
            if(listStatus.getAnimePreview().getID() == TestProvider.AnimeID){
                testStatus(listStatus);
                return;
            }
        Assertions.fail(Workflow.errorSupplier("User Anime list status not found"));
    }

    @Test @Order(3)
    public void testGetFromAnime(){
        Assertions.assertTrue(passedUpdate,
                              Workflow.errorSupplier("Failed to start test (test requires update test to pass)"));

        final AnimeListStatus status = mal
            .getAnime(TestProvider.AnimeID, Fields.Anime.my_list_status)
            .getListStatus();
        testStatus(status);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void testStatus(final AnimeListStatus status){
        Assertions.assertEquals(AnimeStatus.Completed, status.getStatus(),
                                Workflow.errorSupplier("Expected status to match"));
        Assertions.assertEquals(10, status.getScore(),
                                Workflow.errorSupplier("Expected score to match"));
        Assertions.assertEquals(24, status.getWatchedEpisodes(),
                                Workflow.errorSupplier(("Expected episodes watched to match")));
        Assertions.assertTrue(status.isRewatching(),
                              Workflow.errorSupplier("Expected rewatching to be true"));
        Assertions.assertNotNull(status.getStartDate(),
                                 Workflow.errorSupplier("Expected start date to not be null"));
        Assertions.assertNotNull(status.getFinishDate(),
                                 Workflow.errorSupplier("Expected finish date to not be null"));
        Assertions.assertEquals(Priority.High, status.getPriority(),
                                Workflow.errorSupplier("Expected priority to match"));
        Assertions.assertEquals(0, status.getTimesRewatched(),
                                Workflow.errorSupplier("Expected times rewatched to match"));
        Assertions.assertEquals(RewatchValue.VeryHigh, status.getRewatchValue(),
                                Workflow.errorSupplier("Expected rewatch value to match"));
        Assertions.assertTrue(Arrays.asList(status.getTags()).contains(TestProvider.testTags()[0]),
                              Workflow.errorSupplier("Expected tags to match"));
        Assertions.assertTrue(Arrays.asList(status.getTags()).contains(TestProvider.testTags()[1]),
                              Workflow.errorSupplier("Expected tags to match"));
        Assertions.assertEquals(TestProvider.testComment, status.getComments(),
                                Workflow.errorSupplier("Expected comment to match"));
        Assertions.assertNotNull(status.getUpdatedAt(),
                                 Workflow.errorSupplier("Expected updated at to not be null"));
        Assertions.assertNotNull(status.getUpdatedAtEpochMillis(),
                                 Workflow.errorSupplier("Expected updated at millis to not be null"));
    }

    @Test @Order(4)
    public void testConsecutiveUpdates(){
        testDelete();
        testUpdate();
        testUpdate();
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test @Order(5) @DisplayName("testEcchiNSFW(), #90 - Ecchi as NSFW")
    public void testEcchiNSFW(){
        Assertions.assertTrue(passedUpdate,
                              Workflow.errorSupplier("Failed to start test (test requires update test to pass)"));

        final List<AnimeListStatus> list =
            mal.getUserAnimeListing()
                .withLimit(1000)
                .withFields(Fields.Anime.list_status)
                .search();

        for(final AnimeListStatus listStatus : list)
            if(listStatus.getAnimePreview().getID() == TestProvider.AnimeID)
                return;
        Assertions.fail(Workflow.errorSupplier("Failed to find Anime with Ecchi genre (this is an external issue, ignore this)"));
    }

}
