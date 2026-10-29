FROM eclipse-temurin:25.0.5_8-jre-alpine

RUN apk add --no-cache curl tar ffmpeg

RUN curl -L https://github.com/kkdai/youtube/releases/download/v2.10.6/youtubedr_2.10.6_linux_amd64.tar.gz -o /tmp/youtubedr.tar.gz && \
    tar -xzf /tmp/youtubedr.tar.gz -C /usr/local/bin youtubedr && \
    chmod +x /usr/local/bin/youtubedr && \
    rm /tmp/youtubedr.tar.gz

VOLUME /tmp

ARG JAR_FILE
ADD target/${JAR_FILE} app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT [ "sh", "-c", "java -jar /app.jar" ]
