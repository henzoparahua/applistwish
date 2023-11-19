package br.com.etecia.myapp;

public class Api {
    private static final String ROOT_URL = "http://192.168.15.185/api/v1/Api.php?apicall=";

    public static final String URL_CREATE_BOOKS = ROOT_URL + "createbook";
    public static final String URL_READ_BOOKS = ROOT_URL + "getbooks";
    public static final String URL_UPDATE_BOOKS = ROOT_URL + "updatebook";
    public static final String URL_DELETE_BOOK = ROOT_URL + "deletebook&id=";
}
