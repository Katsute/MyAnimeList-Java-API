package com.kttdevelopment.myanimelist.anime.property;

import com.kttdevelopment.myanimelist.property.RankingType;

/**
 * Represents Anime ranking types.
 *
 * @see AnimeType
 * @see RankingType
 * @since 1.0.0
 * @version 1.0.0
 * @author Ktt Development
 */
public enum AnimeRankingType {

    Airing("airing"),
    Upcoming("upcoming"),

    TV(AnimeType.TV.getType()),
    OVA(AnimeType.OVA.getType()),
    Movie(AnimeType.Movie.getType()),
    Special(AnimeType.Special.getType()),

    All(RankingType.All.getType()),
    ByPopularity(RankingType.ByPopularity.getType()),
    Favorite(RankingType.Favorite.getType());

    private final String type;

    AnimeRankingType(final String type){
        this.type = type;
    }

    /**
     * Returns the type field name
     *
     * @return field name
     *
     * @since 1.0.0
     */
    public final String getType(){
        return type;
    }

    @Override
    public String toString(){
        return "AnimeRankingType{" +
               "type='" + type + '\'' +
               '}';
    }
}
