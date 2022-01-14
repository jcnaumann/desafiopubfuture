package br.dev.pubfuture.desafio.presentation;

import br.dev.pubfuture.desafio.businesslayer.Conta;
import br.dev.pubfuture.desafio.businesslayer.service.ContaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de Unidade, Testes de integração.
 * @WebMvcTest especifica qual Controller será testado no parâmtetro da anotação.
 * @ExtendWith informa ao JUnit 5 para habilitar o suporte ao Spring.
 * Fontes: https://reflectoring.io/spring-boot-web-controller-test/
 *         https://stackabuse.com/guide-to-unit-testing-spring-boot-rest-apis/
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ContaController.class)
class ContaControllerTest {

    /**
     * Simula solicitações HTTP.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Fornece funcionalidade para leitura e gravação de JSON.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Simula a lógica de negócios.
     */
    @MockBean
    private ContaService contaService;

    /**
     * Membro de classe tipo Conta utilizado no construtor para instanciar um objeto modelo/exemplo.
     */
    Conta conta;

    /**
     * Contrutor que instancia um objeto 'conta'.
     */
    public ContaControllerTest() {
        this.conta = Conta.builder()
                .id(1)
                .balance(1)
                .accountType(Conta.AccountType.CONTA_CORRENTE)
                .financialInstitution("BANCO DO BRASIL")
                .build();
    }

    /**
     * Teste no escopo do método de salvamento.
     * Verifica se o controlador atende a solicitação HTTP POST /api/conta/create.
     * Também verifica se o controlador responde a URL /api/conta/create, além de verificar se o
     * método HTTP está correto (nesse caso POST) e se o tipo de conteúdo de solicitação está correto.
     * @throws Exception JsonProcessingException
     */
    @Test
    void testHTTPRequestEndpointCreate() throws Exception {
        mockMvc.perform(post("/api/conta/create")
                        .content(objectMapper.writeValueAsString(conta))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    /**
     * Verifica se a entrada do usuário foi desserializada com sucesso em um objeto Java, a fim de saber se o
     * método save() do controlador recebeu esses parâmetros como objetos Java e que eles foram
     * analisados com sucesso a partir da solicitação HTTP.
     * @throws Exception JsonProcessingException
     */
    @Test
    void testValidInputSaveNewAccount() throws Exception {
        mockMvc.perform(post("/api/conta/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(conta)))
                .andExpect(status().isOk());
    }

    /**
     * Testa se a validação da entidade falha conforme o esperado a partir do envio de um objeto JSON inválido
     * para o controlador. Em seguida o controlador deverá retornar o status HTTP 400 (Bad Request):
     * @throws Exception JsonProcessingException
     */
    @Test
    void testValidation() throws Exception {
        Conta c = conta = Conta.builder()
                .id(1)
                .balance(1)
                .accountType(Conta.AccountType.CONTA_CORRENTE)
                .financialInstitution("")
                .build();
        mockMvc.perform(post("/api/conta/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(conta)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Verifica se a lógica de negócios é chamada conforme o esperado.
     * @throws Exception JsonProcessingException
     */
    @Test
    void saveNewAccount() throws  Exception {
        mockMvc.perform(post("/api/conta/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(conta)))
                .andExpect(status().isOk());
        ArgumentCaptor<Conta> contaCaptor = ArgumentCaptor.forClass(Conta.class);
        verify(contaService, times(1)).save(contaCaptor.capture());
        assertThat(contaCaptor.getValue().getId()).isEqualTo(1);
        assertThat(contaCaptor.getValue().getBalance()).isEqualTo(1);
        assertThat(contaCaptor.getValue().getAccountType()).isEqualTo(Conta.AccountType.CONTA_CORRENTE);
        assertThat(contaCaptor.getValue().getFinancialInstitution()).isEqualTo("BANCO DO BRASIL");
    }

    /**
     * Verifica a serialização de saída após o salvamento do objeto (nesse caso uma string JSON com o
     * número do 'id' cadastrado).
     * @throws Exception JsonProcessingException
     */
    @Test
    void testValidOutputSaveNewAccount() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/conta/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(conta)))
                .andExpect(status().isOk())
                .andReturn();
        Conta expected = conta;
        Conta actualResponseBody = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Conta.class);
        assertThat(actualResponseBody.getId()).isEqualTo(expected.getId());
    }

    /**
     * Verifica se o controlador atende a solicitação HTTP POST /api/conta.
     * Também verifica se o controlador responde a URL /api/conta, além de verificar se o
     * método HTTP está correto (nesse caso GET) e se o tipo de conteúdo de solicitação está correto.
     * @throws Exception JsonProcessingException
     */
    @Test
    void testHTTPRequestEndpointListAll() throws Exception {
        mockMvc.perform(get("/api/conta")
                        .content(objectMapper.writeValueAsString(conta))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    /**
     * Verifica se o controlador retorna um id não nulo de uma conta existente na base de dados,
     * e se no campo accountType contém a palavra CONTA_CORRENTE.
     * @throws Exception
     */
    @Test
    void testListAccountById() throws Exception {
        Mockito.when(contaService.findById(conta.getId())).thenReturn(Optional.of(conta));
        mockMvc.perform(get("/api/conta/{id}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.accountType", is("CONTA_CORRENTE")));
    }

    /**
     * Verifica se o controlador retorna uma lista de todas as contas existentes na base de dados,
     * contando o número de itens retornados e se na posição 0 da coleção o campo financialInstitution
     * contém as palavras BANCO DO BRASIL.
     * @throws Exception
     */
    @Test
    void testListAll() throws Exception {
        List<Conta> contas = Arrays.asList(conta, conta, conta, conta);
        Mockito.when(contaService.findAll()).thenReturn(contas);
        mockMvc.perform(get("/api/conta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].financialInstitution", is("BANCO DO BRASIL")));
    }

    /**
     * Verifica se a resposta será Not Found quando do envio de um null object.
     * @throws Exception
     */
    @Test
    void testDeleteAccountNotFound() throws  Exception {
        Mockito.when(contaService.findById(5L)).thenReturn(null);
        mockMvc.perform(delete("/api/conta/{id}", "1")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    /**
     * Verifica se a resposta será OK quando do envio de um id válido (existente na base de dados).
     * @throws Exception
     */
    @Test
    void testDeleteAccount() throws  Exception {
        Mockito.when(contaService.findById(conta.getId())).thenReturn(Optional.of(conta));
        mockMvc.perform(delete("/api/conta/{id}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    /**
     * Verifica se o controlador realiza uma tranferência com sucesso, retornando um objeto JSON com o resumo
     * da operação.
     * @throws Exception
     */
    @Test
    void testTransfer() throws Exception {
        //instancia dois objetos Conta para utilizar no Mock
        Conta contaOrigem  = this.conta;
        Conta contaDestino = Conta.builder()
                .id(2)
                .balance(1)
                .accountType(Conta.AccountType.CONTA_CORRENTE)
                .financialInstitution("BANCO DO BRASIL")
                .build();
        //instancia um objeto Conta.TransferValue com as informações da operação
        Conta.TransferValue transferValue = new Conta.TransferValue(
                contaOrigem.getId(),
                contaDestino.getId(),
                1
        );
        //prepara a simulação repassando os id utilizados no teste
        Mockito.when(contaService.findById(contaOrigem.getId())).thenReturn(Optional.of(contaOrigem));
        Mockito.when(contaService.findById(contaDestino.getId())).thenReturn(Optional.of(contaDestino));
        //afirmação das expectativas de retorno
        mockMvc.perform(put("/api/conta/transfer")
                        .content(objectMapper.writeValueAsString(transferValue))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id-origem", is(1)))
                .andExpect(jsonPath("$.saldo-origem", is(0.0)))
                .andExpect(jsonPath("$.id-destino", is(2)))
                .andExpect(jsonPath("$.saldo-destino", is(2.0)));
    }

    /**
     * Verifica se o controlador retorna NOT FOUND quando é passado id ou id's inexistentes na base.
     * @throws Exception
     */
    @Test
    void testTransferNotFound() throws Exception {
        //instancia dois objetos Conta para utilizar no Mock
        Conta contaOrigem  = this.conta;
        Conta contaDestino = Conta.builder()
                .id(2)
                .balance(1)
                .accountType(Conta.AccountType.CONTA_CORRENTE)
                .financialInstitution("BANCO DO BRASIL")
                .build();
        //instancia um objeto Conta.TransferValue com as informações da operação
        Conta.TransferValue transferValue = new Conta.TransferValue(1,2,1);
        //prepara a simulação repassando os id utilizados no teste
        Mockito.when(contaService.findById(contaOrigem.getId())).thenReturn(Optional.empty());
        Mockito.when(contaService.findById(contaDestino.getId())).thenReturn(Optional.empty());
        //afirmação das expectativas de retorno
        mockMvc.perform(put("/api/conta/transfer")
                        .content(objectMapper.writeValueAsString(transferValue))
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    /**
     * Verifica se o controlador retorna NOT FOUND quando é passado id ou id's inexistentes na base.
     * @throws Exception
     */
    @Test
    void testTransferBadRequest() throws Exception {
        //instancia dois objetos Conta para utilizar no Mock
        Conta contaOrigem = Conta.builder()
                .id(2)
                .balance(0)
                .accountType(Conta.AccountType.CONTA_CORRENTE)
                .financialInstitution("BANCO DO BRASIL")
                .build();
        Conta contaDestino = this.conta;
        //instancia um objeto Conta.TransferValue com as informações da operação
        Conta.TransferValue transferValue = new Conta.TransferValue(2,1,1);
        //prepara a simulação repassando os id utilizados no teste
        Mockito.when(contaService.findById(contaOrigem.getId())).thenReturn(Optional.of(contaOrigem));
        Mockito.when(contaService.findById(contaDestino.getId())).thenReturn(Optional.of(contaDestino));
        //afirmação das expectativas de retorno
        mockMvc.perform(put("/api/conta/transfer")
                        .content(objectMapper.writeValueAsString(transferValue))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

}