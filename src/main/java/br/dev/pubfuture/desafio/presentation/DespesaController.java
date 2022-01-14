package br.dev.pubfuture.desafio.presentation;

import br.dev.pubfuture.desafio.businesslayer.Despesa;
import br.dev.pubfuture.desafio.businesslayer.service.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Classe Controller Despesa anotada para o controle do Spring MVC.
 * Esta classe proporciona os 'endpoints' da API referente à entidade 'despesa', além das regras de negócio,
 * utilizando-se do 'design pattern' MVC.
 */
@RestController
public class DespesaController {

    /**
     * Objeto da classe DespesaService.
     */
    @Autowired
    DespesaService despesaService;

    /**
     * Endpoint '/api/despesa/create' utilizado para o cadastro de despesas na base de dados H2.
     * A anotação '@Valid' é utilizada para a corresponder a validação fixada na 'POJO class'.
     * @param despesa Objeto despesa serializado via Json.
     * @return O método retorna um 'ResponseEntity' contendo um map correspondendo ao 'id' recém criado e
     * um 'status code', caso a resposta tenha sido OK.
     */
    @PostMapping("/api/despesa/create")
    public ResponseEntity<HashMap<String, Long>> save(@Valid @RequestBody Despesa despesa) {
        //insert na base via Spring
        despesaService.save(despesa);
        //objeto map para retorno
        HashMap<String, Long> map = new HashMap<>();
        //insert no map para retorno no formado Json
        map.put("id", despesa.getId());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * Endpoint '/api/despesa/{id}' utilizado para a atualização de registro de despesas na base de dados H2.
     * A anotação '@Valid' é utilizada para a corresponder a validação fixada na 'POJO class'.
     * O número do 'id' é enviado através do endereço URL.
     * @param despesa Objeto despesa serializado via Json.
     * @return O método retorna um 'ResponseEntity' contendo um map correspondendo ao 'id' recém alterado e
     * um 'status code', caso a resposta tenha sido OK.
     */
    @PutMapping("/api/despesa/{id}")
    public ResponseEntity<HashMap<String, Long>> update(
            @PathVariable("id") long id, @Valid @RequestBody Despesa despesa) {
        //objeto Optional para blindar possíveis erros de NPE.
        Optional<Despesa> despesaOptional = despesaService.findById(id);
        //testa de verificação se o objeto não está vazio
        if (despesaOptional.isPresent()) {
            //lonbok builder em funcionamento, ou seja, instanciação do objeto despesa update
            Despesa update = Despesa.builder()
                    .id(despesa.getId())
                    .value(despesa.getValue())
                    .payday(despesa.getPayday())
                    .expectedPaymentDate(despesa.getExpectedPaymentDate())
                    .typeOfExpense(despesa.getTypeOfExpense())
                    .account(despesa.getAccount())
                    .build();
            //salvamento
            despesaService.save(update);
            HashMap<String, Long> map = new HashMap<>();
            //map de retorno Json com o 'id' recém alterado
            map.put("id", id);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else
            //caso null ou não encontrado, lança um 'status code' 404.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Endepoint '/api/despesa/{id}' utilizado para a remoção de registros de despesas na base de dados H2.
     * @param id O número do 'id' é enviado através do endereço URL.
     * @return Retorna um 'status code' ResponseEntity<Despesa> OK (200) ou NOT FOUND (404).
     */
    @DeleteMapping("/api/despesa/{id}")
    public ResponseEntity<Despesa> delete(@PathVariable("id") long id) {
        //objeto Optional para blindar possíveis erros de NPE.
        Optional<Despesa> despesaOptional = despesaService.findById(id);
        //testa de verificação se o objeto não está vazio
        if (despesaOptional.isPresent()) {
            despesaService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Endpoint utilizado para buscar registro de despesas por seus respectivos 'id'.
     * @param id O número do 'id' é enviado através do endereço URL.
     * @return Retorna um 'status code' ResponseEntity<Despesa> OK (200) ou NOT FOUND (404).
     */
    @GetMapping("/api/despesa/{id}")
    public ResponseEntity<Despesa> findById(@PathVariable("id") long id) {
        Optional<Despesa> despesaOptional = despesaService.findById(id);
        if (despesaOptional.isPresent()) {
            return new ResponseEntity<>(despesaOptional.get(), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Endpoint utilizado para buscar todos os registros de despesas cadastrados na base de dados.
     * @return Retorna uma coleção de despesas serializadas.
     */
    @GetMapping("/api/despesa/all")
    public List<Despesa> listAll() {
        return despesaService.findAll();
    }

    /**
     * Endpoint utilizado para retornar o somatório de saldos das despesas cadastradas na base de dados.
     * @return O método retorna um 'ResponseEntity' contendo um map correspondendo ao 'id' recém alterado
     * um 'status code', caso a resposta tenha sido OK.
     */
    @GetMapping("/api/despesa/total")
    public ResponseEntity<HashMap<String, Float>> totalExpenses() {
        HashMap<String, Float> map = new HashMap<>();
        map.put("despesa-total", despesaService.totalExpenses());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * Endpoint utilizado para o retorno de todos os registros existentes na base de dados e dentro
     * do intervalo de datas especificado.
     * Aqui se utilizou o padrão de nomes de consultas proporcionado pelo Spring.
     * @param dateRange Objeto instaciado da classe interna Despesa.DateRange contendo a data inicial e
     *                  final pretendida.
     * @return Retorna uma coleção de despesas dentro dos limites especificados.
     */
    @GetMapping("/api/despesa")
    public List<Despesa> searchByDateRange(@RequestBody Despesa.DateRange dateRange) {
        return despesaService.findAllByPaydayBetween(dateRange.getStart(), dateRange.getEnd());
    }

    /**
     * Endpoint utilizado para o retorno de todos os registros existentes na base de dados por tipo de despesa.
     * Aqui se utilizou o padrão de nomes de consultas proporcionado pelo Spring.
     * @param typeOfExpense Objeto instaciado da classe interna Despesa.TypeOfExpense,
     *                      contendo o tipo de despesa pretendido.
     * @return Retorna uma coleção de despesas dentro dos limites especificados.
     */
    @GetMapping("/api/despesa/tp/{type}")
    public List<Despesa> findDespesaByTypeOfExpense(@PathVariable("type") Despesa.TypeOfExpense typeOfExpense) {
        return despesaService.findDespesaByTypeOfExpense(typeOfExpense);
    }

}
