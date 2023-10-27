    DROP DATABASE IF EXISTS Livros;
    CREATE DATABASE IF NOT EXISTS Livros;
    use livros;

    CREATE TABLE Livros (

        id int primary key auto_increment,
        nome varchar(60) not null,
        autor varchar(30),
        editora varchar(30),
        genero varchar(30)
    );