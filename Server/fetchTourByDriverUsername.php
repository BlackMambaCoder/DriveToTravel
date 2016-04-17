<?php

	$m = new MongoClient();

   	#data base name: mDB
   	$mDB = $m->driverDB;

   	$tourCol = $mDB->tourCollection;
   	$driverCol = $mDB->driverCollection;

	$username = $_POST ["username"];


	$loginQuery = array('username' => $username); //, 'password' => $password);

	$driver = $driverCol.find($loginQuery);

	if ($driver->count() == 1)
	{
		$loginQuery = array('tourDriver', $driver->_id);
		$tour = $tourCol.find($loginQuery);

		echo json_encode($tour);
	}

	else
	{
		echo "ERROR: driver error";
	}
?>