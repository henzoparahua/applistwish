package br.com.etecia.myapp;

public class Api {
    private static final String ROOT_URL = "http://192.168.15.185/api/v1/Api.php?apicall=";

    public static final String URL_CREATE_HERO = ROOT_URL + "createbook";
    public static final String URL_READ_HEROES = ROOT_URL + "getbooks";
    public static final String URL_UPDATE_HERO = ROOT_URL + "updatebook";
    public static final String URL_DELETE_HERO = ROOT_URL + "deletebook&id=";
}
