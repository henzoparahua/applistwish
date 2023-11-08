package br.com.etecia.myapp;

public class Api {

    private static final String ROOT_URL = "localhost/HeroApi/v1/Api.php?apicall=";

    public static final String URL_CREATE_HERO = ROOT_URL + "createbook";
    public static final String URL_READ_HEROES = ROOT_URL + "getbook";
    public static final String URL_UPDATE_HERO = ROOT_URL + "updatebook";
    public static final String URL_DELETE_HERO = ROOT_URL + "deletebook&id=";
}