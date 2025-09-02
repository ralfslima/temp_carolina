package com.br.apispringbbootjava.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.br.apispringbbootjava.model.EnderecoModel;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EnderecoService {

    private final RestTemplate restTemplate; // injeção via construtor, sem @Autowired

    // Método responsável por realizar uma requisição para uma API externa
    public EnderecoModel obterEnderecoPorCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        return restTemplate.getForObject(url, EnderecoModel.class);
    }
}
