package com.br.apispringbbootjava.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EnderecoConfiguracao {

    @Bean
public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    // exemplo: adicionar timeout
    // restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory() {{
    //     setConnectTimeout(5000);
    //     setReadTimeout(5000);
    // }});
    return restTemplate;
}

    
}
