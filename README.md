

# Terminal File Explorer

Bem-vindo ao Terminal File Explorer, um gestor de ficheiros baseado em texto (TUI) desenvolvido em Java. Este guia explica como compilar, executar e utilizar as funcionalidades da aplicação.

## 🚀 Como Iniciar (via Docker)

A forma mais simples de executar a aplicação é utilizando o Docker, o que garante que todas as dependências (Java 11, Maven) estão configuradas corretamente.

### 1. Construir a Imagem

Na raiz do projeto (onde se encontra o ficheiro `Dockerfile`), abre o terminal e executa o seguinte comando para criar a imagem Docker:

```bash
docker build -t terminal-file-explorer .
```

Este comando utiliza o `maven:3.8.5-openjdk-11-slim` para compilar o projeto e gerar o executável.

### 2. Executar a Aplicação

Após a construção da imagem, inicia o contentor com o comando abaixo. É essencial usar a flag `-it` para permitir a interatividade no terminal:

```bash
docker run -it --rm terminal-file-explorer
```

Isto irá iniciar a aplicação e apresentar a interface gráfica no teu terminal.

## 📖 Guia de Utilização

A aplicação divide o ecrã em dois painéis (Esquerdo e Direito), permitindo a gestão de ficheiros entre duas diretorias distintas.

### Navegação

- **Selecionar Ficheiros/Pastas**: Utiliza as setas Cima (`↑`) e Baixo (`↓`) para percorrer a lista de ficheiros.
- **Entrar numa Pasta**: Prime Enter sobre uma pasta (indicada com uma barra `/` no final do nome) para ver o seu conteúdo.
- **Voltar Atrás**: Seleciona o item `..` (no topo da lista) e prime Enter para subir para a diretoria pai.

### Gestão de Ficheiros

#### Mover Ficheiros

A aplicação permite mover ficheiros de um painel para o outro rapidamente utilizando as setas laterais:

- **Do Painel Esquerdo para o Direito**: Seleciona o ficheiro no painel esquerdo e prime a Seta Direita (`→`).
- **Do Painel Direito para o Esquerdo**: Seleciona o ficheiro no painel direito e prime a Seta Esquerda (`←`).

> **Nota**: Ao mover um ficheiro, este será transferido para a diretoria que estiver atualmente aberta no painel oposto.

#### Eliminar Ficheiros

Para apagar um ficheiro permanentemente:

1. Seleciona o ficheiro que desejas remover.
2. Prime a tecla Delete ou Backspace.
3. O ficheiro será removido imediatamente do sistema de ficheiros e a lista será atualizada.

### Sair da Aplicação

Para fechar o programa, podes navegar até ao botão "Exit" na parte inferior do ecrã (utilizando a tecla `Tab` ou as setas) e premir Enter, ou simplesmente fechar a janela do terminal.