package com.br.apispringbbootjava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType; // faltava esse import
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController; // faltava esse import
import org.springframework.web.multipart.MultipartFile;

import com.br.apispringbbootjava.model.ProdutoModel;
import com.br.apispringbbootjava.repository.ProdutoRepository;
import com.br.apispringbbootjava.service.UploadService;

@RestController
@RequestMapping("/api")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private UploadService uploadService;

    // Rota para cadastrar um produto
    @PostMapping(value = "/produtos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // corrigido: MULTIPART
    public ResponseEntity<ProdutoModel> cadastraProduto(
            @RequestParam String nome, 
            @RequestParam Double valor, 
            @RequestParam("imagem") MultipartFile imagemFile) { // corrigido: @RequestParam e não @ResquestPar
        
        try {
            String caminhoImagem = uploadService.armazenarArquivo(imagemFile);

            ProdutoModel produto = new ProdutoModel();
            produto.setNome(nome);
            produto.setValor(valor);
            produto.setImagem(caminhoImagem);

            ProdutoModel salvo = produtoRepository.save(produto);

            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/")
    public Iterable<ProdutoModel> listarTodosProdutos() {
        return produtoRepository.findAll();
    }
}
