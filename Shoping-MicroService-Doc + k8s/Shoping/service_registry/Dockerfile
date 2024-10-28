FROM openjdk:22
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} service-registry
ENTRYPOINT ["java","-jar","service-registry"]
EXPOSE 8761