package com.kttdevelopment.mal4j.UserTests;

import com.kttdevelopment.mal4j.MyAnimeList;
import com.kttdevelopment.mal4j.TestProvider;
import com.kttdevelopment.mal4j.user.User;
import com.kttdevelopment.mal4j.user.UserAnimeStatistics;
import org.junit.jupiter.api.*;

public class TestUser {

    private static MyAnimeList mal;
    private static User user;

    @SuppressWarnings("ConstantConditions")
    @BeforeAll
    public static void beforeAll(){
        mal = TestProvider.getMyAnimeList();
        user = mal.getMyself();
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test @DisplayName("testUser() - not currently allowed by API")
    public void testUser(){
        Assertions.assertThrows(UnsupportedOperationException.class, () ->
            Assertions.assertEquals(8316239, mal.getUser("KatsuteDev").getID())
        );
    }

    @Test
    public void testMyself(){
        Assertions.assertNotNull(user.getID());
        Assertions.assertNotNull(user.getName());
        Assertions.assertNotNull(user.getPictureURL());
        Assertions.assertNotNull(user.getGender());
        Assertions.assertNotNull(user.getLocation());
        Assertions.assertNotNull(user.getJoinedAt());
        Assertions.assertNotNull(user.getJoinedAtEpochMillis());
        Assertions.assertNotNull(user.getTimeZone());
    }

    @Test
    public void testStatistics(){
        final UserAnimeStatistics statistics = user.getAnimeStatistics();
        Assertions.assertNotNull(statistics.getItemsWatching());
        Assertions.assertNotNull(statistics.getItemsCompleted());
        Assertions.assertNotNull(statistics.getDaysOnHold());
        Assertions.assertNotNull(statistics.getItemsPlanToWatch());
        Assertions.assertNotNull(statistics.getItemsDropped());
        Assertions.assertNotNull(statistics.getItemsOnHold());
        Assertions.assertNotNull(statistics.getItems());
        Assertions.assertNotNull(statistics.getDaysWatched());
        Assertions.assertNotNull(statistics.getDaysWatching());
        Assertions.assertNotNull(statistics.getDaysCompleted());
        Assertions.assertNotNull(statistics.getDaysOnHold());
        Assertions.assertNotNull(statistics.getDaysDropped());
        Assertions.assertNotNull(statistics.getDays());
        Assertions.assertNotNull(statistics.getEpisodes());
        Assertions.assertNotNull(statistics.getTimesRewatched());
        Assertions.assertNotNull(statistics.getMeanScore());
    }

    @Test // test does actually pass
    public void testBirthday(){
        Assumptions.assumeTrue(user.getBirthday() != null, "User might not specify a birthday");
        Assumptions.assumeTrue(user.getBirthdayEpochMillis() != null, "User might not specify a birthday");
    }

    @Test // false is treated as null
    public void testSupporter(){
        Assumptions.assumeTrue(user.isSupporter() != null);
    }

}
