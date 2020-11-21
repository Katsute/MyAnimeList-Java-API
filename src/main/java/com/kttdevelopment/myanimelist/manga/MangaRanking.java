package com.kttdevelopment.myanimelist.manga;

@SuppressWarnings("SpellCheckingInspection")
public enum MangaRanking {

    All("all"),
    Manga("manga"),
    Novels("novels"),
    OneShots("oneshots"),
    Doujin("doujin"),
    Manhwa("manhwa"),
    Manhua("manhua"),
    ByPopularity("bypopularity"),
    Favorite("favorite");

    private final String ranking;

    MangaRanking(final String ranking){
        this.ranking = ranking;
    }

    public final String getRanking(){
        return ranking;
    }

    @Override
    public String toString(){
        return "MangaRanking{" +
               "ranking='" + ranking + '\'' +
               '}';
    }

}
