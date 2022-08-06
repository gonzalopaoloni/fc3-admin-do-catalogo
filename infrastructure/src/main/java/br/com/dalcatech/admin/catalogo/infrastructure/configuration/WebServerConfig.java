package br.com.dalcatech.admin.catalogo.infrastructure.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration //para enteder que eh uma classe de configuracao
@ComponentScan("br.com.dalcatech.admin.catalogo") //vai dizer para o spring qual eh o package padrao que ele tem que vai precisar examinar e barrer classes por classes para gerar os bins
public class WebServerConfig
    {
    }
