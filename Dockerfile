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
ENV JDBC_URL=${JDBC_URL}
ENV SPRING_PROFILES_ACTIVE=prod
ENV JDBC_USERNAME=${JDBC_USERNAME}
ENV JDBC_PASSWORD=${JDBC_PASSWORD}
ENV JAR_VERSION=${APP_VERSION}
ENV MAIL_HOST=${MAIL_HOST}
ENV MAIL_USERNAME=${MAIL_USERNAME}
ENV MAIL_PASSWORD=${MAIL_PASSWORD}
ENV SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect

# Add wait-for-it script to ensure database is ready
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Use specific JAR name that matches what was copied
CMD echo "Starting application with profile: ${SPRING_PROFILES_ACTIVE}" && \
    echo "JDBC URL: ${JDBC_URL}" && \
    java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \
         -Dspring.datasource.url=${JDBC_URL} \
         -Dspring.datasource.username=${JDBC_USERNAME} \
         -Dspring.datasource.password=${JDBC_PASSWORD} \
         -Dspring.jpa.properties.hibernate.dialect=${SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT} \
         -jar /app/app.jar
