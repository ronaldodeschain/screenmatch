package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumiApi = new ConsumoApi();
		var json = consumiApi.obterDados("https://www.omdbapi.com/?t=six+feet+under&apikey=534784da");
//		System.out.println(json);
//		json = consumiApi.obterDados("https://coffee.alexflipnote.dev/random.json");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json,DadosSerie.class);
		System.out.println(dados);
		DadosEpisodio dadosEpisodio = conversor.obterDados(json,DadosEpisodio.class);
		System.out.println(dadosEpisodio);

		List<DadosTemporada> listaTemporada = new ArrayList<>();
		for (int i = 1; i <= dados.totalTemporadas();i++){
			json = consumiApi.obterDados("https://www.omdbapi.com/?t=six+feet+under&season=" + i + "&apikey=534784da");
//			json =consumiApi.obterDados("https://www.omdbapi.com/?t=six+feet+under&season=1&apikey=534784da");
			DadosTemporada dadosTemporada = conversor.obterDados(json,DadosTemporada.class);
			listaTemporada.add(dadosTemporada);
		}
		listaTemporada.forEach(System.out::println);


	}


}
