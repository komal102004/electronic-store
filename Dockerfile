FROM ubuntu:latest
LABEL authors="Admin"
EXPOSE 8000
ADD target/electronic-store-01.jar electronic-store-01.jar

ENTRYPOINT ["java", "-jar", "/electronic-store-01.jar"]
