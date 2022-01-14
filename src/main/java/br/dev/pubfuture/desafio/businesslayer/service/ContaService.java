package br.dev.pubfuture.desafio.businesslayer.service;

import br.dev.pubfuture.desafio.businesslayer.Conta;
import br.dev.pubfuture.desafio.persistence.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Camada de serviço de Conta.
 * Neste aplicativo se preferiu utilizar a lógica de negócio junto ao controlador, mas poderia
 * estar aqui residido.
 */
@Service
public class ContaService {

    /**
     * Instanciação do objeto do repositório 'ContaRepository'.
     */
    private final ContaRepository contaRepository ;

    /**
     * Método construtor da classe Conta.
     * @param contaRepository
     */
    @Autowired
    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    /**
     * Este método retorna um objeto da classe 'Conta' para salvamento.
     * @param toSave parâmetro/objeto de salvamento.
     * @return Retorna um objeto Conta
     */
    public Conta save(Conta toSave) {
        return contaRepository.save(toSave);
    }

    /**
     * A função desta método é remover uma conta através do seu 'id'.
     * @param id Código identificador do registro.
     */
    public void deleteById(Long id) { contaRepository.deleteById(id); }

    /**
     * Este método retorna um objeto da classe 'Optional' para verificação se ele existe no banco de dados.
     * A referida classe é utilizada a fim de evitar NPE, ou seja, a mesma necessariamente conterá nada ou
     * uma valor não nulo.
     * @param id Código identificador do registro.
     * @return Retorna um objeto Optional vazio ou não nulo.
     */
    public Optional<Conta> findById(Long id) {
        return contaRepository.findById(id);
    }

    /**
     * Método que retorna todas as contas existentes na tabela correspondente (conta) do banco de dados.
     * @return Retorna uma coleção de todas as contas existentes na base de dados.
     */
    public List<Conta> findAll() {
        return (List<Conta>) contaRepository.findAll();
    }

    /**
     * Este método retorna o somatório de todas as contas existentes na base de dados.
     * @return Retorna um 'float' correspondente ao somatório de todas as contas cadastradas.
     */
    public Float totalBalance() { return contaRepository.totalBalance(); }

    /**
     * Este método auxiliar de transferência realiza o salvamento das informações na base de dados.
     * @param from Conta de origem onde será debitado o valor pretendido à transferência.
     * @param to Conta destino onde será creditado o valor recebido da transferência.
     */
    public void transferValue(Conta from, Conta to) {
        contaRepository.save(from);
        contaRepository.save(to);
    }

}
