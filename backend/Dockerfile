FROM eclipse-temurin:25-jdk AS build
WORKDIR /src
COPY . .
RUN ./gradlew --no-daemon clean installDist

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /src/build/install/gympandro-backend/ /app/
EXPOSE 8081
ENTRYPOINT ["/app/bin/gympandro-backend"]