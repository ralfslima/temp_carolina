package com.br.apispringbbootjava.construtor;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*; // linkTo e methodOn

import com.br.apispringbbootjava.controller.PessoaController;
import com.br.apispringbbootjava.dto.PessoaModeloDTO;
import com.br.apispringbbootjava.model.PessoaModel;

@Component
public class PessoaConstrutor implements RepresentationModelAssembler<PessoaModel, EntityModel<PessoaModel>> {

    @Override
    public EntityModel<PessoaModel> toModel(PessoaModel pessoaModel) {
        return EntityModel.of(
            pessoaModel,
            linkTo(methodOn(PessoaController.class).listarTodasPessoas()).withRel("listar"),
            linkTo(methodOn(PessoaController.class).buscarPessoaPorId(pessoaModel.getId())).withSelfRel(),
            linkTo(methodOn(PessoaController.class).atualizarPessoa(pessoaModel.getId(), null)).withRel("atualizar"),
            linkTo(methodOn(PessoaController.class).deletarPessoa(pessoaModel.getId())).withRel("excluir")
        );
    }
    
    // Método criado para que PessoaController consiga executar corretamente
    public EntityModel<PessoaModel> toModel(PessoaModeloDTO pessoaDTO) {
        // Mapeia o DTO para o modelo real
        PessoaModel pessoaModel = new PessoaModel(
            pessoaDTO.getNome(),
            pessoaDTO.getIdade(),
            pessoaDTO.getEmail(),
            pessoaDTO.getCidade()
        );
        
        // Agora, retorna o EntityModel com links
        return toModel(pessoaModel); // Usa o método toModel que já está implementado
    }

}
