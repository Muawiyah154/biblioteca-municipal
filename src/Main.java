import java.util.NoSuchElementException;

/**
 * Classe principal: apresenta o menu interactivo da Biblioteca Municipal
 * e encaminha cada opção para as operações da classe Biblioteca.
 */
public class Main {

    private static final Biblioteca BIBLIOTECA = new Biblioteca();

    public static void main(String[] args) {
        BIBLIOTECA.carregarDadosDemonstracao();

        System.out.println("=======================================================");
        System.out.println("   BIBLIOTECA MUNICIPAL - Sistema de Gestão (consola)");
        System.out.println("=======================================================");
        System.out.println("Dados de demonstração carregados: " + BIBLIOTECA.getTotalLivros()
                + " livros e " + BIBLIOTECA.getTotalUtilizadores() + " utilizadores.");

        try {
            int opcao;
            do {
                mostrarMenu();
                opcao = Entrada.lerInteiro("Escolha uma opção: ", 0, 11);
                System.out.println();
                executarOpcao(opcao);
            } while (opcao != 0);
        } catch (NoSuchElementException e) {
            // Ocorre se a entrada de dados for fechada (por exemplo, Ctrl+D)
            System.out.println();
            System.out.println("[AVISO] Entrada de dados terminada. A encerrar o programa.");
        }
    }

    // ---------------------------------------------------------------------
    // Menu
    // ---------------------------------------------------------------------

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("==================== MENU PRINCIPAL ====================");
        System.out.println(" LIVROS");
        System.out.println("   1  - Registar novo livro");
        System.out.println("   2  - Listar livros disponíveis");
        System.out.println("   3  - Listar catálogo completo");
        System.out.println("   4  - Pesquisar livros por autor");
        System.out.println("   5  - Pesquisar livros por título");
        System.out.println(" UTILIZADORES");
        System.out.println("   6  - Registar novo utilizador");
        System.out.println("   7  - Listar utilizadores");
        System.out.println(" EMPRÉSTIMOS");
        System.out.println("   8  - Efectuar empréstimo");
        System.out.println("   9  - Registar devolução");
        System.out.println(" ESTATÍSTICAS");
        System.out.println("   10 - Livro mais emprestado");
        System.out.println("   11 - Total de livros requisitados");
        System.out.println("   0  - Sair");
        System.out.println("========================================================");
    }

    private static void executarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                registarLivro();
                break;
            case 2:
                listarLivrosDisponiveis();
                break;
            case 3:
                listarCatalogoCompleto();
                break;
            case 4:
                pesquisarPorAutor();
                break;
            case 5:
                pesquisarPorTitulo();
                break;
            case 6:
                registarUtilizador();
                break;
            case 7:
                listarUtilizadores();
                break;
            case 8:
                efectuarEmprestimo();
                break;
            case 9:
                registarDevolucao();
                break;
            case 10:
                mostrarLivroMaisEmprestado();
                break;
            case 11:
                mostrarTotalRequisitados();
                break;
            case 0:
                System.out.println("Obrigado por utilizar o sistema da Biblioteca Municipal. Até breve!");
                break;
            default:
                System.out.println("[ERRO] Opção inválida.");
        }
    }

    // ---------------------------------------------------------------------
    // Livros
    // ---------------------------------------------------------------------

    private static void registarLivro() {
        System.out.println("--- REGISTO DE LIVRO ---");
        if (BIBLIOTECA.getTotalLivros() >= Biblioteca.MAX_LIVROS) {
            System.out.println("[ERRO] " + mensagemErro(Biblioteca.ERRO_CAPACIDADE_CHEIA));
            return;
        }
        int id = Entrada.lerIdentificador("Identificador único (ID): ");
        if (BIBLIOTECA.existeLivro(id)) {
            System.out.println("[ERRO] " + mensagemErro(Biblioteca.ERRO_ID_DUPLICADO));
            return;
        }
        String titulo = Entrada.lerTexto("Título: ");
        String autor = Entrada.lerTexto("Autor: ");
        int ano = Entrada.lerInteiro("Ano de publicação: ", 1, Biblioteca.anoActual());
        int quantidade = Entrada.lerInteiro("Quantidade disponível: ", 1, 1000);

        int resultado = BIBLIOTECA.registarLivro(id, titulo, autor, ano, quantidade);
        if (resultado == Biblioteca.OK) {
            System.out.println("[OK] Livro \"" + titulo + "\" registado com sucesso.");
        } else {
            System.out.println("[ERRO] " + mensagemErro(resultado));
        }
    }

    private static void listarLivrosDisponiveis() {
        System.out.println("--- LIVROS DISPONÍVEIS ---");
        if (BIBLIOTECA.listarLivrosDisponiveis() == 0) {
            System.out.println("Não existem livros disponíveis de momento.");
        }
    }

    private static void listarCatalogoCompleto() {
        System.out.println("--- CATÁLOGO COMPLETO ---");
        if (BIBLIOTECA.listarCatalogoCompleto() == 0) {
            System.out.println("O catálogo está vazio.");
        }
    }

    private static void pesquisarPorAutor() {
        System.out.println("--- PESQUISA POR AUTOR ---");
        String termo = Entrada.lerTexto("Nome (ou parte do nome) do autor: ");
        if (BIBLIOTECA.pesquisarPorAutor(termo) == 0) {
            System.out.println("Nenhum livro encontrado para o autor \"" + termo + "\".");
        }
    }

    private static void pesquisarPorTitulo() {
        System.out.println("--- PESQUISA POR TÍTULO ---");
        String termo = Entrada.lerTexto("Título (ou parte do título): ");
        if (BIBLIOTECA.pesquisarPorTitulo(termo) == 0) {
            System.out.println("Nenhum livro encontrado com o título \"" + termo + "\".");
        }
    }

    // ---------------------------------------------------------------------
    // Utilizadores
    // ---------------------------------------------------------------------

    private static void registarUtilizador() {
        System.out.println("--- REGISTO DE UTILIZADOR ---");
        if (BIBLIOTECA.getTotalUtilizadores() >= Biblioteca.MAX_UTILIZADORES) {
            System.out.println("[ERRO] " + mensagemErro(Biblioteca.ERRO_CAPACIDADE_CHEIA));
            return;
        }
        int id = Entrada.lerIdentificador("Identificador único (ID): ");
        if (BIBLIOTECA.existeUtilizador(id)) {
            System.out.println("[ERRO] " + mensagemErro(Biblioteca.ERRO_ID_DUPLICADO));
            return;
        }
        String nome = Entrada.lerTexto("Nome completo: ");

        int resultado = BIBLIOTECA.registarUtilizador(id, nome);
        if (resultado == Biblioteca.OK) {
            System.out.println("[OK] Utilizador \"" + nome + "\" registado com sucesso.");
        } else {
            System.out.println("[ERRO] " + mensagemErro(resultado));
        }
    }

    private static void listarUtilizadores() {
        System.out.println("--- UTILIZADORES REGISTADOS ---");
        if (BIBLIOTECA.listarUtilizadores() == 0) {
            System.out.println("Não existem utilizadores registados.");
        }
    }

    // ---------------------------------------------------------------------
    // Empréstimos e devoluções
    // ---------------------------------------------------------------------

    private static void efectuarEmprestimo() {
        System.out.println("--- EMPRÉSTIMO DE LIVRO ---");
        if (BIBLIOTECA.getTotalUtilizadores() == 0 || BIBLIOTECA.getTotalLivros() == 0) {
            System.out.println("[ERRO] É necessário ter pelo menos um livro e um utilizador registados.");
            return;
        }
        int idUtilizador = Entrada.lerIdentificador("ID do utilizador: ");
        if (!BIBLIOTECA.existeUtilizador(idUtilizador)) {
            System.out.println("[ERRO] " + mensagemErro(Biblioteca.ERRO_UTILIZADOR_INEXISTENTE));
            return;
        }
        int idLivro = Entrada.lerIdentificador("ID do livro: ");

        int resultado = BIBLIOTECA.efectuarEmprestimo(idUtilizador, idLivro);
        if (resultado == Biblioteca.OK) {
            System.out.println("[OK] Empréstimo efectuado: \"" + BIBLIOTECA.getTitulo(idLivro)
                    + "\" emprestado a " + BIBLIOTECA.getNomeUtilizador(idUtilizador) + ".");
            System.out.println("     Exemplares ainda disponíveis: "
                    + BIBLIOTECA.getQuantidadeDisponivel(idLivro));
        } else {
            System.out.println("[ERRO] " + mensagemErro(resultado));
        }
    }

    private static void registarDevolucao() {
        System.out.println("--- DEVOLUÇÃO DE LIVRO ---");
        if (BIBLIOTECA.getTotalUtilizadores() == 0 || BIBLIOTECA.getTotalLivros() == 0) {
            System.out.println("[ERRO] É necessário ter pelo menos um livro e um utilizador registados.");
            return;
        }
        int idUtilizador = Entrada.lerIdentificador("ID do utilizador: ");
        if (!BIBLIOTECA.existeUtilizador(idUtilizador)) {
            System.out.println("[ERRO] " + mensagemErro(Biblioteca.ERRO_UTILIZADOR_INEXISTENTE));
            return;
        }
        if (BIBLIOTECA.contarEmprestimosDoUtilizador(idUtilizador) == 0) {
            System.out.println(BIBLIOTECA.getNomeUtilizador(idUtilizador)
                    + " não tem livros por devolver.");
            return;
        }
        System.out.println("Livros actualmente emprestados a "
                + BIBLIOTECA.getNomeUtilizador(idUtilizador) + ":");
        BIBLIOTECA.listarEmprestimosDoUtilizador(idUtilizador);
        int idLivro = Entrada.lerIdentificador("ID do livro a devolver: ");

        int resultado = BIBLIOTECA.registarDevolucao(idUtilizador, idLivro);
        if (resultado == Biblioteca.OK) {
            System.out.println("[OK] Devolução registada: \"" + BIBLIOTECA.getTitulo(idLivro)
                    + "\" devolvido por " + BIBLIOTECA.getNomeUtilizador(idUtilizador) + ".");
            System.out.println("     Exemplares disponíveis: "
                    + BIBLIOTECA.getQuantidadeDisponivel(idLivro));
        } else {
            System.out.println("[ERRO] " + mensagemErro(resultado));
        }
    }

    // ---------------------------------------------------------------------
    // Estatísticas
    // ---------------------------------------------------------------------

    private static void mostrarLivroMaisEmprestado() {
        System.out.println("--- LIVRO MAIS EMPRESTADO ---");
        int maximo = BIBLIOTECA.maximoRequisicoesPorLivro();
        if (maximo == 0) {
            System.out.println("Ainda não foram registados empréstimos.");
            return;
        }
        System.out.println("Livro(s) com mais requisições (" + maximo + "):");
        BIBLIOTECA.listarLivrosComRequisicoes(maximo);
    }

    private static void mostrarTotalRequisitados() {
        System.out.println("--- TOTAL DE LIVROS REQUISITADOS ---");
        int total = BIBLIOTECA.totalRequisicoes();
        int emprestados = BIBLIOTECA.totalEmprestadosActualmente();
        System.out.println("Total de requisições efectuadas: " + total);
        System.out.println("Ainda emprestados (não devolvidos): " + emprestados);
        System.out.println("Já devolvidos: " + (total - emprestados));
    }

    // ---------------------------------------------------------------------
    // Mensagens de erro
    // ---------------------------------------------------------------------

    /** Converte um código de resultado numa mensagem legível para o utilizador. */
    private static String mensagemErro(int codigo) {
        switch (codigo) {
            case Biblioteca.ERRO_DADOS_INVALIDOS:
                return "Dados inválidos. Verifique os valores introduzidos.";
            case Biblioteca.ERRO_ID_DUPLICADO:
                return "Já existe um registo com esse ID. O identificador tem de ser único.";
            case Biblioteca.ERRO_CAPACIDADE_CHEIA:
                return "Capacidade máxima atingida. Não é possível registar mais elementos.";
            case Biblioteca.ERRO_LIVRO_INEXISTENTE:
                return "Livro não encontrado. Verifique o ID introduzido.";
            case Biblioteca.ERRO_UTILIZADOR_INEXISTENTE:
                return "Utilizador não registado. Verifique o ID introduzido.";
            case Biblioteca.ERRO_SEM_EXEMPLARES:
                return "Não há exemplares disponíveis deste livro.";
            case Biblioteca.ERRO_JA_REQUISITADO:
                return "Este utilizador já tem este livro emprestado e ainda não o devolveu.";
            case Biblioteca.ERRO_SEM_EMPRESTIMO:
                return "Este utilizador não tem este livro emprestado.";
            default:
                return "Erro desconhecido.";
        }
    }
}
