FROM openjdk:22
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} api-gateway
ENTRYPOINT ["java","-jar","api-gateway"]
EXPOSE 8761