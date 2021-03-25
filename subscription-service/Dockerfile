FROM adoptopenjdk/openjdk11:alpine-jre
ENV APP_HOME=/usr/app/
COPY build/libs/*.jar service.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","/service.jar"]
