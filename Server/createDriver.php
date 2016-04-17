<?php
   	// connect to mongodb
   	$m = new MongoClient();

   	#data base name: driverDB
   	$mDB = $m->driverDB;

   	$collection = $mDB->driverCollection;

   	$json = file_get_contents('php://input');
    $obj = json_decode($json);

	$name = $obj->name;
	$surname = $obj->surname;
	$username = $obj->username;
	$password = $obj->password;
	$phoneNumber = $obj->phoneNumber;
	$eMail = $obj->eMail;
	$carModel = $obj->carModel;

	# check if such username exists
	$loginQuery = array('username' => $username);

	$driver = $collection->find($loginQuery) or die("ERROR:findDriver");

	if ($driver->count() == 0)
	{
		$driverDocument = array (

			"name" => $name,
			"surname" => $surname,
			"carModel" => $carModel,
			//"noOfPlaces" => $noOfPlaces,
			"username" => $username,
			"password" => $password,
			"eMail" => $eMail,
			"phoneNumber" => $phoneNumber
		);

		$collection->insert($driverDocument) or die("ERROR:insert");

		echo $driverDocument['_id'];
	}

	else
	{
		echo "ERROR:duplicate";
	}
?>