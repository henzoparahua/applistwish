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
		$stmt = $this->con->prepare("INSERT INTO livros (nome, autor, editora, genero) VALUES (?, ?, ?, ?)");
		$stmt->bind_param("ssss", $nome, $autor, $editora, $genero);
		if($stmt->execute())
			return true; 
		return false; 
	}
		
	function getBooks(){
		$stmt = $this->con->prepare("SELECT id, nome, autor, editora, genero FROM livros");
		$stmt->execute();
		$stmt->bind_result($id, $nome, $autor, $editora, $genero);
		
		$books = array();
		
		while($stmt->fetch()){
			$book  = array();
			$book['id'] = $id;
			$book['nome'] = $nome;
			$book['autor'] = $autor;
			$book['editora'] = $editora;
			$book['genero'] = $genero;
			
			array_push($books, $book);
		}
		
		return $books;
	}
	
	
	function updateBook($id, $name, $nome, $autor, $editora, $genero){
		$stmt = $this->con->prepare("UPDATE livros SET nome = ?, autor = ?, editora = ?, genero = ? WHERE id = ?");
		$stmt->bind_param("ssssi", $nome, $autor, $editora, $genero, $id);
		if($stmt->execute())
			return true; 
		return false; 
	}
	
	
	function deleteBook($id){
		$stmt = $this->con->prepare("DELETE FROM livros WHERE id = ? ");
		$stmt->bind_param("i", $id);
		if($stmt->execute())
			return true; 
		
		return false; 
	}
}