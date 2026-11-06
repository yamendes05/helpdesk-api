
package com.example.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Trata erros de validação: HTTP 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(
            MethodArgumentNotValidException ex) {

        Map<String, String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        erro -> erro.getField(),
                        erro -> erro.getDefaultMessage(),
                        (primeiro, segundo) -> primeiro
                ));

        return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "erro", "Erro de validação",
                "mensagens", erros
        ));
    }

    // Trata ticket não encontrado: HTTP 404
    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<Map<String, Object>> tratarNaoEncontrado(
            TicketNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "status", 404,
                "erro", "Não encontrado",
                "mensagem", ex.getMessage()
        ));
    }

    // Trata JSON inválido: HTTP 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> tratarJsonInvalido(
            HttpMessageNotReadableException ex) {

        return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "erro", "Requisição inválida",
                "mensagem", "Verifique o formato do JSON e os valores enviados."
        ));
    }

    // Trata parâmetros incompatíveis com o tipo esperado: HTTP 400
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> tratarParametroInvalido(
            MethodArgumentTypeMismatchException ex) {

        return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "erro", "Requisição inválida",
                "mensagem", "O parâmetro '" + ex.getName()
                        + "' possui um valor ou formato inválido."
        ));
    }

    // Trata erros inesperados: HTTP 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> tratarErroInterno(
            Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", 500,
                "erro", "Erro interno do servidor",
                "mensagem", "Ocorreu um erro inesperado."
        ));
    }
}
