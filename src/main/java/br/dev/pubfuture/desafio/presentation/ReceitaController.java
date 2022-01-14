package br.dev.pubfuture.desafio.presentation;

import br.dev.pubfuture.desafio.businesslayer.Receita;
import br.dev.pubfuture.desafio.businesslayer.service.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Classe Controller Receita anotada para o controle do Spring MVC.
 * Esta classe proporciona os 'endpoints' da API referente à entidade 'receita', além das regras de negócio,
 * utilizando-se do 'design pattern' MVC.
 */
@RestController
public class ReceitaController {

    /**
     * Objeto da classe ReceitaService.
     */
    @Autowired
    ReceitaService receitaService;

    /**
     * Endpoint '/api/receita/create' utilizado para o cadastro de receitas na base de dados H2.
     * A anotação '@Valid' é utilizada para a corresponder a validação fixada na 'POJO class'.
     * @param receita Objeto receita serializado via Json.
     * @return O método retorna um 'ResponseEntity' contendo um map correspondendo ao 'id' recém criado e
     * um 'status code', caso a resposta tenha sido OK.
     */
    @PostMapping("/api/receita/create")
    public ResponseEntity<HashMap<String, Long>> save(@Valid @RequestBody Receita receita) {
        //insert na base via Spring
        receitaService.save(receita);
        //objeto map para retorno
        HashMap<String, Long> map = new HashMap<>();
        //insert no map para retorno no formado Json
        map.put("id", receita.getId());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * Endpoint '/api/receita/{id}' utilizado para a atualização de registro de receitas na base de dados H2.
     * A anotação '@Valid' é utilizada para a corresponder a validação fixada na 'POJO class'.
     * O número do 'id' é enviado através do endereço URL.
     * @param receita Objeto receita serializado via Json.
     * @return O método retorna um 'ResponseEntity' contendo um map correspondendo ao 'id' recém alterado e
     * um 'status code', caso a resposta tenha sido OK.
     */
    @PutMapping("/api/receita/{id}")
    public ResponseEntity<HashMap<String, Long>> update(
            @PathVariable("id") long id, @Valid @RequestBody Receita receita) {
        //objeto Optional para blindar possíveis erros de NPE.
        Optional<Receita> receitaOptional = receitaService.findById(id);
        //testa de verificação se o objeto não está vazio
        if (receitaOptional.isPresent()) {
            //lonbok builder em funcionamento, ou seja, instanciação do objeto receita update
            Receita update = Receita.builder()
                    .id(receita.getId())
                    .value(receita.getValue())
                    .receivingDate(receita.getReceivingDate())
                    .expectedReceiptDate(receita.getExpectedReceiptDate())
                    .description(receita.getDescription())
                    .account(receita.getAccount())
                    .typeOfRevenue(receita.getTypeOfRevenue())
                    .build();
            //salvamento
            receitaService.save(update);
            //map de retorno Json com o 'id' recém alterado
            HashMap<String, Long> map = new HashMap<>();
            map.put("id", id);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else
            //caso null ou não encontrado, lança um 'status code' 404.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Endepoint '/api/receita/{id}' utilizado para a remoção de registros de receitas na base de dados H2.
     * @param id O número do 'id' é enviado através do endereço URL.
     * @return Retorna um 'status code' ResponseEntity<Receita> OK (200) ou NOT FOUND (404).
     */
    @DeleteMapping("/api/receita/{id}")
    public ResponseEntity<Receita> delete(@PathVariable("id") long id) {
        //objeto Optional para blindar possíveis erros de NPE.
        Optional<Receita> receitaOptional = receitaService.findById(id);
        //testa de verificação se o objeto não está vazio
        if (receitaOptional.isPresent()) {
            receitaService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Endpoint utilizado para buscar registro de receitas por seus respectivos 'id'.
     * @param id O número do 'id' é enviado através do endereço URL.
     * @return Retorna um 'status code' ResponseEntity<Receita> OK (200) ou NOT FOUND (404).
     */
    @GetMapping("/api/receita/{id}")
    public ResponseEntity<Receita> findById(@PathVariable("id") long id) {
        Optional<Receita> receitaOptional = receitaService.findById(id);
        if (receitaOptional.isPresent()) {
            return new ResponseEntity<>(receitaOptional.get(), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Endpoint utilizado para buscar todos os registros de receitas cadastrados na base de dados.
     * @return Retorna uma coleção de receitas serializadas.
     */
    @GetMapping("/api/receita/all")
    public List<Receita> listAll() {
        return receitaService.findAll();
    }

    /**
     * Endpoint utilizado para retornar o somatório de saldos das receitas cadastradas na base de dados.
     * @return O método retorna um 'ResponseEntity' contendo um map correspondendo ao 'id' recém alterado
     * um 'status code', caso a resposta tenha sido OK.
     */
    @GetMapping("/api/receita/total")
    public ResponseEntity<HashMap<String, Float>> totalBalance() {
        HashMap<String, Float> map = new HashMap<>();
        map.put("saldo", receitaService.totalBalance());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * Endpoint utilizado para o retorno de todos os registros existentes na base de dados e dentro
     * do intervalo de datas especificado.
     * Aqui se utilizou o padrão de nomes de consultas proporcionado pelo Spring.
     * @param dateRange Objeto instaciado da classe interna Receita.DateRange, contendo a data inicial e
     *                  final pretendida.
     * @return Retorna uma coleção de receitas dentro dos limites especificados.
     */
    @GetMapping("/api/receita")
    public List<Receita> searchByDateRange(@RequestBody Receita.DateRange dateRange) {
        return receitaService.findAllByReceivingDateBetween(dateRange.getStart(), dateRange.getEnd());
    }

    /**
     * Endpoint utilizado para o retorno de todos os registros existentes na base de dados por tipo de receita.
     * Aqui se utilizou o padrão de nomes de consultas proporcionado pelo Spring.
     * @param revenue Objeto instaciado da classe interna Receita.Revenue, contendo o tipo de receita pretendido.
     * @return Retorna uma coleção de receitas dentro dos limites especificados.
     */
    @GetMapping("/api/receita/tp/{type}")
    public List<Receita> findReceitaByRevenue(@PathVariable("type") Receita.Revenue revenue) {
        return receitaService.findReceitaByTypeOfRevenue(revenue);
    }

}
