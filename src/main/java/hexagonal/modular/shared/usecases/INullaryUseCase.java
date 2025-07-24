package hexagonal.modular.shared.usecases;

/**
 * Representa um caso de uso que não requer entrada e retorna um resultado.
 *
 * <p>Implementações desta interface encapsulam uma ação ou operação
 * que pode ser executada sem parâmetros de entrada e que retorna um resultado do tipo * {@code O}.
 *
 * @param <O> o tipo do resultado retornado após a execução do caso de uso
 */
public interface INullaryUseCase<O> {
    /**
     * Executa o caso de uso.
     *
     * @return o resultado da execução, do tipo {@code O}
     */
    O execute();
}