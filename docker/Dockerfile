FROM node:22.12 AS angular

WORKDIR /code

COPY frontend/package*.json frontend/angular.json frontend/tsconfig*.json /code/
COPY frontend/public /code/public

RUN npm install

COPY frontend/src /code/src

RUN npm run build -- --configuration production 

FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /project

COPY backend/pom.xml /project/ 

RUN mvn dependency:go-offline 

COPY backend/src /project/src
COPY --from=angular /code/dist/frontend/browser/ /project/src/main/resources/static/

RUN mvn package -DskipTests=true

FROM eclipse-temurin:21-jre-jammy

RUN apt-get update && apt-get install -y \
    libfreetype6 \
    libfontconfig1 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /usr/app/

COPY --from=builder /project/target/*.jar /usr/app/

EXPOSE 8443

CMD [ "java", "-jar", "backend-0.0.1-SNAPSHOT.jar" ]