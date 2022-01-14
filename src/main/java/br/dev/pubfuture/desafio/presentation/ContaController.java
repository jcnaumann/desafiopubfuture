package br.dev.pubfuture.desafio.presentation;

import br.dev.pubfuture.desafio.businesslayer.Conta;
import br.dev.pubfuture.desafio.businesslayer.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

/**
 * Classe Controller Conta anotada para o controle do Spring MVC.
 * Esta classe proporciona os 'endpoints' da API referente à entidade 'conta', além das regras de negócio,
 * utilizando-se do 'design pattern' MVC.
 */
@RestController
public class ContaController {

    /**
     * Objeto da classe ContaService.
     */
    @Autowired
    ContaService contaService;

    /**
     * Endpoint '/api/conta/create' utilizado para o cadastro de contas na base de dados H2.
     * A anotação '@Valid' é utilizada para a corresponder a validação fixada na 'POJO class'.
     * @param conta Objeto conta serializado via Json.
     * @return O método retorna um 'ResponseEntity' contendo um map correspondendo ao 'id' recém criado
     * um 'status code', caso a resposta tenha sido OK.
     */
    @PostMapping("/api/conta/create")
    public ResponseEntity<HashMap<String, Long>> save(@Valid @RequestBody Conta conta) {
        //insert na base via Spring
        contaService.save(conta);
        //objeto map para retorno
        HashMap<String, Long> map = new HashMap<>();
        //insert no map para retorno no formado Json
        map.put("id", conta.getId());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * Endpoint '/api/conta/{id}' utilizado para a atualização de registro de contas na base de dados H2.
     * A anotação '@Valid' é utilizada para a corresponder a validação fixada na 'POJO class'.
     * O número do 'id' é enviado através do endereço URL.
     * @param conta Objeto conta serializado via Json.
     * @return O método retorna um 'ResponseEntity' contendo um map correspondendo ao 'id' recém alterado e
     * um 'status code', caso a resposta tenha sido OK.
     */
    @PutMapping("/api/conta/{id}")
    public ResponseEntity<HashMap<String, Long>> update(
            @PathVariable("id") long id, @Valid @RequestBody Conta conta) {
        //objeto Optional para blindar possíveis erros de NPE.
        Optional<Conta> accountOptional = contaService.findById(id);
        //testa de verificação se o objeto não está vazio
        if (accountOptional.isPresent()) {
            //lonbok builder em funcionamento, ou seja, instanciação do objeto conta update
            Conta update = Conta.builder()
                    .id(conta.getId())
                    .balance(conta.getBalance())
                    .accountType(conta.getAccountType())
                    .financialInstitution(conta.getFinancialInstitution())
                    .build();
            //salvamento
            contaService.save(update);
            //map de retorno Json com o 'id' recém alterado
            HashMap<String, Long> map = new HashMap<>();
            map.put("id", conta.getId());
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Endepoint '/api/conta/{id}' utilizado para a remoção de registros de contas na base de dados H2.
     * @param id O número do 'id' é enviado através do endereço URL.
     * @return Retorna um 'status code' ResponseEntity<Conta> OK (200) ou NOT FOUND (404).
     */
    @DeleteMapping("/api/conta/{id}")
    public ResponseEntity<Conta> delete(@PathVariable("id") long id) {
        //objeto Optional para blindar possíveis erros de NPE.
        Optional<Conta> contaOptional = contaService.findById(id);
        //testa de verificação se o objeto não está vazio
        if (contaOptional.isPresent()) {
            contaService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Endpoint utilizado para buscar registro de contas por seus respectivos 'id'.
     * @param id O número do 'id' é enviado através do endereço URL.
     * @return Retorna um 'status code' ResponseEntity<Conta> OK (200) ou NOT FOUND (404).
     */
    @GetMapping("/api/conta/{id}")
    public ResponseEntity<Conta> findById(@PathVariable("id") long id) {
        Optional<Conta> contaOptional = contaService.findById(id);
        if (contaOptional.isPresent()) {
            return new ResponseEntity<>(contaOptional.get(), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Endpoint utilizado para buscar todos os registros de contas cadastradas na base de dados.
     * @return Retorna uma coleção de contas serializadas.
     */
    @GetMapping("/api/conta")
    public List<Conta> listAll() {
        return contaService.findAll();
    }

    /**
     * Endpoint utilizado à realização de transferências entre contas existente na base de dados.
     * @param transferValue Espera um objeto da classe interna Conta.TransferValue como parâmetro do método.
     * @return O método retorna um 'ResponseEntity' contendo um map correspondendo com as informações das
     * contas e saldos recém alterados, além de um 'status code', caso a resposta tenha sido OK (202), ou
     * NOT FOUND (404) quando da ocorrência de um erro.
     */
    @PutMapping("/api/conta/transfer")
    public ResponseEntity<LinkedHashMap<String, Object>> transferValue(
            @RequestBody Conta.TransferValue transferValue) {

        //objetos recipientes que podem ou não conter um valor não nulo
        Optional<Conta> contaOptOrigem = contaService.findById(transferValue.getFrom());
        Optional<Conta> contaOptDestino = contaService.findById(transferValue.getTo());

        //testa se os objetos optionals não estão vazios
        if (contaOptOrigem.isPresent() && contaOptDestino.isPresent()) {
            //verifica se há saldo em conta
            if (contaOptOrigem.get().getBalance() - transferValue.getValue() >= 0) {
                //instancia os objetos
                Conta contaOrigem = contaOptOrigem.get();
                Conta contaDestino = contaOptDestino.get();
                //faz o débito na conta origem
                contaOrigem.setBalance(contaOrigem.getBalance() - transferValue.getValue());
                //faz o crédito na conta destino
                contaDestino.setBalance(contaDestino.getBalance() + transferValue.getValue());
                //salva na base
                contaService.transferValue(contaOrigem, contaDestino);
                //prepara a resposta no formato Json
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("id-origem", contaOrigem.getId());
                map.put("saldo-origem", contaOrigem.getBalance());
                map.put("id-destino", contaDestino.getId());
                map.put("saldo-destino", contaDestino.getBalance());
                return new ResponseEntity<>(map, HttpStatus.OK);
            } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Endpoint utilizado para retornar o somatório de saldos das contas cadastradas na base de dados.
     * @return O método retorna um 'ResponseEntity' contendo um map correspondendo ao 'id' recém alterado
     * um 'status code', caso a resposta tenha sido OK.
     */
    @GetMapping("/api/conta/total")
    public ResponseEntity<HashMap<String, Float>> totalBalance() {
        HashMap<String, Float> map = new HashMap<>();
        map.put("saldo", contaService.totalBalance());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
