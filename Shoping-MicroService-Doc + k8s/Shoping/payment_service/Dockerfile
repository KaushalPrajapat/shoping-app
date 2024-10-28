FROM openjdk:22
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} payment-service
ENTRYPOINT ["java","-jar","payment-service"]
EXPOSE 8761