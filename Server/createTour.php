<?php
   	// connect to mongodb
   	$m = new MongoClient();

   	#data base name: driverDB
   	$mDB = $m->driverDB;

   	$collection = $mDB->tourCollection;

   	$json = file_get_contents('php://input');
    $obj = json_decode($json);


	$startLocation = $obj->startLocation;
	$destinationLocation = $obj->destinationLocation;
	$dateAndTime = $obj->startDateAndTime;

	if (property_exists($obj, 'tourDriver'))
	{
		$tourDriver = $obj->tourDriver;
	}
	else
	{
		$tourDriver = NULL;
	}

	if (property_exists($obj, 'passengers'))
	{
		$passengers = $obj->passengers;
	}
	else
	{
		$passengers = NULL;
	}

	$newTour = array (

			"startLocation" => $startLocation,
			"destinationLocation" => $destinationLocation,
			"dateAndTime" => $dateAndTime,
			"tourDriver" => $tourDriver,
			"passengers" => $passengers
		) or die("null");

	

	$collection->insert($newTour) or die("ERROR: insert tour");

	echo "OK";
?>