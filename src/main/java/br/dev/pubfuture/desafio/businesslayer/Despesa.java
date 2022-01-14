package br.dev.pubfuture.desafio.businesslayer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Entidade JPA Despesa, utilizada para definição dos membros da classe, representando os dados que serão persistidos
 * em uma tabela denominada 'despesa' do banco de dados H2.
 * A fim de evitar código clichê, foi utilizada a biblioteca 'Project Lombok', a qual automatiza a criação dos
 * métodos 'Setters' e 'Getters', construtores, toString, builders, etc.
 *
 * @author Jean C. Naumann
 */
@Entity
@Table(name = "despesa")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Despesa {

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
     * Campo validado 'payday' cujo nome da coluna na tabela do banco de dados será 'dt_pagto'.
     * Utilizou-se a anotação '@JsonFormat' para configurar como as datas devem ser serializadas.
     * Não aceita entrada nula, propondo uma mensagem para validação desconforme.
     */
    @Column(name = "dt_pagto")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "Data de pagamento não pode ser nula.")
    private LocalDate payday;

    /**
     * Campo validado 'expectedPaymentDate' cujo nome da coluna na tabela do banco de dados será 'dt_pag_esp'.
     * Utilizou-se a anotação '@JsonFormat' para configurar como as datas devem ser serializadas.
     * Não aceita entrada nula, propondo uma mensagem para validação desconforme.
     */
    @Column(name = "dt_pag_esp")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "Data de pagamento esperado não pode ser nula.")
    private LocalDate expectedPaymentDate;

    /**
     * Campo validado 'typeOfExpense' cujo nome da coluna na tabela do banco de dados será 'tp_despesa'.
     * Trata-se de um objeto da classe enum 'TypeOfExpense' contendo suas constantes.
     * Utilizou-se a anotação '@Enumerated' para que o campo seja persistido como uma 'string'.
     * Não aceita entrada nula, propondo uma mensagem para validação desconforme.
     */
    @Column(name = "tp_despesa")
    @NotNull(message = "Tipo de despesa não pode ser nulo.")
    @Enumerated(EnumType.STRING) //grava a string e não a posição
    private TypeOfExpense typeOfExpense;

    /**
     * Campo validado 'account' cujo nome da coluna na tabela do banco de dados será 'despesa'.
     * Utilizou-se a anotação '@Min' e '@Max' para configurar limitar o intervalo númerico aceito.
     * Não aceita entrada nula, propondo uma mensagem para validação desconforme.
     */
    @Column(name = "conta")
    @NotNull(message = "Conta não pode ser nula.")
    @Min(1) @Max(999)
    private int account;

    /**
     * Classe enum 'TypeOfExpense', contendo suas constantes imutáveis do tipo 'final'.
     * Enum tipo de despesa (está a gravar uma 'string', mas pode ser gravada sua posição ordinal)
     */
    @Getter
    public enum TypeOfExpense {
        ALIMENTACAO,
        EDUCACAO,
        LAZER,
        MORADIA,
        ROUPA,
        SAUDE,
        TRANSPORTE,
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
