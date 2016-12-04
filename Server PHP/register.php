<?php
	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		require "Auth.php";
		
		$name = $_POST['name'];
		$fcm_token = $_POST['token'];
		$jod  = $_POST['date'];

		$sql = "INSERT INTO users(name,token,joindate) VALUES ('$name','$fcm_token','$jod')";

		if(mysqli_query($con,$sql))
		{
                        
			$sql = "SELECT id from users where name='$name' and token='$fcm_token' and joindate='$jod'";
			$results = mysqli_query($con,$sql);

				$row = mysqli_fetch_array($results);
				$uid = $row['id'];
                         
                        $sql = "INSERT INTO status(uid,sstatus,stime) VALUES ($uid,'Hey there! I am using Firebase Messenger','".$jod." 00:00:00')";
                        mysqli_query($con,$sql);
				echo $uid."#You are registered successfully!!";
			
		}
		else
		{
			echo "failed";
		}
	}
	else
	{
			echo "#Failed to contact server";
	}
	mysqli_close($con);
?>		