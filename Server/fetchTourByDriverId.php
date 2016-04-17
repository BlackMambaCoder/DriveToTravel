<?php

	try
	{
		$m = new MongoClient();

	   	#data base name: mDB
	   	$mDB = $m->driverDB;

	   	$tourCol = $mDB->tourCollection;

	   	$id = file_get_contents('php://input');

		$loginQuery = array('tourDriver' => $id);

		$tourCursor = $tourCol->find($loginQuery);

		if ($tourCursor->count() > 0)
		{
			$list = array();

			foreach($tourCursor as $tour)
			{
			    $list[] = $tour;
			}
			
			echo json_encode($list);
		}

		else
		{
			echo "ERROR: no tours for driver " . $id;
		}
	}
	catch (Exception $e)
	{
		echo 'Exception: ' . $e->getMessage();
	}


	//echo "OK";
?>