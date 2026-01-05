# Utiliza uma imagem do Maven com OpenJDK 11 para compilar e correr a aplicação
FROM maven:3.8.5-openjdk-11-slim

# Define o diretório de trabalho dentro do contentor
WORKDIR /app

# Copia o ficheiro pom.xml para descarregar as dependências primeiro (otimização de cache)
COPY pom.xml .

# Descarrega as dependências necessárias sem compilar o código
RUN mvn dependency:go-offline -B

# Copia o código fonte (pasta src) para o contentor
COPY src ./src

# Compila a aplicação e gera o pacote (ignora os testes para ser mais rápido)
RUN mvn package -DskipTests

# Comando para executar a aplicação utilizando o plugin exec-maven-plugin
# definido no ficheiro pom.xml
CMD ["mvn", "exec:java"]