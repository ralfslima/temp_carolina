package com.br.apispringbbootjava.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.apispringbbootjava.model.EnderecoModel;
import com.br.apispringbbootjava.service.EnderecoService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class EnderecoController {

    private final EnderecoService enderecoService;

    @GetMapping("/endereco/{cep}")
    public ResponseEntity<EnderecoModel> consultarCep(@PathVariable String cep) {
        EnderecoModel endereco = enderecoService.obterEnderecoPorCep(cep);

        if (endereco == null || endereco.getCep() == null) {
            return ResponseEntity.notFound().build(); // retorna 404 se CEP não encontrado
        }

        return ResponseEntity.ok(endereco); // retorna 200 e o endereço
    }
}
