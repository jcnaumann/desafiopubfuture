package br.dev.pubfuture.desafio.persistence;

import br.dev.pubfuture.desafio.businesslayer.Conta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de representação da camada de acesso ao banco de dados da classe Conta.
 */
@Repository
public interface ContaRepository extends CrudRepository<Conta, Long> {

    /**
     * Método abstrato para o retorno do somatório do saldo das contas cadastradas na base de dados.
     * Aqui se utilizou uma instrução SQL nativa a fim de lançar mão de diferentes abordagens na construção
     * do aplicativo, uma fez que se poderia utilizar a nomenclatura de nomes de consultas disponibilizadas pelo
     * próprio Spring Data JPA.
     * @return Retorna um objeto 'float' com o somatório dos saldos existentes nas contas cadastradas na base.
     */
    @Query(value = "SELECT SUM(saldo) FROM conta", nativeQuery = true)
    Float totalBalance();

}
