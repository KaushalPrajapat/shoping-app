FROM openjdk:22
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} config-server
ENTRYPOINT ["java","-jar","config-server"]
EXPOSE 8761