package br.com.alura.exercicio;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class main {
    public static void main(String[] args) {
        List<Integer> numeros = Arrays.asList(10,20,30,40,50);
        Optional<Integer> maior = numeros.stream()
                .sorted(Comparator.reverseOrder())
                .findFirst();
        System.out.println("Exercicio 1");
        System.out.println("O maior numero é: " +maior.get().intValue());
        System.out.println("Exercicio 2 - X -");
        List<String> palavras = Arrays.asList("java","stream","lambda","code");
        //Map - Integer para lenght - List de palavras agrupadas pelo lenght
        Map<Integer, List<String>> agrupamento = palavras.stream()
                .collect(Collectors.groupingBy(String::length));
        System.out.println(agrupamento);
        System.out.println("Exercicio 3");
        List<String> nomes = Arrays.asList("Alice","Bob","Charlie");
        nomes.stream()
                .collect(Collectors.joining(", "));
        System.out.println(nomes);
        System.out.println("Exercício 4");
        List<Integer> numeros3 = Arrays.asList(1,2,3,4,5,6);
        int soma = numeros3.stream()
                .filter(n -> n %2 == 0)
                .map(n -> n * n)
                .reduce(0,Integer::sum);
        System.out.println("Soma dos quadrados: " +soma);





    }
}
