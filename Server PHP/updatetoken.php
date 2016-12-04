<?php
	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		require "Auth.php";
		
		$uid = $_POST['uid'];
		$fcm_token = $_POST['token'];
		

		$sql = "UPDATE users SET token = '$fcm_token' WHERE id = $uid";

		if(mysqli_query($con,$sql))
		{
			echo "success";
		}
		else
		{
			echo "failed";
		}
	}
	else
	{
			echo "Failed to contact server";
	}
	mysqli_close($con);
?>		