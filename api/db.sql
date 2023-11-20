DROP DATABASE IF EXISTS Android;
CREATE DATABASE IF NOT EXISTS Android;
use Android;

CREATE TABLE Livros (

    id int primary key auto_increment,
    nome varchar(60) not null,
    autor varchar(30),
    editora varchar(30),
    genero varchar(30)
);