# PMS - Personal Music Stream

A Spring Boot application for streaming personal music from YouTube playlists.

## Prerequisites

- **Java 25** (Temurin) - Install via SDKMAN: `sdk install java 25-tem`
- **Maven 3.9+**
- **Docker** (optional, for building container images)

## Build & Run

```bash
# Compile
mvn clean package

# Run locally
mvn spring-boot:run

# Build Docker image
mvn spring-boot:build-image

# Run Docker container
docker run -d -p 8080:8080 pms:0.1.0-SNAPSHOT

# View container logs
docker logs pms:0.1.0-SNAPSHOT

# Follow logs in real-time
docker logs -f pms:0.1.0-SNAPSHOT
```

## Testing

### Run All Tests

```bash
mvn test
```

### Run Integration Test

The `YouTubeDownloadServiceTest` is disabled by default as it requires `youtubedr` and network access.

**Install youtubedr:**
```bash
brew install youtubedr
```

**Run the integration test:**
```bash
mvn test -Dtest=YouTubeDownloadServiceTest
```

This test downloads a short YouTube video (Rick Astley - Never Gonna Give You Up) to verify the downloader works.

## Configuration

The application uses Spring Boot profiles for environment-specific configuration. The default profile is `dev`.

Activate a profile:
```bash
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
```

### Creating a Custom Profile

Copy `application-dev.properties` to `application-{yourprofile}.properties` and adjust the settings:

**Example: Rick Astley playlist profile**
```properties
# Application paths
pms.filesFolder=/path/to/your/music/folder
pms.hostName=http://localhost:8080
pms.applicationPath=
pms.author=rick-fan

# YouTube downloader (youtubedr)
pms.yt.downloader.executable=youtubedr
pms.yt.downloader.extraOptions[0]=--playlist-end
pms.yt.downloader.extraOptions[1]=10
pms.yt.downloader.extraOptions[2]=--reject-title
pms.yt.downloader.extraOptions[3]=reupload

# Playlist URL (Rick Astley - Never Gonna Give You Up)
pms.yt.playlist.urls[0]=https://www.youtube.com/watch?v=8qfT9JSBL3w&list=UUAXZxrcdp8kM4C18PfTeR-Q
```

**Configuration options:**
- `pms.filesFolder` - Local directory where downloaded videos and metadata are stored
- `pms.hostName` - Base URL of your server (e.g., `http://localhost:8080` or `https://yourdomain.com`)
- `pms.applicationPath` - Optional path prefix (e.g., `/music` for `http://localhost:8080/music`)
- `pms.author` - Display name shown in the UI
- `pms.yt.downloader.executable` - YouTube downloader command (`youtubedr`)
- `pms.yt.downloader.extraOptions` - Additional options passed to youtubedr
- `pms.yt.playlist.urls` - YouTube playlist URLs to download (can have multiple entries)

## Endpoints

### Web UI
- `/player` - List all available mixes
- `/player/{mix_id}` - Play a specific mix

### API
- `/mixes` - List all available mixes (JSON)
- `/mixes/{id}` - Get a specific mix (JSON)
- `/rss` - RSS feed of mixes
- `/file/{fileName}` - Stream audio file
