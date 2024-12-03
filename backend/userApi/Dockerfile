# Base image
FROM ubuntu:22.04

# Define environment variables
ENV DEBIAN_FRONTEND=noninteractive \
    JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 \
    PATH="$JAVA_HOME/bin:$PATH"

# Atualiza os pacotes e instala dependências
RUN apt-get update && apt-get install -y --no-install-recommends \
    openjdk-17-jdk \
    maven \
    curl \
    unzip \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia os arquivos do projeto para o container
COPY . .

# Compila o projeto usando o Maven
RUN mvn clean package -DskipTests

# Define a porta exposta pelo container
EXPOSE 8080

# Comando para iniciar a aplicação
CMD ["java", "-jar", "target/user-api-0.0.1-SNAPSHOT.jar"]
