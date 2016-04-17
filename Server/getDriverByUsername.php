<?php

	try
	{
		$m = new MongoClient();

	   	#data base name: mDB
	   	$mDB = $m->driverDB;

	   	$collection = $mDB->driverCollection;

		$json = file_get_contents('php://input');
	    $obj = json_decode($json);

		$username = $obj->username;
		// $username = $_GET['username'];

		$loginQuery = array('username' => $username);

		$cursor = $collection->find($loginQuery);

		if ($cursor->count() == 1)
		{
			foreach ($cursor as $doc) {
				echo json_encode($doc);
			}
		}

		else
		{
			echo "ERROR";
		}
	}
	catch (Exception $e)
	{
		echo 'Exception: ' . $e->getMessage();
	}
	// finally
	// {
	// 	echo 'finally';
	// }
	


?>