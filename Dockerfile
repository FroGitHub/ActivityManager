# --- Builder stage: Maven компіляція ---
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

# Робоча директорія для збірки
WORKDIR /app

# Копіюємо все (pom.xml + src + інше)
COPY . .

# Компіляція проєкту, пропускаємо тести
RUN mvn clean package -DskipTests


# --- Final stage: легкий JDK для запуску ---
FROM openjdk:17-jdk-alpine

# Робоча директорія контейнера
WORKDIR /app

# Копіюємо зібраний jar з builder-стейджа
COPY --from=builder /app/target/*.jar app.jar

# Команда запуску
ENTRYPOINT ["java", "-jar", "app.jar"]

# Відкриваємо порт (за потреби)
EXPOSE 8080
