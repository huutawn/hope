# Stage 1: build
FROM maven:3.9.9-amazoncorretto-21 AS build

# Đặt thư mục làm việc
WORKDIR /app

# Copy pom.xml và tải dependency trước để cache tốt hơn
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy toàn bộ source code
COPY src ./src

# Build source code với Maven
RUN mvn clean package -DskipTests

# Stage 2: create image
FROM amazoncorretto:21.0.6

# Set working folder to /app
WORKDIR /app

# Copy file JAR từ stage build
COPY --from=build /app/target/*.jar app.jar

# Command để chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
