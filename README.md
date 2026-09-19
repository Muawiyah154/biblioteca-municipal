# Biblioteca Municipal — Sistema de Gestão em Consola (Java)

Trabalho de Campo da disciplina de **Introdução a Algoritmos e Programação**
Universidade Aberta ISCED (UnISCED) — Faculdade de Engenharia e Agricultura
Licenciatura em Engenharia Informática

**Autor:** _[Muawiyah aboobakar]_ — **Código:** _[112660402]_

---

## 1. Descrição

Aplicação de consola, desenvolvida em **Java**, que permite aos bibliotecários da Biblioteca
Municipal gerir o inventário de livros e o histórico de empréstimos aos utilizadores.

Os dados são guardados numa **base de dados simulada em memória**, implementada com
**vectores (arrays) e matrizes**, conforme pedido no enunciado. Ao terminar o programa, os dados
são perdidos (não há persistência em ficheiros ou base de dados).

## 2. Funcionalidades

| Menu | Funcionalidade | Descrição |
|------|----------------|-----------|
| 1 | **Registo de livros** | Insere um novo título com identificador único, título, autor, ano de publicação e quantidade disponível. |
| 2 | **Listar livros disponíveis** | Mostra os livros que têm pelo menos um exemplar disponível. |
| 3 | Listar catálogo completo | Mostra todos os livros, incluindo os que não têm exemplares disponíveis de momento. |
| 4 | **Pesquisar por autor** | Pesquisa por nome (ou parte do nome) do autor. Ignora maiúsculas/minúsculas e acentos. |
| 5 | **Pesquisar por título** | Pesquisa por título (ou parte do título). Ignora maiúsculas/minúsculas e acentos. |
| 6 | Registar utilizador | Regista um utilizador com identificador único e nome. |
| 7 | Listar utilizadores | Mostra os utilizadores e o número de livros que cada um tem emprestados. |
| 8 | **Efectuar empréstimo** | Empresta um livro a um utilizador registado e diminui a quantidade disponível. |
| 9 | **Registar devolução** | Regista a devolução e aumenta a quantidade disponível. |
| 10 | **Livro mais emprestado** | Apresenta o(s) livro(s) com mais requisições (trata empates). |
| 11 | **Total de livros requisitados** | Apresenta o total de requisições, quantos livros estão emprestados e quantos já foram devolvidos. |
| 0 | Sair | Termina o programa. |

## 3. Requisitos e dependências

- **JDK (Java Development Kit) 11 ou superior** (testado com OpenJDK 21).
- Não são necessárias bibliotecas externas: o projecto usa apenas a biblioteca padrão do Java.
- Um terminal (Linux, macOS ou Windows) ou uma IDE (IntelliJ IDEA, NetBeans, Eclipse, VS Code).

Para confirmar a instalação do Java, execute no terminal:

```bash
java -version
javac -version
```

## 4. Estrutura do projecto

```
biblioteca-municipal/
├── README.md
├── .gitignore
├── src/
│   ├── Main.java         # Menu interactivo e ligação com o utilizador
│   ├── Biblioteca.java   # Base de dados em memória (vectores e matrizes) e regras de negócio
│   └── Entrada.java      # Leitura e validação dos dados introduzidos na consola
└── docs/
    └── capturas/         # Capturas de ecrã da execução do programa
```

## 5. Como compilar e executar

### 5.1. Linux / macOS

```bash
git clone <https://github.com/Muawiyah154/biblioteca-municipal.git>
cd biblioteca-municipal
mkdir -p out
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

### 5.2. Windows (Prompt de Comando ou PowerShell)

```bat
git clone <LINK-DO-REPOSITORIO>
cd biblioteca-municipal
mkdir out
javac -encoding UTF-8 -d out src\*.java
java -cp out Main
```

> **Acentos no Windows:** se os caracteres acentuados aparecerem mal, execute `chcp 65001`
> e inicie o programa com:
> `java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp out Main`
> (ou utilize o Windows Terminal).

### 5.3. Numa IDE

1. Abra a pasta do projecto (ou crie um novo projecto Java e copie os ficheiros da pasta `src`).
2. Confirme que os três ficheiros `.java` estão na mesma pasta de código-fonte.
3. Execute a classe **`Main`**.

## 6. Como utilizar

Ao iniciar, o programa carrega **dados de demonstração** (6 livros e 3 utilizadores) para
facilitar os testes e apresenta o menu principal. Digite o número da opção e prima **Enter**.

Exemplo de fluxo de teste:

1. Opção **7** para ver os utilizadores (IDs 101, 102 e 103) e **3** para ver os livros (IDs 1 a 6).
2. Opção **8**: utilizador `101`, livro `5` — empréstimo efectuado.
3. Opção **8**: utilizador `102`, livro `5` — erro: não há exemplares disponíveis.
4. Opção **9**: utilizador `101`, livro `5` — devolução registada.
5. Opção **10** e **11** para ver as estatísticas.

> Para iniciar o sistema **sem** dados de demonstração, apague (ou comente) a linha
> `BIBLIOTECA.carregarDadosDemonstracao();` no método `main` da classe `Main`.

Capturas de ecrã de todas as funcionalidades estão em [`docs/capturas`](docs/capturas).

![Menu principal](docs/capturas/01_arranque_menu.png)

## 7. Estruturas de dados utilizadas

| Estrutura | Onde | Finalidade |
|-----------|------|------------|
| Vectores paralelos (`int[]`, `String[]`) | Livros: `idLivro`, `tituloLivro`, `autorLivro`, `anoLivro`, `quantidadeTotal`, `quantidadeDisponivel` | A posição `i` representa o mesmo livro em todos os vectores. |
| Vectores paralelos | Utilizadores: `idUtilizador`, `nomeUtilizador` | A posição `u` representa o mesmo utilizador nos dois vectores. |
| Matriz `int[][] emprestimosActivos` | `[utilizador][livro]` | `1` se o utilizador tem o livro emprestado neste momento, `0` caso contrário. |
| Matriz `int[][] historicoEmprestimos` | `[utilizador][livro]` | Número de vezes que o utilizador requisitou o livro (base das estatísticas). |

Capacidade máxima: **100 livros** e **50 utilizadores** (constantes `MAX_LIVROS` e `MAX_UTILIZADORES`).

## 8. Lógica e tratamento de erros

- **Validação de entradas:** números inválidos (texto, vazio, fora do intervalo), textos vazios ou
  com mais de 100 caracteres são recusados e a pergunta é repetida; o programa não termina por erro de digitação.
- **Identificadores únicos:** não é possível registar dois livros (ou dois utilizadores) com o mesmo ID.
- **Ano de publicação:** tem de estar entre 1 e o ano actual. **Quantidade:** entre 1 e 1000.
- **Empréstimo:** só é efectuado se o utilizador e o livro existirem, se houver exemplares
  disponíveis e se o utilizador ainda não tiver esse livro emprestado.
- **Devolução:** só é aceite se o utilizador tiver realmente o livro emprestado (evita aumentar o stock indevidamente).
- **Capacidade:** o programa avisa quando o catálogo ou a lista de utilizadores está cheio.
- **Fim inesperado da entrada de dados** (por exemplo, `Ctrl+D`): o programa encerra de forma controlada.

Os métodos da classe `Biblioteca` devolvem **códigos de resultado** (`OK`, `ERRO_ID_DUPLICADO`,
`ERRO_SEM_EXEMPLARES`, …) que a classe `Main` converte em mensagens claras para o utilizador.

## 9. Boas práticas aplicadas

- Separação de responsabilidades: interface (`Main`), regras e dados (`Biblioteca`), leitura de dados (`Entrada`).
- Nomes descritivos em português, constantes em vez de "números mágicos" e comentários Javadoc.
- Métodos curtos, com uma única responsabilidade.
- Ausência de dependências externas e de ficheiros compilados no repositório (`.gitignore`).

## 10. Documentação técnica

O relatório completo (introdução, objectivos, referencial teórico, metodologia, desenvolvimento,
conclusão e referências) encontra-se no ficheiro de documentação técnica entregue com o trabalho.

---

_Trabalho académico — UnISCED, Licenciatura em Engenharia Informática._
