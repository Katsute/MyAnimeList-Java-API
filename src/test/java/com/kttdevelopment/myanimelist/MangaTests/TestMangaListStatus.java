package com.kttdevelopment.myanimelist.MangaTests;

import com.kttdevelopment.myanimelist.MyAnimeList;
import com.kttdevelopment.myanimelist.TestProvider;
import com.kttdevelopment.myanimelist.manga.MangaListStatus;
import com.kttdevelopment.myanimelist.manga.property.MangaStatus;
import com.kttdevelopment.myanimelist.query.property.Priority;
import com.kttdevelopment.myanimelist.query.property.RereadValue;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestMangaListStatus {

    private static MyAnimeList mal;

    @BeforeAll
    public static void beforeAll(){
        mal = TestProvider.getMyAnimeList();
    }

    @AfterAll
    public static void cleanup(){
        TestProvider.testRequireLiveUser();

        mal.deleteMangaListing(TestProvider.MangaID);
        final MangaListStatus status = mal.updateMangaListing(TestProvider.MangaID)
            .status(MangaStatus.PlanToRead)
            .score(0)
            .volumesRead(0)
            .chaptersRead(0)
            .rereading(false)
            .priority(Priority.Low)
            .timesReread(0)
            .rereadValue(RereadValue.None)
            .tags("")
            .comments("")
            .update();

        Assertions.assertEquals(MangaStatus.PlanToRead, status.getStatus());
        Assertions.assertEquals(0, status.getScore());
        Assertions.assertEquals(0, status.getVolumesRead());
        Assertions.assertEquals(0, status.getChaptersRead());
        Assertions.assertFalse(status.isRereading());
        Assertions.assertEquals(Priority.Low, status.getPriority());
        Assertions.assertEquals(0, status.getTimesReread());
        Assertions.assertEquals(RereadValue.None, status.getRereadValue());
        Assertions.assertEquals(0, status.getTags().length);
        Assertions.assertEquals("", status.getComments());
    }

    @Test @Order(1)
    public void testDelete(){
        mal.deleteMangaListing(TestProvider.MangaID);
        Assertions.assertNull(mal.getManga(TestProvider.MangaID).getListStatus());
    }

    @Test @Order(2)
    public void testUpdate(){
        final MangaListStatus status = mal.updateMangaListing(28107)
            .status(MangaStatus.Completed)
            .score(10)
            .volumesRead(0)
            .chaptersRead(0)
            .rereading(true)
            .priority(Priority.High)
            .timesReread(0)
            .rereadValue(RereadValue.VeryHigh)
            .tags("ignore", "tags")
            .comments("ignore comments")
            .update();

        testStatus(status);
    }

    @Test @Order(3) @DisplayName("testGet(), #25 - Rereading status")
    public void testGet(){
        final List<MangaListStatus> list =
            mal.getUserMangaListing()
                .withStatus(MangaStatus.Reading)
                .withLimit(1000)
                .withField(MyAnimeList.ALL_MANGA_FIELDS)
                .search();

        MangaListStatus status = null;
        for(final MangaListStatus userMangaListStatus : list)
            if((status = userMangaListStatus).getMangaPreview().getID() == TestProvider.MangaID)
                break;
            else
                status = null;
        if(status == null)
            Assertions.fail();

        testStatus(status);
    }

    @Test @Order(3) @DisplayName("testGetUsername(), #25 - Rereading status")
    public void testGetUsername(){
        final List<MangaListStatus> list =
            mal.getUserMangaListing("KatsuteDev")
                .withStatus(MangaStatus.Reading)
                .withLimit(1000)
                .withField(MyAnimeList.ALL_MANGA_FIELDS)
                .search();

        MangaListStatus status = null;
        for(final MangaListStatus userMangaListStatus : list)
            if((status = userMangaListStatus).getMangaPreview().getID() == TestProvider.MangaID)
                break;
            else
                status = null;
        if(status == null)
            Assertions.fail();

        testStatus(status);
    }

    @Test @Order(3)
    public void testGetFromManga(){
        final MangaListStatus status = mal.getManga(TestProvider.MangaID, MyAnimeList.ALL_MANGA_FIELDS).getListStatus();
        testStatus(status);
    }

    @SuppressWarnings("CommentedOutCode")
    private void testStatus(final MangaListStatus status){
        Assertions.assertEquals(MangaStatus.Completed, status.getStatus());
        Assertions.assertEquals(10, status.getScore());
        Assertions.assertEquals(0, status.getVolumesRead());
        Assertions.assertEquals(0, status.getChaptersRead());
        Assertions.assertTrue(status.isRereading());
        // Assertions.assertNotEquals(-1, status.getStartDate()); // will fail (unable to set start date)
        // Assertions.assertNotEquals(-1, status.getFinishDate()); // will fail (unable to set finish date)
        Assertions.assertEquals(Priority.High, status.getPriority());
        Assertions.assertEquals(0, status.getTimesReread());
        Assertions.assertEquals(RereadValue.VeryHigh, status.getRereadValue());
        Assertions.assertTrue(Arrays.asList(status.getTags()).contains("ignore"));
        Assertions.assertTrue(Arrays.asList(status.getTags()).contains("tags"));
        Assertions.assertEquals("ignore comments", status.getComments());
        Assertions.assertNotEquals(-1, status.getUpdatedAt());
    }

}
