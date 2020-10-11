FROM gradle:jdk8
WORKDIR /usr/src/app
COPY . .
RUN gradle build --no-daemon

FROM openjdk:8-jre-alpine
EXPOSE 8000
WORKDIR /usr/src/app
COPY --from=0 /usr/src/app/build/libs/app-all.jar .
USER nobody
CMD [ "java", "-jar", "app-all.jar" ]
