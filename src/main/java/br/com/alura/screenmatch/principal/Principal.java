package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    //scanner para captar a resposta
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=534784da";



    //metodo para exibir o menu
    public void exibeMenu(){
        System.out.println("Digite o nome da serie para busca: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ","+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json,DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> listaTemporada = new ArrayList<>();
		for (int i = 1; i <= dados.totalTemporadas();i++){
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ","+")+"&Season="+ i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json,DadosTemporada.class);
			listaTemporada.add(dadosTemporada);
		}
		listaTemporada.forEach(System.out::println);

        for(int i=0;i < dados.totalTemporadas(); i++){
            List<DadosEpisodio> episodiosTemporada = listaTemporada.get(i).episodios();
            for (int j=0; j <episodiosTemporada.size();j++){
                System.out.println(episodiosTemporada.get(j).titulo());
            }
        }
        System.out.println("Lista com lambdas");
        listaTemporada.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //stream
//        System.out.println("---- Stream ----");
//        List<String> nomes = Arrays.asList("Jaque","Nico","Iasmin","Paulo","Rodrigo");
//        nomes.stream()
//                .sorted()
//                .limit(3)
//                .filter(n -> n.startsWith("N"))
//                .map(n -> n.toUpperCase())
//                .forEach(System.out::println);
        List<DadosEpisodio> dadosEpisodios = listaTemporada.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 10 episódios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A "))
                //peek operação intermediária que permite ver o retorno da linha acima
                //.peek(e -> System.out.println("Primeiro Filtro(N/A) " + e))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                //.peek(e -> System.out.println("Ordenação " + e))
                .limit(10)
                //.peek(e -> System.out.println("Limite: " + e))
                .map(e -> e.titulo().toUpperCase())
                //.peek((e -> System.out.println("Mapeamento " + e)))
                .forEach(System.out::println);

//        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5);
//
//        int soma = numeros.stream()
//                .peek(n -> System.out.println("Elemento: " + n))
//                .map(n -> n * 2)
//                .peek(n -> System.out.println("Conteúdo depois do map: " + n))
//                .reduce(0, (total, numero) -> total + numero);
//
//        System.out.println("A soma dos números é: " + soma);

        List<Episodio> episodios = listaTemporada.stream()
                .flatMap(t -> t.episodios().stream()
                .map(d -> new Episodio(t.numero(),d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        //criando um find first
        System.out.println("Que titulo você está procurando? ");
        var trechoTitulo = leitura.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if (episodioBuscado.isPresent()){
            System.out.println("Episodio encontrado!");
            System.out.println("Episodio: " + episodioBuscado.get().getTitulo());
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
        }else{
            System.out.println("Não pegamos a sua referência!");
        }

        System.out.println("Selecione um ano para listar os episódios: ");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano,1,1);

        //criar um formatador de data
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        //stream com buscar personalizada

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episodio: " + e.getTitulo() +
                                " Data de Lançamento: " + e.getDataLancamento().format(dtf)
                ));

//        Map<Integer,Double> avaliacoesPorTemporada = episodios.stream()
//                .filter(e -> e.getAvaliacao() > 0.0)
//                .collect(Collectors.groupingBy(Episodio::getTemporada,
//                        Collectors.averagingDouble(Episodio::getAvaliacao)));
//        System.out.println("Avaliações por temporada: ");
//        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor Nota: " + est.getMax());
        System.out.println("Pior Nota: " + est.getMin());
        System.out.println("Total de episodios: " + est.getCount());
    }
}
