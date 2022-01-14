#DESAFIO DE PROGRAMAÇÃO - PubFuture

*Trata-se da terceira etapa do processo seletivo do programa PubFuture, com a finalidade de testar conhecimento técnico, 
onde serão avaliadas habilidades em programação e noções de banco de dados.*

Participante:

**Jean C. Naumann**

jcnaumann@gmail.com

14/01/2022 - 47-999187074 (cel.)

##Desafio

Implementar ma solução que auxilie no controle e gerenciamento de finanças pessoais. O foco é testar conhecimentos em back-end.

###O sistema deve é capaz de atender aos seguintes requisitos:

CRUD para as entidades Receitas, Despesas e Contas.

Especificamente para as entidades Receitas e Despesas, duas consultas foram implementadas, a saber: Filtro por período e por tipo de receita/despesa.

Acrescentou-se ainda consultas por Id e listagem total de registros, além dos saldos totais das entidades Receitas e Despesas.

Por último, para a entidade Contas, implementou-se uma funcionalidade de transferência de saldo entre contas.

Os campos pertendentes a cada entidade, foram rigorosamente seguidos conforme orientação da atividade/desafio.

##Avaliação

Todos os requisitos obrigatórios, assim como os requisitos desejáveis foram cumpridos e estão cada qual implementado na estrutura que seque.

##Informações 

- O banco de dados reacional utilizado foi o H2;
- A documentação foi gerada como comentários decorados nas classes, métodos e campos;
- A implementação foi compartilhada via GitHub;
- Foram implementados diferentes Testes Unitários e Testes de Integração para os _endpoint_;
- Todo o material foi desenvolvido resguardando às prerrogativas da Programação Orientada a Objetos;
- O _design pattern_ utilizado foi o MVC (Model-View-Controller);
- A solução foi desenvolvida no formato de umas RESTFul API.

##Outras Informações

- A linguagem de programação utilizada foi Java:
- Openjdk 17.0.1 2021-10-19 LTS
  OpenJDK Runtime Environment Corretto-17.0.1.12.1 (build 17.0.1+12-LTS)
  OpenJDK 64-Bit Server VM Corretto-17.0.1.12.1 (build 17.0.1+12-LTS, mixed mode, sharing)
- IDE IntelliJ IDEA 2021.3 (Community Edition)
  Build #IC-213.5744.223, built on November 27, 2021
  Runtime version: 11.0.13+7-b1751.19 amd64
  VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
  Windows 10 10.0
  GC: G1 Young Generation, G1 Old Generation
  Memory: 2032M
  Cores: 4
  Registry:
  scala.erase.compiler.process.jdk.once=false
- Data Base H2;
- Spring Boot: org.springframework.boot - version 2.6.2 
- Ainda: spring-boot-starter-data-jdbc, spring-boot-starter-web, spring-boot-starter-data-jpa, spring-boot-starter-validation.
- org.projectlombok:lombok
- Test JUnit 5;
- Gradle 6.9.1 - Build time:   2021-08-20 11:15:18 UTC
  Kotlin: 1.4.20, Groovy: 2.5.12, Ant: Apache Ant(TM) version 1.10.9 compiled on September 27 2020, JVM: 17.0.1 (Amazon.com Inc. 17.0.1+12-LTS), OS: Windows 10 10.0 amd64;
- Version: Insomnia Designer 2020.5.2
  Release date: 09/12/2020
  OS: Windows_NT x64 10.0.19042
  Electron: 9.1.1
  Node: 12.14.1
  V8: 8.3.110.13-electron.0
  Architecture: x64
  node-libcurl: libcurl/7.69.1-DEV OpenSSL/1.1.1d zlib/1.2.11 WinIDN libssh2/1.9.0_DEV nghttp2/1.40.0

####Para executar rapidamente, basta abrir e executar o projeto no IntelliJ ou executar o comando _gradle run_ diretamente na linha de comando.

###Para testar a API RESTFul, recomenda-se o Insomnia.

####Os objetos JSON utilizados nos testes estão listados a seguir.
##
####ENTIDADE CONTA

_Cadastrar_

Post
http://localhost:8881/api/conta/create
{
"balance": 1.0,
"accountType": "CONTA_CORRENTE",
"financialInstitution": "Safra SA"
}

_Editar_
Put
http://localhost:8881/api/conta/{id}
{
"id": 3,
"balance": 100.0,
"accountType": "CARTEIRA",
"financialInstitution": "Foda"
}

_Remover_
Del
http://localhost:8881/api/conta/{id}

_Listar id_
Get
http://localhost:8881/api/conta/{id}

_Listar todas_
Get
http://localhost:8881/api/conta

_Transferência_
Put
http://localhost:8881/api/conta/transfer
{
"from": 1,
"to": 2,
"value": 1
}

_Saldo total_
Get
http://localhost:8881/api/conta/total
##

####ENTIDADE DESPESA

_Cadastrar_
Post
http://localhost:8881/api/despesa/create
{
"value": 1.0,
"payday": "12-01-2018",
"expectedPaymentDate": "12-01-2018",
"typeOfExpense": "MORADIA",
"account": 100
}

_Editar_
Put
http://localhost:8881/api/despesa/{id}
{
"id": 3,
"value": 100.0,
"payday": "01-01-2022",
"expectedPaymentDate": "01-01-2022",
"typeOfExpense": "LAZER",
"account": 500
}

_Remover_
Del
http://localhost:8881/api/despesa/{id}

_Listar id_
Get
http://localhost:8881/api/despesa/{id}

_Listar todas_
Get
http://localhost:8881/api/despesa/all

_Listar por intervalo de datas_
Get
http://localhost:8881/api/despesa
{
"start": "01-01-2020",
"end": "01-12-2022"
}

_Listar por tipo de despesa_
Get
http://localhost:8881/api/despesa/tp/MORADIA

_Total de despesas_
Get
http://localhost:8881/api/despesa/total
##
####ENTIDADE RECEITA

_Cadastrar_
Post
http://localhost:8881/api/receita/create
{
"value": 1.0,
"receivingDate": "12-02-2020",
"expectedReceiptDate": "12-02-2020",
"description": "LAZER",
"account": 100,
"typeOfRevenue": "PRESENTE"
}

_Editar_
Put
http://localhost:8881/api/receita/{id}
{
"id": 2,
"value": 999.0,
"receivingDate": "01-01-2022",
"expectedReceiptDate": "01-01-2021",
"description": "PRESENTE",
"account": 500,
"typeOfRevenue": "PRESENTE"
}

_Remover_
Delete
http://localhost:8881/api/receita/{id}

_Listar id_
Get
http://localhost:8881/api/receita/{id}

_Listar todas_
Get
http://localhost:8881/api/receita/all

_Listar por intervalo de datas_
Get
http://localhost:8881/api/receita
{
"start": "01-01-2020",
"end": "01-12-2022"
}

_Listar por tipo de receita_
Get
http://localhost:8881/api/receita/tp/MORADIA

_Total de despesas_
Get
http://localhost:8881/api/receita/total