package br.dev.pubfuture.desafio.persistence;

import br.dev.pubfuture.desafio.businesslayer.Receita;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface de representação da camada de acesso ao banco de dados da classe Receita.
 */
@Repository
public interface ReceitaRepository extends CrudRepository<Receita, Long> {

    /**
     * Método abstrato para o retorno do somatório do saldo das receitas cadastradas na base de dados.
     * Aqui se utilizou uma instrução SQL nativa a fim de lançar mão de diferentes abordagens na construção
     * do aplicativo, uma fez que se poderia utilizar a nomenclatura de nomes de consultas disponibilizadas pelo
     * próprio Spring Data JPA.
     * @return Retorna um objeto 'float' com o somatório dos saldos existentes nas receitas cadastradas na base.
     */
    @Query(value = "SELECT SUM(valor) FROM receita", nativeQuery = true)
    Float totalBalance();

    /**
     * Método abstrato que encontra os registros em um determinado intervalo de datas.
     * @param receivingDateStart Data inicial.
     * @param receivingDateEnd Data final.
     * @return Retorna uma coleção de receitas.
     */
    List<Receita> findAllByReceivingDateBetween(LocalDate receivingDateStart, LocalDate receivingDateEnd);

    /**
     * Método abstrato que encontra os registros com um determinado tipo de receita.
     * @param revenue Objeto da classe interna Receita.Revenue.
     * @returnRetorna uma coleção de receitas.
     */
    List<Receita> findReceitaByTypeOfRevenue(Receita.Revenue revenue);

}
