# ---- Build stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew \
 && ./gradlew -q clean build -x test \
 && JAR="$(ls -1 build/libs/*.jar | head -n 1)" \
 && cp "$JAR" app.jar

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/app.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS=""
CMD ["sh","-c","java $JAVA_OPTS -jar app.jar"]
