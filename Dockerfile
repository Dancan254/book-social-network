#build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

#run stage
FROM eclipse-temurin:21

ARG PROFILE=prod
ARG APP_VERSION=1.0.0

WORKDIR /app
# Copy and rename the JAR to have a predictable name
COPY --from=build /build/target/book-social-network-*.jar /app/app.jar

EXPOSE 8088
# Set environment variables for database connection
ENV DB_URL=${JDBC_URL}
ENV ACTIVE_PROFILE=prod
ENV DB_USERNAME=${JDBC_USERNAME}
ENV DB_PASSWORD=${JDBC_PASSWORD}
ENV JAR_VERSION=${APP_VERSION}
ENV EMAIL_HOSTNAME=${MAIL_HOST}
ENV EMAIL_USERNAME=${MAIL_USERNAME}
ENV EMAIL_PASSWORD=${MAIL_PASSWORD}

# Add wait-for-it script to ensure database is ready
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Use specific JAR name that matches what was copied
CMD java -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} -Dspring.datasource.username=${DB_USERNAME} -Dspring.datasource.password=${DB_PASSWORD} -jar /app/app.jar
