FROM gradle:7.4.2-jdk17 as build
WORKDIR /app
COPY --chown=gradle:gradle . ./
RUN gradle build -x test --no-daemon

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/build/libs/user-0.0.1-SNAPSHOT.jar /spring-boot-application.jar

ARG POSTGRES_HOSTS
ARG POSTGRES_DB
ARG POSTGRES_USER
ARG POSTGRES_PWD

ENV POSTGRES_HOSTS = ${POSTGRES_HOSTS}
ENV POSTGRES_DB = ${POSTGRES_DB}
ENV POSTGRES_USER = ${POSTGRES_USER}
ENV POSTGRES_PWD = ${POSTGRES_PWD}

ENTRYPOINT ["java","-jar","/spring-boot-application.jar"]