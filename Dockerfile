FROM adoptopenjdk/openjdk11:alpine
VOLUME /tmp

ARG JAR_FILE
ADD target/${JAR_FILE} app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT [ "sh", "-c", "java -jar /app.jar" ]
