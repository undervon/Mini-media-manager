FROM openjdk:11
EXPOSE 8090
COPY build/libs/mini.media.manager-0.0.1-SNAPSHOT.jar mini.media.manager.jar
ENTRYPOINT ["java", "-jar", "mini.media.manager.jar"]

#
# Command:
#
# docker build -t mini-media-manager-app .
#