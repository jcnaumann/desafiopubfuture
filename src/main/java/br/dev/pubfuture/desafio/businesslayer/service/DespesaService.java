package br.dev.pubfuture.desafio.businesslayer.service;

import br.dev.pubfuture.desafio.businesslayer.Despesa;
import br.dev.pubfuture.desafio.persistence.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Camada de serviço de Despesa.
 * Neste aplicativo se preferiu utilizar a lógica de negócio junto ao controlador, mas poderia
 * estar aqui residido.
 */
@Service
public class DespesaService {

    /**
     * Instanciação do objeto do repositório 'DespesaRepository'.
     */
    private final DespesaRepository despesaRepository;

    /**
     * Método construtor da classe Despesa.
     * @param despesaRepository
     */
    @Autowired
    public DespesaService(DespesaRepository despesaRepository) {
        this.despesaRepository = despesaRepository;
    }

    /**
     * Este método retorna um objeto da classe 'Despesa' para salvamento.
     * @param toSave parâmetro/objeto de salvamento.
     * @return Retorna um objeto Despesa
     */
    public Despesa save(Despesa toSave) {
        return despesaRepository.save(toSave);
    }

    /**
     * A função desta método é remover uma despesa através do seu 'id'.
     * @param id Código identificador do registro.
     */
    public void deleteById(Long id) { despesaRepository.deleteById(id); }

    /**
     * Este método retorna um objeto da classe 'Optional' para verificação se ele existe no banco de dados.
     * A referida classe é utilizada a fim de evitar NPE, ou seja, a mesma necessariamente conterá nada ou
     * uma valor não nulo.
     * @param id Código identificador do registro.
     * @return Retorna um objeto Optional vazio ou não nulo.
     */
    public Optional<Despesa> findById(Long id) { return despesaRepository.findById(id); }

    /**
     * Método que retorna todas as despesas existentes na tabela correspondente (despesa) do banco de dados.
     * @return Retorna uma coleção de todas as despesas existentes na base de dados.
     */
    public List<Despesa> findAll() {
        return (List<Despesa>) despesaRepository.findAll();
    }

    /**
     * Este método retorna o somatório de todas as despesas existentes na base de dados.
     * @return Retorna um 'float' correspondente ao somatório de todas as despesas cadastradas.
     */
    public Float totalExpenses() { return despesaRepository.totalExpenses(); }

    /**
     * Este método tem a finalidade de buscar as despesas cadastradas em um determinado intervalo de datas.
     * @param paydayStart Objeto LocalDate contendo a data inicial do intervalo de datas desejado.
     * @param paydayEnd Objeto LocalDate contendo a data final do intervalo de datas desejado.
     * @return Retorna uma coleção das despesas existentes na base de dados no intervalo desejado.
     */
    public List<Despesa> findAllByPaydayBetween(LocalDate paydayStart, LocalDate paydayEnd) {
        return despesaRepository.findAllByPaydayBetween(paydayStart, paydayEnd);
    }

    /**
     * Este método tem a finalidade de buscar as despesas cadastradas por tipo de despesa.
     * @param typeOfExpense Espera por um objeto da classe interna Despesa.TypeOfExpense, o qual contém
     *                as constantes passíveis de ser utilizadas.
     * @return Retorna uma coleção das despesas existentes na base de dados por 'tipo de despesa'.
     */
    public List<Despesa> findDespesaByTypeOfExpense(Despesa.TypeOfExpense typeOfExpense) {
        return despesaRepository.findDespesaByTypeOfExpense(typeOfExpense);
    }

}
