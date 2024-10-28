FROM openjdk:22
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} product-service
ENTRYPOINT ["java","-jar","product-service"]
EXPOSE 8761