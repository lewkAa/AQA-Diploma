package ru.netology.data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.Year;
import java.util.Locale;
import java.util.Random;

public class DataHelper {

    private static final Faker faker = new Faker();


    public static Card genCard(String cardNum) {
        Card newCard = new Card();
        newCard.setNumber(cardNum);
        newCard.setHolder(genValidHolder());
        String validYear = DataHelper.genValidYear();
        newCard.setYear(validYear);
        newCard.setMonth(genValidMonth(validYear));
        newCard.setCvc(genValidCVC());
        return newCard;
    }

    public static String genRndCardNum() {
        return faker.number().digits(16).replaceAll("(.{4})", "$1 ").trim();
    }

    public static String genShortCardNum() {
        String shortCard = faker.finance().creditCard();
        shortCard = shortCard.replaceAll("-", " ");
        int rndLength = faker.number().numberBetween(1, 19);
        return shortCard.substring(0, rndLength);
    }

    public static String genLongCardNum() {
        int rndLength = faker.number().numberBetween(17, 25);
        String longCard = String.valueOf(faker.number().digits(rndLength));
        return longCard.replaceAll("(.{4})", "$1 ").trim();
    }


    public static String genValidYear() {
        int currentYear = Year.now().getValue() % 100;
        int maxFutureYear = currentYear + 5;
        int year = faker.number().numberBetween(currentYear, maxFutureYear + 1);
        return String.format("%02d", year);
    }

    public static String genOutdateYear() {
        int currentYear = Year.now().getValue() % 100;
        int maxBackYear = currentYear - 10;
        int year = faker.number().numberBetween(maxBackYear, currentYear);
        return String.format("%02d", year);
    }

    public static String getCurrYear() {
        int year = Year.now().getValue() % 100;
        return String.format("%02d", year);
    }


    public static String genValidMonth(String year) {
        int currentYear = Year.now().getValue() % 100;
        int currentMonth = LocalDate.now().getMonthValue();
        ;
        int givenYear = Integer.parseInt(year);
        int month;
        if (givenYear < currentYear) {
            throw new IllegalArgumentException("Year cannot be in the past");
        }
        if (givenYear > currentYear) {
            month = faker.number().numberBetween(1, 13);
        } else {
            month = faker.number().numberBetween(currentMonth, 13);
        }
        return String.format("%02d", month);
    }

    public static String genInvalidMonth() {
        int currentMonth = LocalDate.now().getMonthValue();
        int month;
        month = faker.number().numberBetween(1, currentMonth);
        return String.format("%02d", month);
    }

    public static String genValidHolder() {
        String name = faker.name().fullName().toUpperCase();
        name = name.replaceAll("[^A-Z ]", "")
                .replaceAll("\\s+", " ");
        return name.length() > 30 ? name.substring(0, 30) : name;
    }

    public static String genValidCVC() {
        return faker.number().digits(3);
    }

    public static String genShortCVC() {
        return faker.number().digits(faker.number().numberBetween(1, 3));
    }

    public static String genLongCVC() {
        return faker.number().digits(faker.number().numberBetween(4, 10));
    }

    public static String genDigits(int from, int to) {
        int rndLength = faker.number().numberBetween(from, to);
        return faker.number().digits(rndLength);
    }

    public static String genNumber(int from, int to) {
        return String.valueOf(faker.number().numberBetween(from, to));
    }


    public static String genCyrillStr() {
        Faker faker = new Faker(new Locale("ru"));
        String chars = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        Random random = new Random();

        int length = faker.number().numberBetween(1, 11);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static String genLatinStr(int min, int max) {
        Faker faker = new Faker(new Locale("en"));
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();

        int length = faker.number().numberBetween(min, max);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static String genSpeсChars() {

        String specialChars = "!@#$%^&*()_+-=[]{}|;':\",./<>?~`";
        Random random = new Random();

        int length = new Faker().number().numberBetween(1, 11);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(specialChars.charAt(random.nextInt(specialChars.length())));
        }
        return sb.toString();
    }
}
