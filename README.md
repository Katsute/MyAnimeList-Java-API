<div align="center">
    <a href="https://github.com/KatsuteDev/Mal4J#readme">
        <img src="https://raw.githubusercontent.com/KatsuteDev/Mal4J/main/banner.png" alt="Mal4J - MyAnimeList for Java">
    </a>
    <h3>Java wrapper for the official MyAnimeList API</h3>
    <div>
        <a href="https://docs.katsute.dev/mal4j">Docs</a>
        •
        <a href="https://myanimelist.net/apiconfig/references/api/v2">API Docs</a>
        •
        <a href="https://github.com/KatsuteDev/Mal4J/blob/main/setup.md">Setup</a>
        •
        <a href="https://github.com/KatsuteDev/Mal4J/blob/main/faq.md">FAQ</a>
        •
        <a href="https://github.com/KatsuteDev/Mal4J/issues">Issues</a>
        •
        <a href="https://github.com/KatsuteDev/Mal4J/discussions">Discussions</a>
        •
        <a href="https://myanimelist.net/forum/?topicid=1897569">Forum Post</a>
    </div>
</div>
<br>
<div align="center">
    <a href="https://github.com/KatsuteDev/Mal4J/actions/workflows/java_ci.yml"><img alt="Java CI" src="https://github.com/KatsuteDev/Mal4J/actions/workflows/java_ci.yml/badge.svg"></a>
    <a href="https://github.com/KatsuteDev/Mal4J/actions/workflows/release.yml"><img alt="Deploy" src="https://github.com/KatsuteDev/Mal4J/actions/workflows/release.yml/badge.svg"></a>
    <a href="https://mvnrepository.com/artifact/com.kttdevelopment/mal4j"><img alt="Maven Central" src="https://img.shields.io/maven-central/v/com.kttdevelopment/mal4j"></a>
    <a href="https://github.com/KatsuteDev/Mal4J/releases"><img alt="version" src="https://img.shields.io/github/v/release/KatsuteDev/Mal4J"></a>
    <a href="https://github.com/KatsuteDev/Mal4J/blob/main/LICENSE"><img alt="license" src="https://img.shields.io/github/license/KatsuteDev/Mal4J"></a>
    <br>
    <a href="https://github.com/KatsuteDev/Mal4J/actions/workflows/mal_ci.yml"><img alt="MyAnimeList CI" src="https://github.com/KatsuteDev/Mal4J/actions/workflows/mal_ci.yml/badge.svg"></a>
    <a href="https://myanimelist.net/"><img alt="myanimelist.net" src="https://img.shields.io/website?label=myanimelist.net&logo=myanimelist&url=https%3A%2F%2Fmyanimelist.net%2F"></a>
</div>

<br>

<div align="center">
    <table>
        <thead>
            <tr>
                <th>❗ ❗ This repository will be moving to <a href="https://github.com/KatsuteDev/Mal4J">KatsuteDev/Mal4J</a> soon, more information in this <a href="https://github.com/Katsute/Mal4J/discussions/230">discussion post</a></th>
            </tr>
        </thead>
    </table>
</div>

# Overview

Mal4J is a modular wrapper for the MyAnimeList API written for Java 8+ and simplifies many complex operations into an easy to use library.

# [Setup](https://github.com/KatsuteDev/Mal4J/blob/main/setup.md)

Dependencies are hosted on Maven Central and compiled builds can be found in releases.

**Mal4J requires at least Java 8.**

## Maven Central

Compiled binaries can be found on Maven Central → [![Maven Central](https://img.shields.io/maven-central/v/com.kttdevelopment/mal4j)](https://mvnrepository.com/artifact/com.kttdevelopment/mal4j)

## Local

For projects built locally, jars can be found in releases → [![Releases](https://img.shields.io/github/v/release/KatsuteDev/Mal4J)](https://github.com/KatsuteDev/Mal4J/releases)

Mal4J is a standalone library and requires no additional dependencies.

### More information on further stages of the setup can be found [here](https://github.com/KatsuteDev/Mal4J/blob/main/setup.md).

# Features

### 🔎 Search Queries

Easily search through MyAnimeList with search, ranking, seasonal, and suggestion queries; returning only selected or all fields.

```java
MyAnimeList mal = MyAnimeList.withOAuthToken("");
List<AnimePreview> search =
    mal.getAnime()
        .withQuery("さくら荘のペットな彼女")
        .withLimit(1)
        .withOffset(1)
        .includeNSFW(false)
        .search();
```

### 📋 List Modification
Easily update your Anime and Manga listings through update methods.

```java
MyAnimeList mal = MyAnimeList.withOAuthToken("");
MangaListStatus status =
    mal.updateMangaListing(28107)
        .status(MangaStatus.Reading)
        .volumesRead(7)
        .chaptersRead(2)
        .rereading(false)
        .priority(Priority.High)
        .timesReread(0)
        .rereadValue(RereadValue.VeryHigh)
        .tags("tags", "more tags")
        .comments("comments")
        .update();
```

### 📦 Structured Objects

**All** information provided in the [MyAnimeList API](https://myanimelist.net/apiconfig/references/api/v2) including Anime, Manga, forums, genres, pictures, statistics, and even some *undocumented* fields are accessible in this library. Effortlessly retrieve any and all information you need.

```java
MyAnimeList mal = MyAnimeList.withOAuthToken("");
Anime anime = mal.getAnime(13759);

String ja = anime.getAlternativeTitles().getJapanese();
Genre[] genres = anime.getGenres();
RelatedAnime[] relatedAnime = anime.getRelatedAnime();
AnimeRecommendation[] recs = anime.getRecommendations();
OpeningTheme[] op = anime.getOpeningThemes();
```

## Contributing

### Local Tests

For local tests you can use Java 8+, however only methods in the Java 8 API may be used. The `src/main/java9` and `src/main/java11` folders should not be marked as a source root.

Run tests locally by adding a text file named `client.txt` that contains the client ID in the `src/test/java/resources` directory.

Please note that the client ID being used for tests must not have a client secret and must have an app redirect url of `http://localhost:5050`.

### Remote Tests

Devs running remote tests may do so by running the `MyAnimeList CI` workflow manually in the actions tab of your fork. Note that this requires a secret `MAL_OAUTH` which contains the OAuth token (ex: `Bearer <oauth token>`).

**Known test failures:** See [**⚠ External Issues**](https://github.com/Katsute/Mal4J/projects/10)

---

- Found a bug? Post it in [issues](https://github.com/KatsuteDev/Mal4J/issues).
- Have a suggestion or looking for inspiration? Check out our [discussions](https://github.com/KatsuteDev/Mal4J/discussions).
- Want to further expand our project or site? [Fork](https://github.com/KatsuteDev/Mal4J/fork) this repository and submit a [pull request](https://github.com/KatsuteDev/Mal4J/pulls).

## Disclaimer
- [@Katsute](https://github.com/Katsute) is not affiliated with MyAnimeList.
- By using the MyAnimeList API you are subject to their [Terms Of Service](https://myanimelist.net/static/apiagreement.html).
