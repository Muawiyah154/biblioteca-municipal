import java.text.Normalizer;
import java.time.Year;

/**
 * Representa a "base de dados" da Biblioteca Municipal, simulada em memória.
 *
 * Estruturas de dados utilizadas:
 * - Vetores paralelos para os livros (o índice i identifica o mesmo livro em todos);
 * - Vetores paralelos para os utilizadores;
 * - Duas matrizes (utilizador x livro): empréstimos activos e histórico de empréstimos.
 *
 * Os métodos que alteram dados devolvem um código de resultado (constantes OK e ERRO_*),
 * o que permite à interface (classe Main) apresentar mensagens de erro claras.
 */
public class Biblioteca {

    // ---------------------------------------------------------------------
    // Capacidades máximas da base de dados em memória
    // ---------------------------------------------------------------------
    public static final int MAX_LIVROS = 100;
    public static final int MAX_UTILIZADORES = 50;

    // ---------------------------------------------------------------------
    // Códigos de resultado das operações
    // ---------------------------------------------------------------------
    public static final int OK = 0;
    public static final int ERRO_DADOS_INVALIDOS = 1;
    public static final int ERRO_ID_DUPLICADO = 2;
    public static final int ERRO_CAPACIDADE_CHEIA = 3;
    public static final int ERRO_LIVRO_INEXISTENTE = 4;
    public static final int ERRO_UTILIZADOR_INEXISTENTE = 5;
    public static final int ERRO_SEM_EXEMPLARES = 6;
    public static final int ERRO_JA_REQUISITADO = 7;
    public static final int ERRO_SEM_EMPRESTIMO = 8;

    // ---------------------------------------------------------------------
    // Vetores dos livros (posição i = mesmo livro em todos os vetores)
    // ---------------------------------------------------------------------
    private final int[] idLivro = new int[MAX_LIVROS];
    private final String[] tituloLivro = new String[MAX_LIVROS];
    private final String[] autorLivro = new String[MAX_LIVROS];
    private final int[] anoLivro = new int[MAX_LIVROS];
    private final int[] quantidadeTotal = new int[MAX_LIVROS];
    private final int[] quantidadeDisponivel = new int[MAX_LIVROS];
    private int totalLivros = 0;

    // ---------------------------------------------------------------------
    // Vetores dos utilizadores
    // ---------------------------------------------------------------------
    private final int[] idUtilizador = new int[MAX_UTILIZADORES];
    private final String[] nomeUtilizador = new String[MAX_UTILIZADORES];
    private int totalUtilizadores = 0;

    // ---------------------------------------------------------------------
    // Matrizes [utilizador][livro]
    // emprestimosActivos: 1 se o utilizador tem o livro emprestado, 0 caso contrário
    // historicoEmprestimos: número de vezes que o utilizador requisitou o livro
    // ---------------------------------------------------------------------
    private final int[][] emprestimosActivos = new int[MAX_UTILIZADORES][MAX_LIVROS];
    private final int[][] historicoEmprestimos = new int[MAX_UTILIZADORES][MAX_LIVROS];

    // =====================================================================
    // Registo
    // =====================================================================

    /**
     * Regista um novo livro no catálogo.
     *
     * @return OK, ERRO_DADOS_INVALIDOS, ERRO_ID_DUPLICADO ou ERRO_CAPACIDADE_CHEIA
     */
    public int registarLivro(int id, String titulo, String autor, int ano, int quantidade) {
        if (id <= 0 || titulo == null || titulo.isBlank() || autor == null || autor.isBlank()
                || ano < 1 || ano > anoActual() || quantidade < 1) {
            return ERRO_DADOS_INVALIDOS;
        }
        if (indiceLivro(id) != -1) {
            return ERRO_ID_DUPLICADO;
        }
        if (totalLivros >= MAX_LIVROS) {
            return ERRO_CAPACIDADE_CHEIA;
        }
        idLivro[totalLivros] = id;
        tituloLivro[totalLivros] = titulo.trim();
        autorLivro[totalLivros] = autor.trim();
        anoLivro[totalLivros] = ano;
        quantidadeTotal[totalLivros] = quantidade;
        quantidadeDisponivel[totalLivros] = quantidade;
        totalLivros++;
        return OK;
    }

    /**
     * Regista um novo utilizador.
     *
     * @return OK, ERRO_DADOS_INVALIDOS, ERRO_ID_DUPLICADO ou ERRO_CAPACIDADE_CHEIA
     */
    public int registarUtilizador(int id, String nome) {
        if (id <= 0 || nome == null || nome.isBlank()) {
            return ERRO_DADOS_INVALIDOS;
        }
        if (indiceUtilizador(id) != -1) {
            return ERRO_ID_DUPLICADO;
        }
        if (totalUtilizadores >= MAX_UTILIZADORES) {
            return ERRO_CAPACIDADE_CHEIA;
        }
        idUtilizador[totalUtilizadores] = id;
        nomeUtilizador[totalUtilizadores] = nome.trim();
        totalUtilizadores++;
        return OK;
    }

    // =====================================================================
    // Empréstimos e devoluções
    // =====================================================================

    /**
     * Efectua o empréstimo de um livro a um utilizador registado,
     * diminuindo a quantidade disponível.
     *
     * @return OK, ERRO_UTILIZADOR_INEXISTENTE, ERRO_LIVRO_INEXISTENTE,
     *         ERRO_SEM_EXEMPLARES ou ERRO_JA_REQUISITADO
     */
    public int efectuarEmprestimo(int idDoUtilizador, int idDoLivro) {
        int u = indiceUtilizador(idDoUtilizador);
        if (u == -1) {
            return ERRO_UTILIZADOR_INEXISTENTE;
        }
        int l = indiceLivro(idDoLivro);
        if (l == -1) {
            return ERRO_LIVRO_INEXISTENTE;
        }
        if (quantidadeDisponivel[l] == 0) {
            return ERRO_SEM_EXEMPLARES;
        }
        if (emprestimosActivos[u][l] > 0) {
            return ERRO_JA_REQUISITADO;
        }
        quantidadeDisponivel[l]--;
        emprestimosActivos[u][l] = 1;
        historicoEmprestimos[u][l]++;
        return OK;
    }

    /**
     * Regista a devolução de um livro, aumentando a quantidade disponível.
     *
     * @return OK, ERRO_UTILIZADOR_INEXISTENTE, ERRO_LIVRO_INEXISTENTE ou ERRO_SEM_EMPRESTIMO
     */
    public int registarDevolucao(int idDoUtilizador, int idDoLivro) {
        int u = indiceUtilizador(idDoUtilizador);
        if (u == -1) {
            return ERRO_UTILIZADOR_INEXISTENTE;
        }
        int l = indiceLivro(idDoLivro);
        if (l == -1) {
            return ERRO_LIVRO_INEXISTENTE;
        }
        if (emprestimosActivos[u][l] == 0) {
            return ERRO_SEM_EMPRESTIMO;
        }
        emprestimosActivos[u][l] = 0;
        quantidadeDisponivel[l]++;
        return OK;
    }

    // =====================================================================
    // Consulta do catálogo
    // =====================================================================

    /**
     * Lista os livros com pelo menos um exemplar disponível.
     *
     * @return número de livros listados
     */
    public int listarLivrosDisponiveis() {
        int listados = 0;
        for (int i = 0; i < totalLivros; i++) {
            if (quantidadeDisponivel[i] > 0) {
                if (listados == 0) {
                    imprimirCabecalhoLivros();
                }
                imprimirLivro(i);
                listados++;
            }
        }
        return listados;
    }

    /**
     * Lista todos os livros do catálogo (incluindo os sem exemplares disponíveis).
     *
     * @return número de livros listados
     */
    public int listarCatalogoCompleto() {
        for (int i = 0; i < totalLivros; i++) {
            if (i == 0) {
                imprimirCabecalhoLivros();
            }
            imprimirLivro(i);
        }
        return totalLivros;
    }

    /**
     * Pesquisa livros cujo autor contém o texto indicado
     * (ignora maiúsculas/minúsculas e acentos).
     *
     * @return número de livros encontrados
     */
    public int pesquisarPorAutor(String termo) {
        return pesquisar(termo, true);
    }

    /**
     * Pesquisa livros cujo título contém o texto indicado
     * (ignora maiúsculas/minúsculas e acentos).
     *
     * @return número de livros encontrados
     */
    public int pesquisarPorTitulo(String termo) {
        return pesquisar(termo, false);
    }

    private int pesquisar(String termo, boolean porAutor) {
        String chave = normalizar(termo);
        int encontrados = 0;
        for (int i = 0; i < totalLivros; i++) {
            String campo = porAutor ? autorLivro[i] : tituloLivro[i];
            if (normalizar(campo).contains(chave)) {
                if (encontrados == 0) {
                    imprimirCabecalhoLivros();
                }
                imprimirLivro(i);
                encontrados++;
            }
        }
        return encontrados;
    }

    // =====================================================================
    // Utilizadores
    // =====================================================================

    /**
     * Lista todos os utilizadores registados e o número de livros que têm emprestados.
     *
     * @return número de utilizadores listados
     */
    public int listarUtilizadores() {
        for (int u = 0; u < totalUtilizadores; u++) {
            if (u == 0) {
                System.out.printf("%-5s %-40s %s%n", "ID", "Nome", "Livros emprestados");
                System.out.println("-".repeat(70));
            }
            System.out.printf("%-5d %-40s %d%n", idUtilizador[u],
                    truncar(nomeUtilizador[u], 40), contarEmprestimosActivos(u));
        }
        return totalUtilizadores;
    }

    /**
     * Conta os livros que um utilizador tem actualmente emprestados
     * (0 se o utilizador não existir).
     */
    public int contarEmprestimosDoUtilizador(int idDoUtilizador) {
        int u = indiceUtilizador(idDoUtilizador);
        return (u == -1) ? 0 : contarEmprestimosActivos(u);
    }

    /**
     * Lista os livros que um utilizador tem actualmente emprestados.
     *
     * @return número de livros listados (0 se o utilizador não existir ou não tiver livros)
     */
    public int listarEmprestimosDoUtilizador(int idDoUtilizador) {
        int u = indiceUtilizador(idDoUtilizador);
        if (u == -1) {
            return 0;
        }
        int listados = 0;
        for (int l = 0; l < totalLivros; l++) {
            if (emprestimosActivos[u][l] > 0) {
                if (listados == 0) {
                    System.out.printf("%-5s %-34s %s%n", "ID", "Título", "Autor");
                    System.out.println("-".repeat(70));
                }
                System.out.printf("%-5d %-34s %s%n", idLivro[l],
                        truncar(tituloLivro[l], 34), truncar(autorLivro[l], 28));
                listados++;
            }
        }
        return listados;
    }

    // =====================================================================
    // Estatísticas
    // =====================================================================

    /**
     * Total de requisições feitas desde o início (inclui os livros já devolvidos).
     */
    public int totalRequisicoes() {
        int total = 0;
        for (int u = 0; u < totalUtilizadores; u++) {
            for (int l = 0; l < totalLivros; l++) {
                total += historicoEmprestimos[u][l];
            }
        }
        return total;
    }

    /**
     * Número de livros que se encontram emprestados neste momento (ainda não devolvidos).
     */
    public int totalEmprestadosActualmente() {
        int total = 0;
        for (int u = 0; u < totalUtilizadores; u++) {
            total += contarEmprestimosActivos(u);
        }
        return total;
    }

    /**
     * Devolve o maior número de requisições registado para um único livro
     * (0 se ainda não houve empréstimos).
     */
    public int maximoRequisicoesPorLivro() {
        int maximo = 0;
        for (int l = 0; l < totalLivros; l++) {
            int requisicoes = requisicoesDoLivro(l);
            if (requisicoes > maximo) {
                maximo = requisicoes;
            }
        }
        return maximo;
    }

    /**
     * Lista os livros que têm exactamente o número de requisições indicado
     * (usado para mostrar o(s) livro(s) mais emprestado(s), tratando empates).
     */
    public void listarLivrosComRequisicoes(int requisicoes) {
        System.out.printf("%-5s %-34s %-26s %s%n", "ID", "Título", "Autor", "Requisições");
        System.out.println("-".repeat(80));
        for (int l = 0; l < totalLivros; l++) {
            if (requisicoesDoLivro(l) == requisicoes) {
                System.out.printf("%-5d %-34s %-26s %d%n", idLivro[l],
                        truncar(tituloLivro[l], 34), truncar(autorLivro[l], 26), requisicoes);
            }
        }
    }

    // =====================================================================
    // Métodos de consulta simples (usados pela interface)
    // =====================================================================

    public int getTotalLivros() {
        return totalLivros;
    }

    public int getTotalUtilizadores() {
        return totalUtilizadores;
    }

    public boolean existeLivro(int id) {
        return indiceLivro(id) != -1;
    }

    public boolean existeUtilizador(int id) {
        return indiceUtilizador(id) != -1;
    }

    /** Devolve o título do livro com o ID indicado, ou null se não existir. */
    public String getTitulo(int id) {
        int l = indiceLivro(id);
        return (l == -1) ? null : tituloLivro[l];
    }

    /** Devolve o nome do utilizador com o ID indicado, ou null se não existir. */
    public String getNomeUtilizador(int id) {
        int u = indiceUtilizador(id);
        return (u == -1) ? null : nomeUtilizador[u];
    }

    /** Devolve a quantidade disponível do livro com o ID indicado, ou -1 se não existir. */
    public int getQuantidadeDisponivel(int id) {
        int l = indiceLivro(id);
        return (l == -1) ? -1 : quantidadeDisponivel[l];
    }

    /** Ano civil actual (limite máximo aceite para o ano de publicação). */
    public static int anoActual() {
        return Year.now().getValue();
    }

    // =====================================================================
    // Dados de demonstração
    // =====================================================================

    /**
     * Carrega alguns livros e utilizadores de exemplo para facilitar a demonstração.
     */
    public void carregarDadosDemonstracao() {
        registarLivro(1, "Terra Sonâmbula", "Mia Couto", 1992, 3);
        registarLivro(2, "Ualalapi", "Ungulani Ba Ka Khosa", 1987, 2);
        registarLivro(3, "Os Lusíadas", "Luís de Camões", 1572, 2);
        registarLivro(4, "Dom Casmurro", "Machado de Assis", 1899, 4);
        registarLivro(5, "Memorial do Convento", "José Saramago", 1982, 1);
        registarLivro(6, "Nós Matámos o Cão-Tinhoso", "Luís Bernardo Honwana", 1964, 3);

        registarUtilizador(101, "Ana Machava");
        registarUtilizador(102, "Carlos Sitoe");
        registarUtilizador(103, "Fátima Cossa");
    }

    // =====================================================================
    // Métodos auxiliares privados
    // =====================================================================

    /** Pesquisa linear: devolve a posição do livro nos vetores, ou -1 se não existir. */
    private int indiceLivro(int id) {
        for (int i = 0; i < totalLivros; i++) {
            if (idLivro[i] == id) {
                return i;
            }
        }
        return -1;
    }

    /** Pesquisa linear: devolve a posição do utilizador nos vetores, ou -1 se não existir. */
    private int indiceUtilizador(int id) {
        for (int i = 0; i < totalUtilizadores; i++) {
            if (idUtilizador[i] == id) {
                return i;
            }
        }
        return -1;
    }

    /** Soma a coluna do livro l na matriz de histórico (todas as requisições desse livro). */
    private int requisicoesDoLivro(int l) {
        int total = 0;
        for (int u = 0; u < totalUtilizadores; u++) {
            total += historicoEmprestimos[u][l];
        }
        return total;
    }

    /** Soma a linha do utilizador u na matriz de empréstimos activos. */
    private int contarEmprestimosActivos(int u) {
        int total = 0;
        for (int l = 0; l < totalLivros; l++) {
            total += emprestimosActivos[u][l];
        }
        return total;
    }

    private void imprimirCabecalhoLivros() {
        System.out.printf("%-5s %-34s %-26s %-5s %s%n", "ID", "Título", "Autor", "Ano", "Disponíveis");
        System.out.println("-".repeat(85));
    }

    private void imprimirLivro(int i) {
        System.out.printf("%-5d %-34s %-26s %-5d %d de %d%n", idLivro[i],
                truncar(tituloLivro[i], 34), truncar(autorLivro[i], 26), anoLivro[i],
                quantidadeDisponivel[i], quantidadeTotal[i]);
    }

    /** Remove acentos e converte para minúsculas, para pesquisas mais tolerantes. */
    private static String normalizar(String texto) {
        String semAcentos = Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcentos.toLowerCase();
    }

    /** Encurta o texto (com "...") para que a tabela fique alinhada. */
    private static String truncar(String texto, int maximo) {
        if (texto.length() <= maximo) {
            return texto;
        }
        return texto.substring(0, maximo - 3) + "...";
    }
}
