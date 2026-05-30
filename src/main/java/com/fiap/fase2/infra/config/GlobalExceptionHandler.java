package com.fiap.fase2.infra.config;

import com.fiap.fase2.domain.exception.BusinessException;
import com.fiap.fase2.domain.exception.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final URI TYPE_NOT_FOUND = URI.create("https://api.fiap.com/errors/not-found");
    private static final URI TYPE_CONFLICT = URI.create("https://api.fiap.com/errors/conflict");
    private static final URI TYPE_VALIDATION = URI.create("https://api.fiap.com/errors/validation");
    private static final URI TYPE_BAD_REQUEST = URI.create("https://api.fiap.com/errors/bad-request");
    private static final URI TYPE_BUSINESS = URI.create("https://api.fiap.com/errors/business");

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        log.warn("Entidade não encontrada: {} | Path: {}", ex.getMessage(), request.getRequestURI());
        return buildProblem(HttpStatus.NOT_FOUND, ex.getMessage(), "Recurso não encontrado", TYPE_NOT_FOUND, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusiness(BusinessException ex, HttpServletRequest request) {
        log.warn("Erro de negócio: {} | Path: {}", ex.getMessage(), request.getRequestURI());
        return buildProblem(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), "Erro de negócio", TYPE_BUSINESS, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage()));

        log.warn("Validação falhou: {} | Path: {}", fieldErrors, request.getRequestURI());

        ProblemDetail problem = buildProblem(HttpStatus.BAD_REQUEST, "Erro de validação", "Dados inválidos", TYPE_VALIDATION, request);
        problem.setProperty("campos", fieldErrors);
        return problem;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.error("Violação de integridade: {} | Path: {}", ex.getMostSpecificCause().getMessage(), request.getRequestURI());
        return buildProblem(HttpStatus.CONFLICT, "Violação de integridade de dados", "Conflito de dados", TYPE_CONFLICT, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Argumento inválido: {} | Path: {}", ex.getMessage(), request.getRequestURI());
        return buildProblem(HttpStatus.BAD_REQUEST, ex.getMessage(), "Requisição inválida", TYPE_BAD_REQUEST, request);
    }

    private ProblemDetail buildProblem(HttpStatus status, String detail, String title, URI type, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setType(type);
        problem.setInstance(URI.create(request.getRequestURI()));
        return problem;
    }
}
