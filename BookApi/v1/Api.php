<?php 


	require_once '../includes/DbOperation.php';

	function isTheseParametersAvailable($params){
	
		$available = true; 
		$missingparams = ""; 
		
		foreach($params as $param){
			if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
				$available = false; 
				$missingparams = $missingparams . ", " . $param; 
			}
		}
		
		
		if(!$available){
			$response = array(); 
			$response['error'] = true; 
			$response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)) . ' missing';
			
		
			echo json_encode($response);
			
		
			die();
		}
	}
	
	
	$response = array();
	

	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
	
			case 'createbook':
				
				isTheseParametersAvailable(array('nome','autor','editora','genero'));
				
				$db = new DbOperation();
				
				$result = $db->createBook(
					$_POST['nome'],
					$_POST['autor'],
					$_POST['editora'],
					$_POST['genero']
				);
				

			
				if($result){
					
					$response['error'] = false; 

					
					$response['message'] = 'Livro adicionado com sucesso';

					
					$response['livros'] = $db->getBooks();
				}else{

					
					$response['error'] = true; 

				
					$response['message'] = 'Algum erro ocorreu por favor tente novamente';
				}
				
			break; 
			
		
			case 'getbooks':
				$db = new DbOperation();
				$response['error'] = false; 
				$response['message'] = 'Pedido concluído com sucesso';
				$response['livros'] = $db->getBooks();
			break; 
			
			
		
			case 'updatehero':
				isTheseParametersAvailable(array('id','nome','autor','editora','genero'));
				$db = new DbOperation();
				$result = $db->updateBook(
					$_POST['id'],
					$_POST['nome'],
					$_POST['autor'],
					$_POST['editora'],
					$_POST['genero']
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Livro atualizado com sucesso';
					$response['livros'] = $db->getBooks();
				}else{
					$response['error'] = true; 
					$response['message'] = 'Algum erro ocorreu por favor tente novamente';
				}
			break; 
			
			
			case 'deletebook':

				
				if(isset($_GET['id'])){
					$db = new DbOperation();
					if($db->deleteBook($_GET['id'])){
						$response['error'] = false; 
						$response['message'] = 'Livro excluído com sucesso';
						$response['books'] = $db->getBooks();
					}else{
						$response['error'] = true; 
						$response['message'] = 'Algum erro ocorreu por favor tente novamente';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Não foi possível deletar, forneça um id por favor';
				}
			break; 
		}
		
	}else{
		 
		$response['error'] = true; 
		$response['message'] = 'Chamada de API inválida';
	}
	

	echo json_encode($response);
	
	
