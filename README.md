# Indexação de Dados

Indexação é a criação de estruturas auxiliares (índices) que permitem localizar rapidamente
registros em uma tabela, sem precisar examinar linha por linha. Esse repositorio contém o projeto desenvolvido para a disciplina de **Estrutura de Dados II**, onde o objetivo é implementar um **sistema de monitoramento ambiental que simula a indexação de dados**.

## Estrutura do projeto
A arquitetura do projeto é baseada no padrão MVC (Model-View-Controller), onde a lógica de negócio é separada da interface do usuário. Segue a estrutura do projeto:

```
├── src
│   ├── Main.java
│   ├── controller
│   ├── datastructures
│   ├── model 
│   │   ├── DAO
│   │   └── entities
│   ├── service
│   └── utils
```

- `Main.java`: Classe principal que inicia o sistema.
- `controller`: Pacote que contém as classes responsáveis por controlar a lógica do sistema.
- `datastructures`: Pacote que contém as classes responsáveis pelas estruturas de dados utilizadas no sistema (AVL e LinkedList).
- `model`: Pacote que contém as classes responsáveis pela persistência de dados.
  - `DAO`: Classes responsáveis pela comunicação com o banco de dados.
  - `entities`: Classes que representam as entidades do sistema.
- `service`: Pacote que contém as classes responsáveis pela lógica de negócio do sistema.
- `utils`: Pacote que contém as classes utilitárias do sistema.

## Executando o projeto
1. Certifique-se de ter o Java 17 instalado em sua máquina. Você pode baixar a versão mais recente do Java [aqui](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).
2. Clone este repositório em sua máquina:
   ```bash
   git clone https://github.com/lauramoroni/database-indexing.git
   cd database-indexing
   ```

## Documentação
[Arquitetura do sistema](https://www.figma.com/board/VZIT6t9Fx9PEQ4K8dLQ6Y9/DatabaseIndexing?node-id=0-1&t=PO5u0XlMRP42AizU-1)
