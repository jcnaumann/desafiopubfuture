package br.dev.pubfuture.desafio;

import br.dev.pubfuture.desafio.businesslayer.service.ContaService;
import br.dev.pubfuture.desafio.presentation.ContaController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A anotação @SpringBootTest que diz ao Spring Boot para procurar a classe de configuração principal
 * (aquela anotada com @SpringBootApplication) e usá-la para iniciar um contexto de aplicativo Spring.
 */
@SpringBootTest
class DesafioApplicationTests {

	@Autowired
	private ContaController contaController;

	@Autowired
	private ContaService contaService;

	/**
	 * Teste se o contexto do aplicativo é carregado corretamente.
	 * Verifica se o aplicativo é capaz de carregar o contexto Spring com sucesso ou não.
	 * @throws Exception
	 */
	@Test
	public void contextLoads() throws Exception {
		Assertions.assertThat(contaController).isNotNull();
		Assertions.assertThat(contaService).isNotNull();
	}

}
