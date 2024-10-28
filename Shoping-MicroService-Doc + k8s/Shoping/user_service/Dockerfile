FROM openjdk:22
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} user_service
ENTRYPOINT ["java","-jar","user-service"]
EXPOSE 8761