# Stage that builds the application
FROM maven:3-openjdk-17-slim as build

# Stop running as root at this point
RUN useradd -m lunch-buddy
WORKDIR /usr/src/app/
RUN chown lunch-buddy:lunch-buddy /usr/src/app/
USER lunch-buddy

# Copy pom.xml and prefetch dependencies so a repeated build can continue from the next step with existing dependencies
COPY --chown=lunch-buddy pom.xml ./
RUN mvn dependency:go-offline -Pproduction

# Copy all needed project files to a folder
COPY --chown=lunch-buddy:lunch-buddy src src

# Build the production package, assuming that we validated the version before so no need for running tests again
RUN mvn clean package -DskipTests -Pproduction

# Running stage: the part that is used for running the application
FROM openjdk:17-jdk-slim
COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar
RUN useradd -m lunch-buddy
USER lunch-buddy
EXPOSE 3000
CMD java -jar /usr/app/app.jar