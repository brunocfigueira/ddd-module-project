package hexagonal.modular.shared.usecases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.exception.StripeException;

/**
 * Representa um caso de uso que recebe uma entrada do tipo {@code I}
 * e retorna um resultado do tipo {@code O}.
 *
 * <p>Implementações desta interface encapsulam uma ação ou operação que pode
 * ser executada com um parâmetro de entrada do tipo {@code I}, retornando
 * um valor de resultado do tipo {@code O}.
 *
 * @param <I> o tipo do parâmetro de entrada para a execução do caso de uso
 * @param <O> o tipo do resultado retornado após a execução do caso de uso
 */
public interface IUseCase<I, O> {
    /**
     * Executa o caso de uso com o parâmetro de entrada fornecido.
     *
     * @param i o parâmetro de entrada para a execução, do tipo {@code I}
     * @return o resultado da execução, do tipo {@code O}
     */
    O execute(I i) throws StripeException, JsonProcessingException;
}
