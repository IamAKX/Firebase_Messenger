<?php

	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		
		
		require_once('Auth.php');
		$name = $_POST['name'];
		
		$sql = "SELECT * FROM `users` WHERE name ='$name'";
		$result = mysqli_query($con,$sql);
		
		$usersarray = array();
		
		$row = mysqli_fetch_assoc($result);
    	        array_push($usersarray,array(
									 "id"=>$row['id'],
									 "name"=>$row['name'],
									 "token"=>$row['token'],
									 "joindate"=>$row['joindate']
									 
							    )
					 );

		if(isset($row))
			echo json_encode(array("userdata"=>$usersarray));	
		else
			echo "fail";

		//$conn->close();
	}
	else
	{
		echo 'error';
	}
	mysqli_close($con);
?>			