package br.dev.pubfuture.desafio.businesslayer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * Entidade JPA Receita, utilizada para definição dos membros da classe, representando os dados que serão persistidos
 * em uma tabela denominada 'receita' do banco de dados H2.
 * A fim de evitar código clichê, foi utilizada a biblioteca 'Project Lombok', a qual automatiza a criação dos
 * métodos 'Setters' e 'Getters', construtores, toString, builders, etc.
 *
 * @author Jean C. Naumann
 */
@Entity
@Table(name = "receita")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Receita {

    /**
     * Campo Id incrementado automaticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Campo validado 'value' cujo nome da coluna na tabela do banco de dados será 'valor'.
     * Não aceita entrada nula, propondo uma mensagem para validação desconforme.
     */
    @Column(name = "valor")
    @NotNull(message = "Valor não pode ser nulo.")
    private float value;

    /**
     * Campo validado 'receivingDate' cujo nome da coluna na tabela do banco de dados será 'dt_receb'.
     * Utilizou-se a anotação '@JsonFormat' para configurar como as datas devem ser serializadas.
     * Não aceita entrada nula, propondo uma mensagem para validação desconforme.
     */
    @Column(name = "dt_receb")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "Data de recebimento não pode ser nula.")
    private LocalDate receivingDate;

    /**
     * Campo validado 'expectedReceiptDate' cujo nome da coluna na tabela do banco de dados será 'dt_recb_esp'.
     * Utilizou-se a anotação '@JsonFormat' para configurar como as datas devem ser serializadas.
     * Não aceita entrada nula, propondo uma mensagem para validação desconforme.
     */
    @Column(name = "dt_recb_esp")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "Data de recebimento esperado não pode ser nula.")
    private LocalDate expectedReceiptDate;

    /**
     * Campo validado 'description' cujo nome da coluna na tabela do banco de dados será 'descricao'.
     * Utilizou-se a anotação '@Size' para limitar o número de caracteres a 255.
     * Não aceita entrada em branco, propondo uma mensagem para validação desconforme.
     */
    @Column(name = "descricao")
    @NotBlank(message = "Descrição não pode ficar em branco.")
    @Size(max = 255)
    private String description;

    /**
     * Campo validado 'account' cujo nome da coluna na tabela do banco de dados será 'conta'.
     * Utilizou-se a anotação '@Min' e '@Max' para configurar limitar o intervalo númerico aceito.
     * Não aceita entrada nula, propondo uma mensagem para validação desconforme.
     */
    @Column(name = "conta")
    @NotNull(message = "Conta não pode ser nula.")
    @Min(1) @Max(999)
    private int account;

    /**
     * Campo validado 'typeOfRevenue' cujo nome da coluna na tabela do banco de dados será 'tp_receita'.
     * Trata-se de um objeto da classe enum 'Revenue' contendo suas constantes.
     * Utilizou-se a anotação '@Enumerated' para que o campo seja persistido como uma 'string'.
     * Não aceita entrada nula, propondo uma mensagem para validação desconforme.
     */
    @Column(name = "tp_receita")
    @NotNull(message = "Tipo de receita não pode ser nula.")
    @Enumerated(EnumType.STRING) //grava a string e não a posição
        private Revenue typeOfRevenue;

    /**
     * Classe enum 'Revenue', contendo suas constantes imutáveis do tipo 'final'.
     * Enum tipo de despesa (está a gravar uma 'string', mas pode ser gravada sua posição ordinal)
     */
    @Getter
    public enum Revenue {
        SALARIO,
        PRESENTE,
        PREMIO,
        OUTROS
    }

    /**
     * Classe interna utilizada na API para delimitar um intervalo de datas.
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class DateRange {
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate start;
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate end;
    }

}


