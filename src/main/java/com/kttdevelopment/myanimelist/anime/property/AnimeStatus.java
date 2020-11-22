package com.kttdevelopment.myanimelist.anime.property;

/**
 * Represents an Anime status.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Ktt Development
 */
public enum AnimeStatus {

    Watching("watching"),
    Completed("completed"),
    OnHold("on_hold"),
    Dropped("dropped"),
    PlanToWatch("plan_to_watch");

    private final String status;

    AnimeStatus(final String status){
        this.status = status;
    }

    /**
     * Returns the status field name
     *
     * @return field name
     *
     * @since 1.0.0
     */
    public final String getStatus(){
        return status;
    }

    @Override
    public String toString(){
        return "AnimeStatus{" +
               "status='" + status + '\'' +
               '}';
    }

}
