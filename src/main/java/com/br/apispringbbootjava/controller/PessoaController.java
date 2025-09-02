package com.br.apispringbbootjava.controller;

import com.br.apispringbbootjava.construtor.PessoaConstrutor;
import com.br.apispringbbootjava.dto.PessoaModeloDTO;
import com.br.apispringbbootjava.model.PessoaModel;
import com.br.apispringbbootjava.service.PessoaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Pessoa", description = "Endpoints para gerenciamento de pessoas")
@RestController
@RequestMapping("/api/pessoas")
@CrossOrigin(origins = "*")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private PessoaConstrutor pessoaConstrutor;

@GetMapping("/pessoas")
public CollectionModel<EntityModel<PessoaModel>> listar() {
    // Extrai o Iterable<PessoaModeloDTO> do ResponseEntity
    Iterable<PessoaModeloDTO> pessoasDTO = pessoaService.buscarTodasPessoas().getBody();

    // Usa o spliterator() diretamente no Iterable<PessoaModeloDTO>
    List<EntityModel<PessoaModel>> pessoas = StreamSupport.stream(pessoasDTO.spliterator(), false)
            .map(pessoaConstrutor::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(pessoas,
            linkTo(methodOn(PessoaController.class).listar()).withSelfRel());
}



    // ------------------- CREATE -------------------
    @Operation(summary = "Criar nova pessoa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pessoa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<PessoaModel> criarPessoa(
            @RequestBody(description = "Objeto de pessoa a ser criado", required = true)
            @Valid @org.springframework.web.bind.annotation.RequestBody PessoaModel pessoaModel) {
        return pessoaService.cadastrarPessoa(pessoaModel);
    }

    // ------------------- READ ALL -------------------
    @Operation(summary = "Listar todas as pessoas")
    @GetMapping
    public ResponseEntity<Iterable<PessoaModeloDTO>> listarTodasPessoas() {
        return pessoaService.buscarTodasPessoas();
    }

    // ------------------- READ BY ID -------------------
    @Operation(summary = "Buscar pessoa por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pessoa encontrada"),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada")
            })
    @GetMapping("/{id}")
    public ResponseEntity<PessoaModel> buscarPessoaPorId(
            @Parameter(name = "id", description = "ID da pessoa", required = true)
            @PathVariable Long id) {
        return pessoaService.buscaPessoaPorId(id);
    }

    // ------------------- UPDATE -------------------
    @Operation(summary = "Atualizar pessoa existente")
    @PutMapping("/{id}")
    public ResponseEntity<PessoaModel> atualizarPessoa(
            @Parameter(name = "id", description = "ID da pessoa a ser atualizada", required = true)
            @PathVariable Long id,
            @RequestBody(description = "Objeto de pessoa com dados atualizados", required = true)
            @Valid @org.springframework.web.bind.annotation.RequestBody PessoaModel pessoaModel) {

        pessoaModel.setId(id);
        return pessoaService.atualizarPessoa(pessoaModel);
    }

    // ------------------- PATCH -------------------
    @Operation(summary = "Atualização parcial de pessoa")
    @PatchMapping("/{id}")
    public ResponseEntity<PessoaModel> atualizacaoParcialPessoa(
            @Parameter(name = "id", description = "ID da pessoa a ser atualizada parcialmente", required = true)
            @PathVariable Long id,
            @RequestBody(description = "Objeto com campos a serem atualizados")
            @org.springframework.web.bind.annotation.RequestBody PessoaModel pessoaModel) {

        return pessoaService.atualizacaoParcial(id, pessoaModel);
    }

    // ------------------- DELETE -------------------
    @Operation(summary = "Deletar pessoa por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPessoa(
            @Parameter(name = "id", description = "ID da pessoa a ser deletada", required = true)
            @PathVariable Long id) {

        return pessoaService.deletarPessoa(id);
    }

    // ------------------- SECURE ENDPOINT EXAMPLE -------------------
    @Operation(summary = "Exemplo de endpoint seguro", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/protegido")
    public ResponseEntity<String> endpointSeguro() {
        return ResponseEntity.ok("Acesso permitido com token!");
    }
}
