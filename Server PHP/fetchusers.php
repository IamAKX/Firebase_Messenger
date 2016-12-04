<?php

	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		
		
		require_once('Auth.php');
		
		$sql = "SELECT * FROM `users`";
		$result = mysqli_query($con,$sql);
		
		$usersarray = array();
		
		while($row = mysqli_fetch_assoc($result))
		    {
		       
		    	 array_push($usersarray,array(
											 "id"=>$row['id'],
											 "name"=>$row['name'],
											 "token"=>$row['token'],
											 "joindate"=>$row['joindate'],
											 "lastseen"=>$row['lastseen'],
											 "profpic"=>$row['profpic']
									    )
							 );
		    }

		echo json_encode(array("userdata"=>$usersarray));	

		//$conn->close();
	}
	else
	{
		echo 'error';
	}
	mysqli_close($con);
?>	