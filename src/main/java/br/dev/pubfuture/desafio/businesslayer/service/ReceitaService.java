package br.dev.pubfuture.desafio.businesslayer.service;

import br.dev.pubfuture.desafio.businesslayer.Receita;
import br.dev.pubfuture.desafio.persistence.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Camada de serviço de Receita.
 * Neste aplicativo se preferiu utilizar a lógica de negócio junto ao controlador, mas poderia
 * estar aqui residido.
 */
@Service
public class ReceitaService {

    /**
     * Instanciação do objeto do repositório 'ReceitaRepository'.
     */
    private final ReceitaRepository receitaRepository;

    /**
     * Método construtor da classe Receita.
     * @param receitaRepository
     */
    @Autowired
    public ReceitaService(ReceitaRepository receitaRepository) {
        this.receitaRepository = receitaRepository;
    }

    /**
     * Este método retorna um objeto da classe 'Receita' para salvamento.
     * @param toSave parâmetro/objeto de salvamento.
     * @return Retorna um objeto Receita
     */
    public Receita save(Receita toSave) {
        return receitaRepository.save(toSave);
    }

    /**
     * A função desta método é remover uma receita através do seu 'id'.
     * @param id Código identificador do registro.
     */
    public void deleteById(Long id) { receitaRepository.deleteById(id); }

    /**
     * Este método retorna um objeto da classe 'Optional' para verificação se ele existe no banco de dados.
     * A referida classe é utilizada a fim de evitar NPE, ou seja, a mesma necessariamente conterá nada ou
     * uma valor não nulo.
     * @param id Código identificador do registro.
     * @return Retorna um objeto Optional vazio ou não nulo.
     */
    public Optional<Receita> findById(Long id) {
        return receitaRepository.findById(id);
    }

    /**
     * Método que retorna todas as receitas existentes na tabela correspondente (receita) do banco de dados.
     * @return Retorna uma coleção de todas as receitas existentes na base de dados.
     */
    public List<Receita> findAll() {
        return (List<Receita>) receitaRepository.findAll();
    }

    /**
     * Este método retorna o somatório de todas as receitas existentes na base de dados.
     * @return Retorna um 'float' correspondente ao somatório de todas as receitas cadastradas.
     */
    public Float totalBalance() { return receitaRepository.totalBalance(); }

    /**
     * Este método tem a finalidade de buscar as receitas cadastradas em um determinado intervalo de datas.
     * @param receivingDateStart Objeto LocalDate contendo a data inicial do intervalo de datas desejado.
     * @param receivingDateEnd Objeto LocalDate contendo a data final do intervalo de datas desejado.
     * @return Retorna uma coleção das receitas existentes na base de dados no intervalo desejado.
     */
    public List<Receita> findAllByReceivingDateBetween(LocalDate receivingDateStart, LocalDate receivingDateEnd) {
        return receitaRepository.findAllByReceivingDateBetween(receivingDateStart, receivingDateEnd);
    }

    /**
     * Este método tem a finalidade de buscar as receitas cadastradas por tipo de receita.
     * @param revenue Espera por um objeto da classe interna Receita.Revenue, o qual contém as constantes
     *                passíveis de ser utilizadas.
     * @return Retorna uma coleção das receitas existentes na base de dados por 'tipo de receita'.
     */
    public List<Receita> findReceitaByTypeOfRevenue(Receita.Revenue revenue) {
        return receitaRepository.findReceitaByTypeOfRevenue(revenue);
    }

}
