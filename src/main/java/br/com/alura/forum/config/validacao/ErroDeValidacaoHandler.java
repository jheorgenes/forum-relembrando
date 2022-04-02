package br.com.alura.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //Interceptador de Erros (Controller Advice)
public class ErroDeValidacaoHandler { //Essa classe fará o tratamento de erros de Validação
	
	@Autowired
	private MessageSource messageSource; //Injetando a classe para pegar mensagens de erro
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST) //Definindo o retorno (status code 400) obrigatório ao chamar esse método 
	@ExceptionHandler(MethodArgumentNotValidException.class) //Annotation diz ao spring para chamá-lo quando houver exception na aplicação (do tipo de exception definida no parâmetro)
	public List<ErrorDeFormularioDto> handle(MethodArgumentNotValidException exception) { //Método que fará o tratamento do erro
		List<ErrorDeFormularioDto> dto = new ArrayList<ErrorDeFormularioDto>();
		
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors(); //Método para pegar o resultado das validações e pegar a lista de campos com erro
		
		fieldErrors.forEach(e -> {
			String mensagem = messageSource.getMessage(e,  LocaleContextHolder.getLocale()); //getMessage recebe o erro e a classe que busca o idioma conforme localidade
			ErrorDeFormularioDto erro = new ErrorDeFormularioDto(e.getField(), mensagem);
			dto.add(erro); //Guardando o erro formado na lista de erros de formulário
		});
		
		return dto; //retornando os erros de formulário coletados
	}
}
