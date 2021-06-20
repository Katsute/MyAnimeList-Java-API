# General

### Does this library support Java # ?

This project supports Java 8+ and Java 9 modules.

### WARNING: An illegal reflective access operation has occurred

_(this warning does not affect the library in any way)_

In order to make this library function on JDK 8-10, reflection is used in order to make PATCH requests work correctly.

If you don't want this warning then either:
- Downgrade to JDK 8
- Upgrade to JDK 11 or higher

### `UnsupportedClassVersionError`

This issue is caused by using an older, unsupported Java version; this library requires at least Java 8.

### `AndroidCompatibilityException` / `ClassNotFoundException` / `NoSuchMethodError`
This issue may occur due to Android incompatibilities (blame Google), please open a new issue if this happens.

### `InaccessibleObjectException` / `IllegalStateException: Reflect module is not accessible in JDK 9+`

In order to make this library function on JDK 9-10, reflection is used in order to make PATCH requests work correctly.

To avoid this exception either:
- Downgrade to JDK 8
- Upgrade to JDK 11 or higher
- Add `--add-opens java.base/java.lang.reflect=Mal4J --add-opens java.base/java.net=Mal4J` to VM options
- Make the project not modular (remove `module-info.java`)

# API

### Are their any rate limits?

The MyAnimeList API currently has no rate limit in place so requests must be sent at your own discretion.

### What does this library offer in comparison to the Official API?

This library offers ***ALL*** the features provided by the API and even some *undocumented* fields.

### My auth token doesn't work.

- Make sure that you are providing an auth token and not the client id.
- Your token may be expired.
- Your token is missing '`Bearer `'.
- Your token may contain dangling whitespace.

### Incorrect date is returned.

Java does not support null date fields; for Anime/Manga that is yet to start, an unknown month will be returned as January, and an unknown day will be returned as 1.

### Seasons query is returning Anime from other seasons.

The seasons query returns Anime that are airing in the current season, this includes Anime that may have started the season before and are still airing this season.

### NSFW is not working.

For search queries make sure you also run [`#includeNSFW()`](https://mal4j.katsute.dev/Mal4J/com/kttdevelopment/mal4j/query/NSFW.html#includeNSFW()) in the query builder.

### I can't get other users.

Currently the MyAnimeList API does not allow you to check users other than yourself.

### `list_status` / `my_list_status` field isn't working.

For some requests `my_list_status` field may be required instead of `list_status` or vice-versa. Try using the other field if responses are null.
