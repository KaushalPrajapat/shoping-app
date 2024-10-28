FROM openjdk:22
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} order-service
ENTRYPOINT ["java","-jar","order-service"]
EXPOSE 8761