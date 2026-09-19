import java.util.Scanner;

/**
 * Classe utilitária responsável pela leitura e validação dos dados
 * introduzidos pelo utilizador na consola.
 *
 * Todos os métodos repetem a pergunta até que seja introduzido um valor válido,
 * evitando que o programa termine de forma inesperada (por exemplo, quando é
 * digitado texto onde se esperava um número).
 */
public class Entrada {

    /** Número máximo de caracteres aceites num campo de texto. */
    public static final int MAX_CARACTERES = 100;

    private static final Scanner LEITOR = new Scanner(System.in);

    /** Classe utilitária: não deve ser instanciada. */
    private Entrada() {
    }

    /**
     * Lê um texto não vazio. Remove espaços no início/fim e espaços repetidos.
     *
     * @param mensagem texto apresentado ao utilizador
     * @return o texto validado
     */
    public static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = LEITOR.nextLine().trim().replaceAll("\\s+", " ");
            if (texto.isEmpty()) {
                System.out.println("  [ERRO] O campo não pode ficar vazio. Tente novamente.");
            } else if (texto.length() > MAX_CARACTERES) {
                System.out.println("  [ERRO] O texto excede o limite de " + MAX_CARACTERES
                        + " caracteres. Tente novamente.");
            } else {
                return texto;
            }
        }
    }

    /**
     * Lê um número inteiro dentro de um intervalo (inclusive).
     *
     * @param mensagem texto apresentado ao utilizador
     * @param minimo   menor valor aceite
     * @param maximo   maior valor aceite
     * @return o número validado
     */
    public static int lerInteiro(String mensagem, int minimo, int maximo) {
        while (true) {
            System.out.print(mensagem);
            String linha = LEITOR.nextLine().trim();
            try {
                int valor = Integer.parseInt(linha);
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
                System.out.println("  [ERRO] Valor fora do intervalo permitido (" + minimo
                        + " a " + maximo + "). Tente novamente.");
            } catch (NumberFormatException e) {
                System.out.println("  [ERRO] Entrada inválida. Introduza um número inteiro.");
            }
        }
    }

    /**
     * Lê um identificador (ID): um número inteiro positivo.
     *
     * @param mensagem texto apresentado ao utilizador
     * @return o identificador validado
     */
    public static int lerIdentificador(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = LEITOR.nextLine().trim();
            try {
                int valor = Integer.parseInt(linha);
                if (valor > 0) {
                    return valor;
                }
                System.out.println("  [ERRO] O ID tem de ser um número inteiro positivo. Tente novamente.");
            } catch (NumberFormatException e) {
                System.out.println("  [ERRO] Entrada inválida. Introduza um número inteiro positivo.");
            }
        }
    }
}
