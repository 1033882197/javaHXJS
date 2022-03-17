package com.demoproject.stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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

        //stream 的初始化
        Stream<String> song = Stream.of("gently", "down", "the", "stream");
        show("song", song) ;
        Stream<String> silence = Stream.empty();
        show("silence", silence);

        Stream<String> echos = Stream.generate(() -> "Echo");
        show("echo", echos);

        Stream<Double> randoms = Stream.generate(Math::random).limit(10);
        show("randoms", randoms);

        //iterate的使用
        Stream<BigInteger> integers = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE));
        show("integers", integers);

        //将字符串转化为小写，peek可以断点
        List<String> words = Arrays.asList("QBCD", "QaSdc", "cewqr");
        Stream<String> lowercaseWords = words.stream().map(String::toLowerCase).peek(x->{
            return;}
        );

        show("lowercaseWords", lowercaseWords);

        //截取每个字符传的第一个字母
        Stream<String> firstLetters = words.stream().map(s -> s.substring(0, 1));
        show("firstLetters",firstLetters);

        Stream<Stream<String>> result = words.stream().map(w -> codePoints(w));
        show("stream in stream", result);

        //使用flatMap将Stream<Stream<String>>平铺
        Stream<String> flatResult = words.stream().flatMap(w -> codePoints(w));
        show("stream flatMap", flatResult);

        //使用concat拼接两个stream
        Stream<String> combined = Stream.concat(codePoints("hello"), codePoints("world"));
        show("concat", combined);

        //sort自定义排序
        List<Student> studentList = new ArrayList<>(3);
        studentList.add(new Student("小马", 10));
        studentList.add(new Student("小李", 15));
        studentList.add(new Student("小明",20));
        Stream<Student> studentSort = studentList.stream().sorted(Comparator.comparing(Student :: getAge).reversed());
        show("custom sort", studentSort);

        //去重复
        Stream<String> uniqueWords = Stream.of("Ray","Ray","Ma").distinct();
        //only one "Ray" is retained

        Stream<String> longestFirst = words.stream().sorted(Comparator.comparing(String::length).reversed());

        //获取最大的一个字符串
        Optional<String> largest = words.stream().max(String ::compareToIgnoreCase);
        //获取以Q开头的第一个元素
        Optional<String> startWithQ = words.stream().filter(s -> s.startsWith("Q")).findAny();
        //该流是否包含以Q开头的元素，使用并行流
        boolean aWordStartWithQ = words.stream().parallel().anyMatch(s -> s.startsWith("Q"));

        //optional 如果为null返回""
        String getOptional = largest.orElse("");
        //如果为null返回一个配置中的值
        getOptional = largest.orElseGet(() -> System.getProperty("xxx.xxx"));
        //如果为空返回一个异常
        getOptional = largest.orElseThrow(IllegalStateException :: new);

        List<String> getOptionalResult = new ArrayList<>();
        //如果存在把该元素传递给一个方法
        largest.ifPresent(getOptionalResult :: add);

        //如果largest为空 则transform 也为空
        Optional<String> transform = largest.map(String :: toLowerCase);
        //如果largest为空 则什么也不会发生
        largest.map(getOptionalResult :: add);
        //optional中长度大于5的元素转小写
        transform.filter(s -> s.length() > 5).map(String::toLowerCase);

        //flatMap，将CreatingStream::squareRoot 应用于当前Option值
        System.out.println(inverse(4.0).flatMap(CreatingStream::squareRoot));

        //public<U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper)
        //产生将mapper应用于当前Optional值所产生的结果，或者在当前Optional为空时，返回一个空Optional
        Optional.of(4.0).flatMap(CreatingStream::inverse).flatMap(CreatingStream::squareRoot);

        Stream<String> stream = Stream.of("123","456");
        //stream转化为指定类型的数组
        String[] streamToArray = stream.toArray(String[] :: new);
        //List<String> streamToList = stream.collect(Collectors.toList());
        //Set<String> streamToSetDefault = stream.collect(Collectors.toSet());
        //TreeSet<String> streamToTreeSet = stream.collect(Collectors.toCollection(TreeSet::new));
        //String streamToJoinString = stream.collect(Collectors.joining());

        //IntSummaryStatistics summary = stream.collect(Collectors.summarizingInt(String :: length));
        //double averageWordLength = summary.getAverage();
        //double maxWordLength = summary.getMax();

        List<Student> studentList1 = Arrays.asList(new Student("小花", 10), new Student("小红",15));
        //name为key，对象本身为value
        Map<String, Student> nameToStudent = studentList1.stream().collect(Collectors.toMap(Student::getName, Function.identity()));

        Map<String, Student> nameToStudentTreeMap = studentList1.stream().collect(
                Collectors.toMap(
                        Student::getName,
                        Function.identity(),
                        (existingValue, newValue) -> {throw new IllegalStateException();},
                        TreeMap :: new
                        )
        );

        Stream<Locale> localeStream = Stream.of(Locale.getAvailableLocales());
        //获取所有语言，locale中的名字为key,其本地化名字为value，如果key冲突只记录第一个
        Map<String, String> languageNames = localeStream.collect(Collectors.toMap(
                Locale :: getDisplayLanguage,
                loc -> loc.getDisplayLanguage(loc),
                (existingValue, newValue) -> existingValue
        ));

        localeStream = Stream.of(Locale.getAvailableLocales());
        //获取不同国家的所有语言
        Map<String, Set<String>> countryLanguageSets = localeStream.collect(
                Collectors.toMap(
                        Locale::getDisplayCountry,
                        l -> Collections.singleton(l.getDisplayLanguage()),
                        (a, b) ->{
                            HashSet<String> union = new HashSet<>(a);
                            union.addAll(b);
                            return union;
                        }
                )
        );

        localeStream = Stream.of(Locale.getAvailableLocales());
        //groupBy分组
        Map<String, List<Locale>> countryToLocales = localeStream.collect(Collectors.groupingBy(Locale :: getCountry));

        localeStream = Stream.of(Locale.getAvailableLocales());
        //使用partitioningBy 获取说英文的和不说英文的两类
        Map<Boolean, List<Locale>> englishAndOtherLocales = localeStream.collect(Collectors.partitioningBy(
                l -> l.getLanguage().equals("en")));

        localeStream = Stream.of(Locale.getAvailableLocales());
        //每个国家有多少个locale进行计数
        Map<String, Long> countryToLocaleCounts = localeStream.collect(Collectors.groupingBy(Locale :: getCountry, Collectors.counting()));

        List<City> cities = Arrays.asList(new City("西安", "状态1", 10),
                new City("咸阳", "状态2", 15),new City("汉中", "状态1", 10));
        //根据状态分组，计算每组的总人数
        Map<String, Integer> stateToCityPopulation = cities.stream().collect(Collectors.groupingBy(City::getState, Collectors.summingInt(City::getPopulation)));

        //根据状态分组，然后找出每组名字最长的
        Map<String, Optional<String>> stateToLongestCityName = cities.stream().collect(Collectors.groupingBy(
                City::getState, Collectors.mapping(
                        City::getName, Collectors.maxBy(
                                Comparator.comparing(String :: length)))));

        localeStream = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<String>> countryToLanguages = localeStream.collect(Collectors.groupingBy(
                Locale::getDisplayCountry, Collectors.mapping(Locale :: getDisplayLanguage, Collectors.toSet())
        ));

        Map<String, IntSummaryStatistics> stateToCityPopulationSummary = cities.stream().collect(Collectors.groupingBy(City::getState,Collectors.summarizingInt(City::getPopulation)));

        Map<String, String> stateToCityNames = cities.stream().collect(Collectors.groupingBy(
                City::getState,Collectors.reducing("11111", City :: getName, (s,t) -> s.length() == 0 ? t : s +", " + t)));

        stateToCityNames = cities.stream().collect(Collectors.groupingBy(City::getState, Collectors.mapping(City::getName, Collectors.joining(", "))));

        //基本类型流
        Random random = new Random();
        IntStream is1 = IntStream.generate(() ->random.nextInt(100)).limit(10);

        is1 = random.ints(10,0,100);
        show("is1", is1);
        IntStream is2 = IntStream.range(5,10);
        show("is2", is2);
        IntStream is3 = IntStream.rangeClosed(5, 10);
        show("is3", is3);

        List<String> wordList = Arrays.asList("asdqwe","asdczxsadd","asdasd");
        IntStream is4 = wordList.stream().mapToInt(String :: length);
        show("is4",is4);

        String sentence = "\uD835\uDD46 is the set of octonions.";
        IntStream codes = sentence.codePoints();
        System.out.println(codes.mapToObj(c -> String.format("%X ", c)).collect(Collectors.joining()));

        Stream<Integer> streamIntegers = IntStream.range(0, 100).boxed();
        IntStream is5 = streamIntegers.mapToInt(Integer :: intValue);
        show("is5", is5);



    }

    public static Optional<Double> inverse(Double x) {
        return x == 0 ? Optional.empty() : Optional.of(1 / x);
    }

    public static Optional<Double> squareRoot(Double x) {
        return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
    }

    public static Stream<String> codePoints(String s){
        List<String> result = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            int j = s.offsetByCodePoints(i, 1);
            result.add(s.substring(i, j));
            i = j;
        }
        return result.stream();
    }

    public static void show(String title, IntStream stream){
        final int SIZE = 10;
        int[] firstElements = stream.limit(SIZE + 1).toArray();
        System.out.print(title + ": ");
        for(int i = 0; i < firstElements.length; i++){
            if(i > 0) System.out.print(", ");
            if(i < SIZE) System.out.print(firstElements[i]);
            else System.out.print("...");
        }
        System.out.println();
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

    @AllArgsConstructor
    @Getter
    @Setter
    public static class City{
        private String name;
        private String state;
        private int population;
    }

}
