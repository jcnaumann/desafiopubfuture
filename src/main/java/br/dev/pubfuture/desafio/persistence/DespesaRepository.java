package br.dev.pubfuture.desafio.persistence;

import br.dev.pubfuture.desafio.businesslayer.Despesa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface de representação da camada de acesso ao banco de dados da classe Despesa.
 */
@Repository
public interface DespesaRepository extends CrudRepository<Despesa, Long> {

    /**
     * Método abstrato para o retorno do somatório do saldo das despesas cadastradas na base de dados.
     * Aqui se utilizou uma instrução SQL nativa a fim de lançar mão de diferentes abordagens na construção
     * do aplicativo, uma fez que se poderia utilizar a nomenclatura de nomes de consultas disponibilizadas pelo
     * próprio Spring Data JPA.
     * @return Retorna um objeto 'float' com o somatório dos saldos existentes nas receitas cadastradas na base.
     */
    @Query(value = "SELECT SUM(valor) FROM despesa", nativeQuery = true)
    Float totalExpenses();

    /**
     * Método abstrato que encontra os registros existentes em um determinado intervalo de datas.
     * @param paydayStart Data inicial.
     * @param paydayEnd Data final.
     * @return Retorna uma coleção de despesas.
     */
    List<Despesa> findAllByPaydayBetween(LocalDate paydayStart, LocalDate paydayEnd);

    /**
     * Método abstrato que encontra os registros com um determinado tipo de despesa.
     * @param typeOfExpense Objeto da classe interna Receita.Revenue.
     * @returnRetorna uma coleção de receitas.
     */
    List<Despesa> findDespesaByTypeOfExpense(Despesa.TypeOfExpense typeOfExpense);

}
