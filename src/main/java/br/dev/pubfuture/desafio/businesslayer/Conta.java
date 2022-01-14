package br.dev.pubfuture.desafio.businesslayer;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entidade JPA Conta, utilizada para definição dos membros da classe, representando os dados que serão persistidos
 * em uma tabela denominada 'conta' do banco de dados H2.
 * A fim de evitar código clichê, foi utilizada a biblioteca 'Project Lombok', a qual automatiza a criação dos
 * métodos 'Setters' e 'Getters', construtores, toString, builders, etc.
 *
 * @author Jean C. Naumann
 */
@Entity
@Table(name = "conta")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Conta {

    /**
     * Campo Id incrementado automaticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Campo validado 'balance' cujo nome da coluna na tabela do banco de dados será 'saldo'.
     * Não aceita entrada nula, propondo uma mensagem para validação desconforme.
     */
    @Column(name = "saldo")
    @NotNull(message = "Saldo não pode ser nulo.")
    private float balance;

    /**
     * Campo validado 'accountType' cujo nome da coluna na tabela do banco de dados será 'tp_conta'.
     * Trata-se de um objeto da classe enum 'TransferValue' contendo suas constantes.
     * Utilizou-se a anotação '@Enumerated' para que o campo seja persistido como uma 'string'.
     * Não aceita entrada nula, propondo uma mensagem para validação desconforme.
     */
    @Column(name = "tp_conta")
    @NotNull(message = "Tipo de conta não pode ser nulo.")
    @Enumerated(EnumType.STRING) //grava a string e não a posição
    private AccountType accountType;

    /**
     * Campo validado 'financialInstitution' cujo nome da coluna na tabela do banco de dados será 'inst_fin'.
     * Utilizou-se a anotação '@Size' para limitar o número de caracteres a 255.
     * Não aceita entrada em branco, propondo uma mensagem para validação desconforme.
     */
    @Column(name = "inst_fin")
    @NotBlank(message = "Instituição financeira não pode ficar em branco.")
    @Size(max = 255)
    private String financialInstitution;

    /**
     * Classe enum 'AccountType', contendo suas constantes imutáveis do tipo 'final'.
     * Enum tipo de conta (está a gravar uma 'string', mas pode ser gravada sua posição ordinal)
     */
    @Getter
    @AllArgsConstructor
    public enum AccountType {
        CARTEIRA,
        CONTA_CORRENTE,
        POUPANCA;
    }

    /**
     * Classe interna utilizada na API para delimitar a operação de transferência entre contas.
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class TransferValue {
        private long from;
        private long to;
        private float value;
    }

}
