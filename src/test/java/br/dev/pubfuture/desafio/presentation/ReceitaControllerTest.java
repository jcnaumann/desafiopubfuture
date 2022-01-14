package br.dev.pubfuture.desafio.presentation;

import br.dev.pubfuture.desafio.businesslayer.Receita;
import br.dev.pubfuture.desafio.businesslayer.service.ReceitaService;
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
 *
 * @WebMvcTest especifica qual Controller será testado no parâmtetro da anotação.
 * @ExtendWith informa ao JUnit 5 para habilitar o suporte ao Spring.
 * Fonte: https://reflectoring.io/spring-boot-web-controller-test/
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ReceitaController.class)
class ReceitaControllerTest {

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
    private ReceitaService receitaService;

    /**
     * Membro de classe tipo Receita utilizado no construtor para instanciar um objeto modelo/exemplo.
     */
    Receita receita;

    /**
     * Contrutor que instancia um objeto 'receita'.
     */
    public ReceitaControllerTest() {
        this.receita = receita.builder()
                .id(1)
                .value(1)
                .receivingDate(LocalDate.parse("2022-01-16"))
                .expectedReceiptDate(LocalDate.parse("2022-01-16"))
                .description("TESTE")
                .account(999)
                .typeOfRevenue(br.dev.pubfuture.desafio.businesslayer.Receita.Revenue.PRESENTE)
                .build();
    }

    /**
     * Teste no escopo do método de salvamento.
     * Verifica se o controlador atende a solicitação HTTP POST /api/receita/create.
     * Também verifica se o controlador responde a URL /api/receita/create, além de verificar se o
     * método HTTP está correto (nesse caso POST) e se o tipo de conteúdo de solicitação está correto.
     *
     * @throws Exception JsonProcessingException
     */
    @Test
    void testHTTPRequestEndpointCreate() throws Exception {
        mockMvc.perform(post("/api/receita/create")
                        .content(objectMapper.writeValueAsString(receita))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    /**
     * Verifica se a entrada do usuário foi desserializada com sucesso em um objeto Java, a fim de saber se o
     * método save() do controlador recebeu esses parâmetros como objetos Java e que eles foram
     * analisados com sucesso a partir da solicitação HTTP.
     *
     * @throws Exception JsonProcessingException
     */
    @Test
    void testValidInputSaveNewAccount() throws Exception {
        mockMvc.perform(post("/api/receita/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(receita)))
                .andExpect(status().isOk());
    }

    /**
     * Testa se a validação da entidade falha conforme o esperado a partir do envio de um objeto JSON inválido
     * para o controlador. Em seguida o controlador deverá retornar o status HTTP 400 (Bad Request):
     *
     * @throws Exception JsonProcessingException
     */
    @Test
    void testValidation() throws Exception {
        Receita r = receita.builder()
                .id(1)
                .value(1)
                .receivingDate(LocalDate.parse("2022-01-16"))
                .expectedReceiptDate(LocalDate.parse("2022-01-16"))
                .description("")
                .account(1000)
                .typeOfRevenue(br.dev.pubfuture.desafio.businesslayer.Receita.Revenue.PRESENTE)
                .build();
        mockMvc.perform(post("/api/receita/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(r)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Verifica se a lógica de negócios é chamada conforme o esperado.
     *
     * @throws Exception JsonProcessingException
     */
    @Test
    void saveNewAccount() throws Exception {
        mockMvc.perform(post("/api/receita/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(receita)))
                .andExpect(status().isOk());
        ArgumentCaptor<Receita> receitaCaptor = ArgumentCaptor.forClass(Receita.class);
        verify(receitaService, times(1)).save(receitaCaptor.capture());
        assertThat(receitaCaptor.getValue().getId()).isEqualTo(1);
        assertThat(receitaCaptor.getValue().getValue()).isEqualTo(1);
        assertThat(receitaCaptor.getValue().getReceivingDate()).isEqualTo(LocalDate.parse("2022-01-16"));
        assertThat(receitaCaptor.getValue().getExpectedReceiptDate()).isEqualTo(LocalDate.parse("2022-01-16"));
        assertThat(receitaCaptor.getValue().getDescription()).isEqualTo("TESTE");
        assertThat(receitaCaptor.getValue().getAccount()).isEqualTo(999);
    }

    /**
     * Verifica a serialização de saída após o salvamento do objeto (nesse caso uma string JSON com o
     * número do 'id' cadastrado).
     *
     * @throws Exception JsonProcessingException
     */
    @Test
    void testValidOutputSaveNewAccount() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/receita/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(receita)))
                .andExpect(status().isOk())
                .andReturn();
        Receita expected = receita;
        Receita actualResponseBody = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Receita.class);
        assertThat(actualResponseBody.getId()).isEqualTo(expected.getId());
    }

    /**
     * Verifica se o controlador atende a solicitação HTTP POST /api/receita.
     * Também verifica se o controlador responde a URL /api/receita, além de verificar se o
     * método HTTP está correto (nesse caso GET) e se o tipo de conteúdo de solicitação está correto.
     *
     * @throws Exception JsonProcessingException
     */
    @Test
    void testHTTPRequestEndpointListAll() throws Exception {
        mockMvc.perform(get("/api/receita")
                        .content(objectMapper.writeValueAsString(receita))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    /**
     * Verifica se o controlador retorna um id não nulo de uma receita existente na base de dados,
     * e se no campo typeOfRevenue contém a palavra PRESENTE.
     * @throws Exception
     */
    @Test
    void testListExpenseById() throws Exception {
        Mockito.when(receitaService.findById(receita.getId())).thenReturn(Optional.of(receita));
        mockMvc.perform(get("/api/receita/{id}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.typeOfRevenue", is("PRESENTE")));
    }

    /**
     * Verifica se o controlador retorna uma lista de todas as receitas existentes na base de dados,
     * contando o número de itens retornados e se na posição 0 da coleção o campo description
     * contém as palavras TESTE.
     * @throws Exception
     */
    @Test
    void testListAll() throws Exception {
        List<Receita> receitas = Arrays.asList(receita, receita, receita, receita);
        Mockito.when(receitaService.findAll()).thenReturn(receitas);
        mockMvc.perform(get("/api/receita/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].description", is("TESTE")));
    }

    /**
     * Verifica se a resposta será Not Found quando do envio de um null object.
     * @throws Exception
     */
    @Test
    void testDeleteRevenueNotFound() throws  Exception {
        Mockito.when(receitaService.findById(5L)).thenReturn(null);
        mockMvc.perform(delete("/api/receita/{id}", "1")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    /**
     * Verifica se a resposta será OK quando do envio de um id válido (existente na base de dados).
     * @throws Exception
     */
    @Test
    void testDeleteAccount() throws  Exception {
        Mockito.when(receitaService.findById(receita.getId())).thenReturn(Optional.of(receita));
        mockMvc.perform(delete("/api/receita/{id}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

}