# === Builder stage: використовує Maven для збірки ===
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app

# Копіюємо pom.xml і завантажуємо залежності (оптимізація кешу)
COPY pom.xml .
RUN mvn dependency:go-offline

# Копіюємо решту проекту і збираємо jar
COPY . .
RUN mvn clean package -DskipTests

# === Final stage: легкий образ з готовим jar ===
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Копіюємо зібраний jar з builder-стейджу
COPY --from=builder /app/target/*.jar app.jar

# Порт і запуск
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
