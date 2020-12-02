package com.kttdevelopment.myanimelist;

import com.kttdevelopment.myanimelist.anime.*;
import com.kttdevelopment.myanimelist.anime.property.*;
import com.kttdevelopment.myanimelist.anime.property.time.Season;
import com.kttdevelopment.myanimelist.auth.MyAnimeListAuthenticator;
import com.kttdevelopment.myanimelist.forum.*;
import com.kttdevelopment.myanimelist.forum.property.*;
import com.kttdevelopment.myanimelist.manga.*;
import com.kttdevelopment.myanimelist.manga.property.*;
import com.kttdevelopment.myanimelist.property.ListStatus;
import com.kttdevelopment.myanimelist.property.NSFW;
import com.kttdevelopment.myanimelist.user.User;
import com.kttdevelopment.myanimelist.user.UserAnimeStatistics;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class TestMyAnimeList {

    private static MyAnimeList mal;

    private static final Path client = new File("src/test/java/com/kttdevelopment/myanimelist/client.txt").toPath();
    private static final Path oauth  = new File("src/test/java/com/kttdevelopment/myanimelist/oauth.txt").toPath();

    private static MyAnimeListAuthenticator authenticator;

    @BeforeAll
    public static void beforeAll() throws IOException{
        if(oauth.toFile().exists()){ // use existing OAuth
            mal = MyAnimeList.withOAuthToken(Files.readString(oauth));
            if(mal.getAnime().withQuery("さくら荘のペットな彼女").search() != null)
                return; // create new auth only if above null
        }

        Assumptions.assumeTrue(client.toFile().exists(), "Skipping tests (requires user authentication)");
        final String clientId = Files.readString(client);
        authenticator = new MyAnimeListAuthenticator(clientId, null, 5050);
        mal = MyAnimeList.withAuthorization(authenticator);

        // test refresh auth
        Assertions.assertNotNull(mal.getAnime().withQuery("さくら荘のペットな彼女").search());
        mal.refreshOAuthToken();
    }

    @AfterAll
    public static void afterAll() throws IOException{
        if(authenticator != null)
            Files.write(oauth, authenticator.getAccessToken().getToken().getBytes(StandardCharsets.UTF_8));
    }

    // Anime

    @Test
    public void testAnimeSearch(){
        // test standard
        {
            final List<AnimePreview> search =
                mal.getAnime()
                    .withQuery("さくら荘のペットな彼女")
                    .search();
            Assertions.assertEquals(13759, search.get(0).getID());
            Assertions.assertNotEquals(1, search.size());
        }
        // test offset & limit
        {
            final List<AnimePreview> search =
                mal.getAnime()
                    .withQuery("さくら荘のペットな彼女")
                    .withLimit(1)
                    .withOffset(1)
                    .search();
            Assertions.assertNotEquals(13759, search.get(0).getID());
            Assertions.assertEquals(1, search.size());
        }
        // test fields
        {
            final List<AnimePreview> search =
                mal.getAnime()
                    .withQuery("さくら荘のペットな彼女")
                    .withLimit(1)
                    .withFields(new String[0])
                    .search();
            Assertions.assertNull(search.get(0).getType());
        }
        // test NSFW
        {
            {
                final List<AnimePreview> search =
                    mal.getAnime()
                        .withQuery("いただきっセーエキ")
                        .withLimit(1)
                        .search();
                Assertions.assertEquals(0, search.size());
            }
            {
                final List<AnimePreview> search =
                    mal.getAnime()
                       .withQuery("いただきっセーエキ")
                       .withLimit(1)
                       .includeNSFW(true)
                       .search();
                Assertions.assertEquals(22429, search.get(0).getID());
            }
        }
        // test limit bounds
        { // seems to be an API issue
            try{
                Assumptions.assumeTrue(mal.getAnime().withLimit(200).search() != null, "API issue, disregard failure");
                Assumptions.assumeTrue(mal.getAnime().withOffset(-1).search() != null, "API issue, disregard failure");
            }catch(final InvalidParametersException e){
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testAnime(){
        final Anime anime = mal.getAnime(11757);
        Assertions.assertEquals(anime, anime.getAnime());

        Assertions.assertEquals(11757, anime.getID());
        Assertions.assertNotNull(anime.getTitle());
        Assertions.assertNotNull(anime.getMainPicture().getMediumURL());
        Assertions.assertNotNull(anime.getMainPicture().getLargeURL());
        Assertions.assertNotNull(anime.getAlternativeTitles().getEnglish());
        Assertions.assertNotNull(anime.getAlternativeTitles().getJapanese());
        Assertions.assertNotNull(anime.getAlternativeTitles().getSynonyms());
        Assertions.assertNotEquals(-1, anime.getStartDate());
        Assertions.assertNotEquals(-1, anime.getEndDate());
        Assertions.assertNotNull(anime.getSynopsis());
        Assertions.assertNotEquals(-1, anime.getMeanRating());
        Assertions.assertNotEquals(-1, anime.getRank());
        Assertions.assertNotEquals(-1, anime.getPopularity());
        Assertions.assertNotEquals(-1, anime.getUserListingCount());
        Assertions.assertNotEquals(-1, anime.getUserScoringCount());
        Assertions.assertNotNull(anime.getNSFW());
        Assertions.assertNotNull(anime.getGenres());
        Assertions.assertNotEquals(-1, anime.getCreatedAt());
        Assertions.assertNotEquals(-1, anime.getUpdatedAt());
        Assertions.assertNotNull(anime.getType());
        Assertions.assertNotNull(anime.getStatus());
        Assertions.assertNotNull(anime.getStartSeason().getSeason());
        Assertions.assertNotEquals(-1, anime.getStartSeason().getYear());
        Assertions.assertNotNull(anime.getBroadcast().getDayOfWeek());
        Assertions.assertNotNull(anime.getBroadcast().getStartTime());
        Assertions.assertNotNull(anime.getSource());
        Assertions.assertNotEquals(-1, anime.getAverageEpisodeLength());
        Assertions.assertNotNull(anime.getRating());
        Assertions.assertNotEquals(-1, anime.getStudios()[0].getID());
        Assertions.assertNotNull(anime.getStudios()[0].getName());
        Assertions.assertNotNull(anime.getPictures()[0].getMediumURL());
        Assertions.assertNotNull(anime.getPictures()[0].getLargeURL());
        Assertions.assertNotNull(anime.getBackground());
        // related
        {
            final RelatedAnime relatedAnime = anime.getRelatedAnime()[0];
            Assertions.assertNotEquals(-1, relatedAnime.getAnimePreview().getID());
            Assertions.assertNotNull(relatedAnime.getRelationType());
            Assertions.assertNotNull(relatedAnime.getRelationTypeFormat());

            // Anime is unlikely to have a related manga
            // final RelatedManga relatedManga = anime.getRelatedManga()[0];
            // Assertions.assertNotEquals(-1, relatedManga.getMangaPreview().getID());
            // Assertions.assertNotNull(relatedManga.getRelationType());
            // Assertions.assertNotNull(relatedManga.getRelationTypeFormat());
        }
        // recommendations
        {
            final AnimeRecommendation recommendation = anime.getRecommendations()[0];
            Assertions.assertNotEquals(-1, recommendation.getAnimePreview().getID());
            Assertions.assertNotEquals(-1, recommendation.getRecommendations());
        }
        // statistics
        {
            final AnimeStatistics statistics = anime.getStatistics();
            Assertions.assertNotEquals(-1, statistics.getCompleted());
            Assertions.assertNotEquals(-1, statistics.getDropped());
            Assertions.assertNotEquals(-1, statistics.getOnHold());
            Assertions.assertNotEquals(-1, statistics.getPlanToWatch());
            Assertions.assertNotEquals(-1, statistics.getWatching());
            Assertions.assertNotEquals(-1, statistics.getUserCount());
        }
        // list status
        {
            final AnimeListStatus listStatus = anime.getListStatus();
            Assertions.assertNotNull(listStatus.getStatus());
            Assertions.assertNotEquals(-1, listStatus.getScore());
            Assertions.assertNotEquals(-1, listStatus.getWatchedEpisodes());
            Assertions.assertFalse(listStatus.isRewatching()); // weak test
            Assertions.assertNotEquals(-1, listStatus.getStartDate());
            Assertions.assertNotEquals(-1, listStatus.getFinishDate());
            Assertions.assertNotEquals(-1, listStatus.getPriority());
            Assertions.assertNotEquals(-1, listStatus.getTimesRewatched());
            Assertions.assertNotEquals(-1, listStatus.getRewatchValue());
            Assertions.assertNotNull(listStatus.getTags());
            Assertions.assertNotNull(listStatus.getComments());
            Assertions.assertNotEquals(-1, listStatus.getUpdatedAt());
        }
    }

    @Test
    public void testAnimeRank(){
        // test standard
        {
            final List<AnimeRanking> ranking =
                mal.getAnimeRanking(AnimeRankingType.Movie)
                    .withLimit(1)
                    .search();
            final AnimeRanking first = ranking.get(0);
            Assertions.assertEquals(1,first.getRanking());
            Assertions.assertTrue(first.getPreviousRank() < 1);
            Assertions.assertEquals(AnimeType.Movie, first.getAnimePreview().getType());
        }
        // test NSFW
        {
            // NSFW is unlikely to be in the top ranking so tests will not work
        }
    }

    @Test // this test may fail if the Anime spans more than one airing season
    public void testAnimeSeason(){
        // test standard
        {
            final List<AnimePreview> season =
                mal.getAnimeSeason(2020, Season.Spring)
                    .withLimit(1)
                    .search();
            final AnimePreview anime = season.get(0);
            Assertions.assertEquals(2020, anime.getStartSeason().getYear());
            Assertions.assertEquals(Season.Spring, anime.getStartSeason().getSeason());
        }
        // test sort
        {
            final List<AnimePreview> season =
                mal.getAnimeSeason(2020, Season.Winter)
                    .withLimit(2)
                    .sortBy(AnimeSeasonSort.Score)
                    .search();
            final AnimePreview first = season.get(0);
            final AnimePreview second = season.get(1);
            Assertions.assertTrue(first.getMeanRating() > second.getMeanRating());
        }
        // test NSFW
        {
            final List<AnimePreview> season =
                mal.getAnimeSeason(2014, Season.Winter)
                    .search();
            boolean hasNSFW = false;
            for(final AnimePreview animePreview : season)
                if(animePreview.getNSFW() != NSFW.White)
                    hasNSFW = true;
            Assumptions.assumeTrue(hasNSFW, "API issue, disregard failure");
        }
    }

    @Test
    public void testAnimeSuggestions(){
        final List<AnimePreview> suggestions =
            mal.getAnimeSuggestions()
                .search();
        Assertions.assertNotNull(suggestions);
    }

    @Test @Disabled
    public void testUpdateAndDeleteAnimeListing(){
        // test delete
        {
            mal.deleteAnimeListing(13759);
            Assertions.assertNull(mal.getAnime(13759).getListStatus());
        }
        // update
        {
            final AnimeListStatus status = mal.updateAnimeListing(13759)
                .status(AnimeStatus.Completed)
                .score(10)
                .episodesWatched(24)
                .rewatching(false)
                .priority(2)
                .timesRewatched(0)
                .rewatchValue(5)
                .tags("ignore", "tags")
                .setComments("ignore comments")
                .update();

            Assertions.assertEquals(AnimeStatus.Completed, status.getStatus());
            Assertions.assertEquals(10, status.getScore());
            Assertions.assertEquals(24, status.getWatchedEpisodes());
            Assertions.assertFalse(status.isRewatching()); // weak test
            // Assertions.assertNotEquals(-1, status.getStartDate()); // will fail
            // Assertions.assertNotEquals(-1, status.getFinishDate()); // will fail
            Assertions.assertEquals(2, status.getPriority());
            Assertions.assertEquals(0, status.getTimesRewatched());
            Assertions.assertEquals(5, status.getRewatchValue());
            Assertions.assertTrue(Arrays.asList(status.getTags()).contains("ignore"));
            Assertions.assertTrue(Arrays.asList(status.getTags()).contains("tags"));
            Assertions.assertEquals("ignore comments", status.getComments());
            Assertions.assertNotEquals(-1, status.getUpdatedAt());
        }

        // test get
        { // fixme: API only returns limited fields, not all
            final List<AnimeListStatus> list =
                mal.getUserAnimeListing()
                    .withStatus(AnimeStatus.Completed)
                    .withLimit(1000)
                    .search();

            AnimeListStatus status = null;
            for(final AnimeListStatus userAnimeListStatus : list)
                if((status = userAnimeListStatus).getAnimePreview().getID() == 13759)
                    break;
            if(status == null)
                Assertions.fail();

            Assertions.assertEquals(AnimeStatus.Completed, status.getStatus());
            Assertions.assertEquals(10, status.getScore());
            Assertions.assertEquals(24, status.getWatchedEpisodes());
            Assertions.assertFalse(status.isRewatching()); // weak test
            // Assertions.assertNotEquals(-1, status.getStartDate());
            // Assertions.assertNotEquals(-1, status.getFinishDate());
            Assertions.assertEquals(2, status.getPriority());
            Assertions.assertEquals(0, status.getTimesRewatched());
            Assertions.assertEquals(5, status.getRewatchValue());
            Assertions.assertTrue(Arrays.asList(status.getTags()).contains("ignore"));
            Assertions.assertTrue(Arrays.asList(status.getTags()).contains("tags"));
            Assertions.assertEquals("ignore comments", status.getComments());
            Assertions.assertNotEquals(-1, status.getUpdatedAt());
        }
    }

    @Test
    public void testUserAnimeListing(){
        // test status
        {
            final List<AnimeListStatus> list =
                mal.getUserAnimeListing()
                   .withStatus(AnimeStatus.Dropped)
                   .search();
            Assertions.assertEquals(AnimeStatus.Dropped, list.get(0).getStatus());
        }
        // test sort
        {
            final List<AnimeListStatus> list =
                mal.getUserAnimeListing()
                   .sortBy(AnimeSort.UpdatedAt)
                   .search();
            Assertions.assertTrue(list.get(0).getUpdatedAt() > list.get(1).getUpdatedAt());
        }
    }

    // Forum

    @Test // test may fail if forum has no subboards
    public void testForumCategories(){
        // test standard
        {
            final ForumCategory category =
                mal.getForumBoards().get(0);

            Assertions.assertNotNull(category.getTitle());
            // board
            {
                final ForumBoard board = category.getForumBoards()[2];
                Assertions.assertNotEquals(-1, board.getID());
                Assertions.assertNotNull(board.getTitle());
                Assertions.assertNotNull(board.getDescription());
                // subboard
                {
                    final ForumSubBoard subBoard = board.getSubBoards()[0];
                    Assertions.assertNotEquals(-1, subBoard.getID());
                    Assertions.assertNotNull(subBoard.getTitle());
                }
            }
        }
    }

    @Test
    public void testForumTopicDetail(){
        // test standard
        {
            final ForumTopic topic = mal.getForumTopicDetail(481);
            Assertions.assertNotNull(topic.getTitle());
            // posts
            {
                final Post post = topic.getPosts()[0];
                Assertions.assertNotEquals(-1, post.getID());
                Assertions.assertNotEquals(-1, post.getNumber());
                Assertions.assertNotEquals(-1, post.getCreatedAt());
                Assertions.assertNotNull(post.getBody());
                Assertions.assertNotNull(post.getSignature());
                Assertions.assertSame(topic, post.getForumTopic());
            }
            // poll
            {
                final Poll poll = topic.getPoll();
                Assertions.assertNotEquals(-1, poll.getID());
                Assertions.assertNotNull(poll.getQuestion());
                Assertions.assertFalse(poll.isClosed()); // weak test
                // options
                {
                    final PollOption option = poll.getOptions()[0];
                    Assertions.assertNotEquals(-1, option.getID());
                    Assertions.assertNotNull(option.getText());
                    Assertions.assertNotEquals(-1, option.getVotes());
                    Assertions.assertSame(poll, option.getPoll());
                }
                Assertions.assertSame(topic, poll.getForumTopic());
            }
        }
    }

    @Test // todo
    public void testForumTopics(){
        // test standard
        {

        }
        // test limit & offset
        {
            final List<ForumTopicDetail> topics =
                mal.getForumTopics()
                    .withLimit(1)
                    .withOffset(1)
                    .search();
            Assertions.assertEquals(1, topics.size());
        }
        // id
        {

        }
        // test search
        {

        }
        // test topic name
        {

        }
        // test username
        {

        }
    }

    // Manga

    @Test
    public void testMangaSearch(){
        // test standard
        {
            final List<MangaPreview> search =
                mal.getManga()
                    .withQuery("ソードアートオンライン")
                    .search();

            Assertions.assertEquals(21479, search.get(0).getID());
            Assertions.assertNotEquals(1, search.size());
        }
        // test offset & limit
        {
            final List<MangaPreview> search =
                mal.getManga()
                    .withQuery("ソードアートオンライン")
                    .withLimit(1)
                    .withOffset(1)
                    .search();
            Assertions.assertNotEquals(21479, search.get(0).getID());
            Assertions.assertEquals(1, search.size());
        }
        // test fields
        {
            final List<MangaPreview> search =
                mal.getManga()
                    .withQuery("さくら荘のペットな彼女")
                    .withLimit(1)
                    .withFields(new String[0])
                    .search();
            Assertions.assertNull(search.get(0).getType());
        }
        // test NSFW
        {
            {
                final List<MangaPreview> search =
                    mal.getManga()
                        .withQuery("いただきっセーエキ")
                        .withLimit(1)
                        .search();
                Assertions.assertEquals(0, search.size());
            }
            {
                final List<MangaPreview> search =
                    mal.getManga()
                       .withQuery("いただきっセーエキ")
                       .withLimit(1)
                       .includeNSFW(true)
                       .search();
                Assertions.assertEquals(49697, search.get(0).getID());
            }
        }
        // test limit bounds
        { // seems to be an API issue
            try{
                Assumptions.assumeTrue(mal.getManga().withLimit(200).search() != null, "API issue, disregard failure");
                Assumptions.assumeTrue(mal.getManga().withOffset(-1).search() != null, "API issue, disregard failure");
            }catch(final InvalidParametersException e){
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testManga(){
        final Manga manga = mal.getManga(21479);
        Assertions.assertEquals(manga, manga.getManga());

        Assertions.assertEquals(21479, manga.getID());
        Assertions.assertNotNull(manga.getTitle());
        Assertions.assertNotNull(manga.getMainPicture().getMediumURL());
        Assertions.assertNotNull(manga.getMainPicture().getLargeURL());
        Assertions.assertNotNull(manga.getAlternativeTitles().getEnglish());
        Assertions.assertNotNull(manga.getAlternativeTitles().getJapanese());
        Assertions.assertNotNull(manga.getAlternativeTitles().getSynonyms());
        Assertions.assertNotEquals(-1, manga.getStartDate());
        Assertions.assertNotEquals(-1, manga.getEndDate());
        Assertions.assertNotNull(manga.getSynopsis());
        Assertions.assertNotEquals(-1, manga.getMeanRating());
        Assertions.assertNotEquals(-1, manga.getRank());
        Assertions.assertNotEquals(-1, manga.getPopularity());
        Assertions.assertNotEquals(-1, manga.getUserListingCount());
        Assertions.assertNotEquals(-1, manga.getUserScoringCount());
        Assertions.assertNotNull(manga.getNSFW());
        Assertions.assertNotNull(manga.getGenres());
        Assertions.assertNotEquals(-1, manga.getCreatedAt());
        Assertions.assertNotEquals(-1, manga.getUpdatedAt());
        Assertions.assertNotNull(manga.getType());
        Assertions.assertNotNull(manga.getStatus());
        Assertions.assertNotEquals(-1, manga.getVolumes());
        Assertions.assertNotEquals(-1, manga.getChapters());
        Assertions.assertNotEquals(-1, manga.getAuthors()[0].getID());
        Assertions.assertNotNull(manga.getAuthors()[0].getFirstName());
        Assertions.assertNotNull(manga.getAuthors()[0].getLastName());
        Assertions.assertNotNull(manga.getAuthors()[0].getRole());
        Assertions.assertNotNull(manga.getPictures()[0].getMediumURL());
        Assertions.assertNotNull(manga.getPictures()[0].getLargeURL());
        Assertions.assertNotNull(manga.getBackground());
        // related
        {
            final RelatedAnime relatedAnime = manga.getRelatedAnime()[0];
            Assertions.assertNotEquals(-1, relatedAnime.getAnimePreview().getID());
            Assertions.assertNotNull(relatedAnime.getRelationType());
            Assertions.assertNotNull(relatedAnime.getRelationTypeFormat());

            final RelatedManga relatedManga = manga.getRelatedManga()[0];
            Assertions.assertNotEquals(-1, relatedManga.getMangaPreview().getID());
            Assertions.assertNotNull(relatedManga.getRelationType());
            Assertions.assertNotNull(relatedManga.getRelationTypeFormat());
        }
        // recommendations
        {
            final MangaRecommendation recommendation = manga.getRecommendations()[0];
            Assertions.assertNotEquals(-1, recommendation.getMangaPreview().getID());
            Assertions.assertNotEquals(-1, recommendation.getRecommendations());
        }
        Assertions.assertNotEquals(-1, manga.getSerialization()[0].getID());
        Assertions.assertNotNull(manga.getSerialization()[0].getName());
        // list status
        {
            final MangaListStatus listStatus = manga.getListStatus();
            Assertions.assertNotNull(listStatus.getStatus());
            Assertions.assertNotEquals(-1, listStatus.getScore());
            Assertions.assertNotEquals(-1, listStatus.getVolumesRead());
            Assertions.assertNotEquals(-1, listStatus.getChaptersRead());
            Assertions.assertFalse(listStatus.isRereading()); // weak test
            Assertions.assertNotEquals(-1, listStatus.getStartDate());
            Assertions.assertNotEquals(-1, listStatus.getFinishDate());
            Assertions.assertNotEquals(-1, listStatus.getPriority());
            Assertions.assertNotEquals(-1, listStatus.getTimesReread());
            Assertions.assertNotEquals(-1, listStatus.getRereadValue());
            Assertions.assertNotNull(listStatus.getTags());
            Assertions.assertNotNull(listStatus.getComments());
            Assertions.assertNotEquals(-1, listStatus.getUpdatedAt());
        }
        Assumptions.assumeTrue(manga.getSerialization()[0].getRole() != null, "API issue, disregard failure"); // seems to be an API issue
    }

    @Test
    public void testMangaRank(){
        // test standard
        {
            final List<MangaRanking> ranking =
                mal.getMangaRanking(MangaRankingType.Manga)
                    .withLimit(1)
                    .search();
            final MangaRanking first = ranking.get(0);
            Assertions.assertEquals(1,first.getRanking());
            Assertions.assertTrue(first.getPreviousRank() < 1);
            Assertions.assertEquals(MangaType.Manga, first.getMangaPreview().getType());
        }
        // test NSFW
        {
            // NSFW is unlikely to be in top ranking so testing is not possible
        }
    }

    @Test @Disabled
    public void testUpdateAndDeleteMangaListing(){
        // test get
        {
            final MangaListStatus status =
                mal.getUserMangaListing()
                    .search()
                    .get(0);
            Assertions.assertNotNull(status.getStatus());
            Assertions.assertNotEquals(-1, status.getScore());
            Assertions.assertNotEquals(-1, status.getVolumesRead());
            Assertions.assertNotEquals(-1, status.getChaptersRead());
            Assertions.assertFalse(status.isRereading()); // weak test
            Assertions.assertNotEquals(-1, status.getStartDate());
            Assertions.assertNotEquals(-1, status.getFinishDate());
            Assertions.assertNotEquals(-1, status.getPriority());
            Assertions.assertNotEquals(-1, status.getTimesReread());
            Assertions.assertNotEquals(-1, status.getRereadValue());
            Assertions.assertNotNull(status.getTags());
            Assertions.assertNotNull(status.getComments());
            Assertions.assertNotEquals(-1, status.getUpdatedAt());
        }

        // todo: delete

        // todo: update
    }

    @Test
    public void testUserMangaListing(){
        // test status
        {
            final List<MangaListStatus> list =
                mal.getUserMangaListing()
                   .withStatus(MangaStatus.PlanToRead)
                   .withLimit(1)
                   .search();
            Assertions.assertEquals(MangaStatus.PlanToRead, list.get(0).getStatus());
        }
        // test sort
        {
            final List<MangaListStatus> list =
                mal.getUserMangaListing()
                   .sortBy(MangaSort.UpdatedAt)
                   .withLimit(2)
                   .search();
            Assertions.assertTrue(list.get(0).getUpdatedAt() > list.get(1).getUpdatedAt());
        }
    }

    // User

    @Test
    public void testUser(){
        final User user = mal.getMyself();
        // Assertions.assertEquals(user.getID(), mal.getUser("_Katsute_").getID()); // according to API only @me can be used

        Assertions.assertNotEquals(-1, user.getID());
        Assertions.assertNotNull(user.getName());
        Assertions.assertNotNull(user.getPictureURL());
        Assertions.assertNotNull(user.getGender());
        Assertions.assertNotNull(user.getLocation());
        Assertions.assertNotEquals(-1, user.getJoinedAt());
        // anime statistics
        {
            final UserAnimeStatistics statistics = user.getAnimeStatistics();
            Assertions.assertNotEquals(-1, statistics.getItemsWatching());
            Assertions.assertNotEquals(-1, statistics.getItemsCompleted());
            Assertions.assertNotEquals(-1, statistics.getDaysOnHold());
            Assertions.assertNotEquals(-1, statistics.getItemsPlanToWatch());
            Assertions.assertNotEquals(-1, statistics.getItems());
            Assertions.assertNotEquals(-1, statistics.getDaysWatched());
            Assertions.assertNotEquals(-1, statistics.getDaysWatching());
            Assertions.assertNotEquals(-1, statistics.getDaysCompleted());
            Assertions.assertNotEquals(-1, statistics.getDaysOnHold());
            Assertions.assertNotEquals(-1, statistics.getDaysDropped());
            Assertions.assertNotEquals(-1, statistics.getDays());
            Assertions.assertNotEquals(-1, statistics.getEpisodes());
            Assertions.assertNotEquals(-1, statistics.getTimesRewatched());
            Assertions.assertNotEquals(-1, statistics.getMeanScore());
        }
        Assertions.assertNotNull(user.getTimeZone());
        Assertions.assertFalse(user.isSupporter());

        Assertions.assertNotEquals(-1, user.getBirthday(), "API or privacy issue, disregard failure"); // unknown issue
    }

}
