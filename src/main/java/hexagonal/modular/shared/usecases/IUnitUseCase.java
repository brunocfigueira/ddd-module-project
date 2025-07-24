package hexagonal.modular.shared.usecases;

/**
 * Representa um caso de uso que recebe uma entrada do tipo {@code I} e não retorna um resultado.
 *
 * <p>Implementações desta interface encapsulam uma ação ou operação que pode
 * ser executada com um parâmetro de entrada do tipo {@code I}, mas que não produz um valor de retorno.
 *
 * @param <I> o tipo do parâmetro de entrada para a execução do caso de uso
 */
public interface IUnitUseCase<I> {
    /**
     * Executa o caso de uso com o parâmetro de entrada fornecido.
     *
     * @param i o parâmetro de entrada para a execução, do tipo {@code I}
     */
    void execute(I i);
}

