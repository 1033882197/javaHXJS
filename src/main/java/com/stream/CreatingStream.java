package com.stream;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CreatingStream {
    public static void main(String[] args) throws IOException {
//        Path path = Paths.get("/src/main/resource/static/alice30.txt");
//        String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
//
//        Stream<String> words = Stream.of(content.split("\\PL+"));
//        show("words", words);
        Stream<String> song = Stream.of("gently", "down", "the", "stream");
        show("song", song) ;

        Stream<String> silence = Stream.empty();
        show("silence", silence);

        Stream<String> echos = Stream.generate(() -> "Echo");
        show("echo", echos);

        Stream<Double> randoms = Stream.generate(Math::random);
        show("randoms", randoms);

        Stream<BigInteger> integers = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE));
        show("integers", integers);

        List<String> words = Arrays.asList("ABCD", "baSdc", "cewqr");
        Stream<String> lowercaseWords = words.stream().map(String::toLowerCase).peek(x->{
            return;}
        );
        show("lowercaseWords", lowercaseWords);

        Stream<String> firstLetters = words.stream().map(s -> s.substring(0, 1));
        show("firstLetters",firstLetters);

        String a = "abcdefg";
        show("aaa", codePoints(a));

        Stream<Stream<String>> result = words.stream().map(w -> codePoints(w));
        show("stream in stream", result);

        Stream<String> flatResult = words.stream().flatMap(w -> codePoints(w));
        show("stream flatMap", flatResult);

        Stream<String> uniqueWords = Stream.of("Ray","Ray","Ma").distinct();
        //only one "Ray" is retained

        Stream<String> longestFirst = words.stream().sorted(Comparator.comparing(String::length).reversed());
        
        Optional<String> largest = words.stream().max(String ::compareToIgnoreCase);
        System.out.println(largest);



    }

    public static Stream<String> codePoints(String s){
        List<String> result = new ArrayList<>();
        int i = 0;
        while (i < s.length()){
            int j = s.offsetByCodePoints(i , 1);
            result.add(s.substring(i, j));
            i = j;
        }
        return result.stream();
    }

    public static <T> void show(String title, Stream<T> stream){
        final int SIZE = 10;
        List<T> firstElements = stream.limit(SIZE + 1).collect(Collectors.toList());
        System.out.print(title + ": ");
        for(int i = 0; i < firstElements.size(); i++){
            if(i > 0) System.out.print(", ");
            if(i < SIZE) System.out.print(firstElements.get(i));
            else System.out.print("...");
        }
        System.out.println();
    }

}
