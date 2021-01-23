/*
 * Copyright (C) 2021 Ktt Development
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

package com.kttdevelopment.mal4j;

/**
 * Thrown if the server does not return a 200 OK response.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Ktt Development
 */
public class HttpException extends RuntimeException {

    private final String URL, message;
    private final int code;

    HttpException(final String URL, final int code, final String message){
        super("Server returned code " + code + " from '" + URL + "': " + message);
        this.URL = URL;
        this.code = code;
        this.message = message;
    }

    public final String URL(){
        return URL;
    }

    public final String message(){
        return message;
    }

    public final int code(){
        return code;
    }

}
