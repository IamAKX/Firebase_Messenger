<?php
	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		require "Auth.php";
		
		$uid = $_POST['id'];
		$seen= $_POST['seen'];
		

		$sql = "UPDATE users SET lastseen = '$seen' WHERE id = $uid";

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