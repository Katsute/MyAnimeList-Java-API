/*
 * Copyright (C) 2021-2022 Katsute <https://github.com/Katsute>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.kttdevelopment.mal4j.anime.property;

import com.kttdevelopment.mal4j.Logging;
import com.kttdevelopment.mal4j.anime.AnimePreview;
import com.kttdevelopment.mal4j.property.FieldEnum;

/**
 * Represents the source material for an Anime.
 *
 * @see AnimePreview#getSource()
 * @since 1.0.0
 * @version 2.9.0
 * @author Katsute
 */
@SuppressWarnings("SpellCheckingInspection")
public enum AnimeSource implements FieldEnum {

    Unknown         ("unknown"),

    Other           ("other"),
    Original        ("original"),
    Manga           ("manga"),
    FourKomaManga   ("4_koma_manga"),
    /**
     * @deprecated use {@link #WebManga}
     */
    @Deprecated
    Web_Manga       ("web_manga"),
    WebManga        ("web_manga"),
    /**
     * @deprecated use {@link #DigitalManga}
     */
    @Deprecated
    Digital_Manga   ("digital_manga"),
    DigitalManga    ("digital_manga"),
    Novel           ("novel"),
    LightNovel      ("light_novel"),
    VisualNovel     ("visual_novel"),
    Game            ("game"),
    CardGame        ("card_game"),
    Book            ("book"),
    PictureBook     ("picture_book"),
    Radio           ("radio"),
    Music           ("music"),

    MixedMedia      ("mixed_media"),
    WebNovel        ("web_novel");

    private final String field;

    AnimeSource(String field) {
        this.field = field;
    }

    @Override
    public final String field(){
        return field;
    }

    /**
     * Returns the field name as an enum.
     *
     * @param string json field name
     *
     * @return enum
     *
     * @since 1.0.0
     */
    public static AnimeSource asEnum(final String string){
        if(string != null){
            for(final AnimeSource value : values())
                if(!value.name().contains("_") && value.field.equalsIgnoreCase(string))
                    return value;
            Logging.getLogger().warning(String.format("Unrecognized Anime source '%s', please report this to the maintainers of Mal4J", string));
        }

        return Unknown;
    }

    @Override
    public final String toString(){
        return name();
    }

}
