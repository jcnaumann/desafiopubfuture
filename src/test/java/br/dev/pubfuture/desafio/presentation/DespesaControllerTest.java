package br.dev.pubfuture.desafio.presentation;

import br.dev.pubfuture.desafio.businesslayer.Despesa;
import br.dev.pubfuture.desafio.businesslayer.service.DespesaService;
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
import java.time.LocalDate;
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
 * Fonte: https://reflectoring.io/spring-boot-web-controller-test/
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DespesaController.class)
class DespesaControllerTest {

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
    private DespesaService despesaService;

    /**
     * Membro de classe tipo Despesa utilizado no construtor para instanciar um objeto modelo/exemplo.
     */
    Despesa despesa;

    /**
     * Contrutor que instancia um objeto 'despesa'.
     */
    public DespesaControllerTest() {
        this.despesa = Despesa.builder()
                .id(1)
                .value(1)
                .payday(LocalDate.parse("2022-01-16"))
                .expectedPaymentDate(LocalDate.parse("2022-01-16"))
                .typeOfExpense(Despesa.TypeOfExpense.LAZER)
                .account(999)
                .build();
    }

    /**
     * Teste no escopo do método de salvamento.
     * Verifica se o controlador atende a solicitação HTTP POST /api/despesa/create.
     * Também verifica se o controlador responde a URL /api/despesa/create, além de verificar se o
     * método HTTP está correto (nesse caso POST) e se o tipo de conteúdo de solicitação está correto.
     * @throws Exception JsonProcessingException
     */
    @Test
    void testHTTPRequestEndpointCreate() throws Exception {
        mockMvc.perform(post("/api/despesa/create")
                        .content(objectMapper.writeValueAsString(despesa))
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
        mockMvc.perform(post("/api/despesa/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(despesa)))
                .andExpect(status().isOk());
    }

    /**
     * Testa se a validação da entidade falha conforme o esperado a partir do envio de um objeto JSON inválido
     * para o controlador. Em seguida o controlador deverá retornar o status HTTP 400 (Bad Request):
     * @throws Exception JsonProcessingException
     */
    @Test
    void testValidation() throws Exception {
        Despesa d = despesa = Despesa.builder()
                .id(1)
                .value(1)
                .payday(LocalDate.parse("2022-01-16"))
                .expectedPaymentDate(LocalDate.parse("2022-01-16"))
                .typeOfExpense(Despesa.TypeOfExpense.LAZER)
                .account(1000)
                .build();
        mockMvc.perform(post("/api/despesa/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(d)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Verifica se a lógica de negócios é chamada conforme o esperado.
     * @throws Exception JsonProcessingException
     */
    @Test
    void saveNewAccount() throws  Exception {
        mockMvc.perform(post("/api/despesa/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(despesa)))
                .andExpect(status().isOk());
        ArgumentCaptor<Despesa> despesaCaptor = ArgumentCaptor.forClass(Despesa.class);
        verify(despesaService, times(1)).save(despesaCaptor.capture());
        assertThat(despesaCaptor.getValue().getId()).isEqualTo(1);
        assertThat(despesaCaptor.getValue().getValue()).isEqualTo(1);
        assertThat(despesaCaptor.getValue().getPayday()).isEqualTo(LocalDate.parse("2022-01-16"));
        assertThat(despesaCaptor.getValue().getExpectedPaymentDate()).isEqualTo(LocalDate.parse("2022-01-16"));
        assertThat(despesaCaptor.getValue().getTypeOfExpense()).isEqualTo(Despesa.TypeOfExpense.LAZER);
        assertThat(despesaCaptor.getValue().getAccount()).isEqualTo(999);
    }

    /**
     * Verifica a serialização de saída após o salvamento do objeto (nesse caso uma string JSON com o
     * número do 'id' cadastrado).
     * @throws Exception JsonProcessingException
     */
    @Test
    void testValidOutputSaveNewAccount() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/despesa/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(despesa)))
                .andExpect(status().isOk())
                .andReturn();
        Despesa expected = despesa;
        Despesa actualResponseBody = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Despesa.class);
        assertThat(actualResponseBody.getId()).isEqualTo(expected.getId());
    }

    /**
     * Verifica se o controlador atende a solicitação HTTP POST /api/despesa.
     * Também verifica se o controlador responde a URL /api/despesa, além de verificar se o
     * método HTTP está correto (nesse caso GET) e se o tipo de conteúdo de solicitação está correto.
     * @throws Exception JsonProcessingException
     */
    @Test
    void testHTTPRequestEndpointListAll() throws Exception {
        mockMvc.perform(get("/api/despesa")
                        .content(objectMapper.writeValueAsString(despesa))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    /**
     * Verifica se o controlador retorna um id não nulo de uma despesa existente na base de dados,
     * e se no campo typeOfExpense contém a palavra LAZER.
     * @throws Exception
     */
    @Test
    void testListExpenseById() throws Exception {
        Mockito.when(despesaService.findById(despesa.getId())).thenReturn(Optional.of(despesa));
        mockMvc.perform(get("/api/despesa/{id}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.typeOfExpense", is("LAZER")));
    }

    /**
     * Verifica se o controlador retorna uma lista de todas as despesas existentes na base de dados,
     * contando o número de itens retornados e se na posição 0 da coleção o campo account
     * contém o número 999.
     * @throws Exception
     */
    @Test
    void testListAll() throws Exception {
        List<Despesa> despesas = Arrays.asList(despesa, despesa, despesa, despesa);
        Mockito.when(despesaService.findAll()).thenReturn(despesas);
        mockMvc.perform(get("/api/despesa/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].account", is(999)));
    }

    /**
     * Verifica se a resposta será Not Found quando do envio de um null object.
     * @throws Exception
     */
    @Test
    void testDeleteExpenseNotFound() throws  Exception {
        Mockito.when(despesaService.findById(5L)).thenReturn(null);
        mockMvc.perform(delete("/api/despesa/{id}", "1")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    /**
     * Verifica se a resposta será OK quando do envio de um id válido (existente na base de dados).
     * @throws Exception
     */
    @Test
    void testDeleteAccount() throws  Exception {
        Mockito.when(despesaService.findById(despesa.getId())).thenReturn(Optional.of(despesa));
        mockMvc.perform(delete("/api/despesa/{id}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

}