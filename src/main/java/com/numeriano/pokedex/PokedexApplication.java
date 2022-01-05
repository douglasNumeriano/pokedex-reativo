package com.numeriano.pokedex;

import com.numeriano.pokedex.model.Pokemon;
import com.numeriano.pokedex.repository.PokemonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;


@SpringBootApplication
@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class PokedexApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokedexApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ReactiveMongoOperations operations,
						   PokemonRepository repository){
		return args -> {
			Flux<Pokemon> pokemonFlux = Flux.just(
							new Pokemon(null, "Bulbassauro", "Semente", "OverGrow", 6.09),
							new Pokemon(null, "Charizard", "Fogo", "Blaze", 90.05),
							new Pokemon(null, "Caterpie", "Minhoca", "Poeira do Escudo", 2.09),
							new Pokemon(null, "Blastoise", "Marisco", "Torrente", 6.09))
					.flatMap(repository::save);

//			Flux.just(): quando eu quero trabalhar com vários fluxos
//			flatMap(): vai executar toda ação que estiver dentro dele pra todos os registros

			pokemonFlux
					.thenMany(repository.findAll())
					.subscribe(System.out::println);

//			thenMany: usado para chamar um função que estiver dentro dele
//			subscribe: é uma função de printar conteúdo na tela em modo reativo
		};
	}

}
