# 📌 Instruções de Alterações no Projeto - Spring Boot  

Olá **Carolina**,  

Este repositório contém o projeto base, mas você deve realizar algumas alterações importantes para garantir a compatibilidade com o **Spring Boot 3.X.X** e ajustar a implementação do HATEOAS.  

Abaixo seguem as mudanças necessárias:  

---

## ✅ Alterações no Projeto  

### 1. `PessoaConstrutor.java`  

📍 Localização: `com.br.apispringbbootjava.construtor`  

Crie ou edite a classe **PessoaConstrutor** conforme abaixo:  

```java
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
        return toModel(pessoaModel); 
    }
}
```

---

### 2. `PessoaController.java`  

No método **listar()**, faça a alteração para usar o `spliterator()` corretamente:  

```java
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
```

---

### 3. `EnderecoConfiguracao.java`  

Ajuste o **RestTemplate** removendo o `HttpComponentsClientHttpRequestFactory`, que não é mais compatível com o Spring 3.X.X:  

```java
@Bean
public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate;
}
```

---

## 🚀 Conclusão  

Essas alterações garantem que:  

- O HATEOAS funcione corretamente com DTOs e Models.  
- O método `listar()` em `PessoaController` esteja adequado ao uso do `StreamSupport`.  
- O `RestTemplate` seja compatível com o **Spring Boot 3.X.X**.  

Depois de aplicar essas modificações, basta rodar o projeto novamente. 🎉  

---

👉 Carolina, recomendo você **commitar cada alteração separadamente**, para ficar mais fácil revisar no histórico do GitHub.  
