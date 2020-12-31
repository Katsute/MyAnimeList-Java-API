package com.kttdevelopment.myanimelist;

import java.lang.annotation.*;
import java.lang.reflect.Method;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Annotations used to structure API calls.
 *
 * @see APICall
 * @see APICall#APICall(String, Method, Object...)
 */
abstract class APIStruct {

    @Documented
    @Target(METHOD)
    @Retention(RUNTIME)
    @interface Endpoint {

        String method() default "GET";

        String value();

    }

    @Documented
    @Target(PARAMETER)
    @Retention(RUNTIME)
    @interface Path {

        String value();

        boolean encoded() default false;

    }

    @Documented
    @Target(METHOD)
    @Retention(RUNTIME)
    @interface FormUrlEncoded {

    }

    @Documented
    @Target(PARAMETER)
    @Retention(RUNTIME)
    @interface Header {

        String value();

    }

    @Documented
    @Target(PARAMETER)
    @Retention(RUNTIME)
    @interface Query {

        String value();

        boolean encoded() default false;

    }

    @Documented
    @Target(PARAMETER)
    @Retention(RUNTIME)
    @interface Field {

        String value();

        boolean encoded() default false;

    }

    /**
     * Represents HTTP response.
     *
     * @param <T> response type
     */
    public static final class Response<T>{

        private final String URL;
        private final String raw;
        private final T response;
        private final int code;

        Response(final String URL, final String raw, final T response, final int code){
            this.URL        = URL;
            this.raw        = raw;
            this.response   = response;
            this.code       = code;
        }

        final String URL(){
            return URL;
        }

        final String raw(){
            return raw;
        }

        final T body(){
            return response;
        }

        final int code(){
            return code;
        }

        @Override
        public String toString(){
            return "Response{" +
                   "response=" + response +
                   ", code=" + code +
                   '}';
        }

    }

}
