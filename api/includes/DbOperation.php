<?php

class DbOperation
{

    private $con;


    function __construct()
    {

        require_once dirname(__FILE__) . '/DbConnect.php';


        $db = new DbConnect();


        $this->con = $db->connect();
    }


	function createBook($nome, $autor, $editora, $genero){
		$stmt = $this->con->prepare("INSERT INTO Livros (name, autor, editora, genero) VALUES (?, ?, ?, ?)");
		$stmt->bind_param("ssis", $nome, $autor, $editora, $genero);
		if($stmt->execute())
			return true;
		return false;
	}

	function getBook(){
		$stmt = $this->con->prepare("SELECT id, nome, autor, editora, genero FROM Livros");
		$stmt->execute();
		$stmt->bind_result($id, $nome, $autor, $editora, $genero);

		$livros = array();

		while($stmt->fetch()){
			$livro  = array();
			$livro['id'] = $id;
			$livro['nome'] = $nome;
			$livro['autor'] = $autor;
			$livro['editora'] = $editora;
			$livro['genero'] = $genero;

			array_push($livros, $livro);
		}

		return $livros;
	}


	function updateBook($id, $nome, $autor, $editora, $genero){
		$stmt = $this->con->prepare("UPDATE Livros SET nome = ?, autor = ?, editora = ?, genero = ? WHERE id = ?");
		$stmt->bind_param("ssssi", $nome, $autor, $editora, $genero, $id);
		if($stmt->execute())
			return true;
		return false;
	}


	function deleteBook($id){
		$stmt = $this->con->prepare("DELETE FROM Livros WHERE id = ? ");
		$stmt->bind_param("i", $id);
		if($stmt->execute())
			return true;

		return false;
	}
}
?>