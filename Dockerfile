FROM eclipse-temurin:17-jdk-jammy
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew $APP_HOME/
COPY gradle $APP_HOME/gradle
COPY src $APP_HOME/src
RUN ./gradlew --no-daemon build
COPY src ./src
CMD ["./gradlew", "bootRun"]