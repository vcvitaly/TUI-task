FROM maven:3-amazoncorretto-17 as build
WORKDIR /app

COPY ../pom.xml ./
RUN mvn dependency:resolve
COPY ../src src

RUN mvn clean package

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/target/tui-task-0.0.1-SNAPSHOT.jar ./app.jar

ENTRYPOINT java -jar -Dspring.profiles.active=$APPLICATION_PROFILE \
                      app.jar