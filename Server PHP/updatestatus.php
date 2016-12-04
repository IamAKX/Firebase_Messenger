<?php
	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		require "Auth.php";
		
		$uid = $_POST['id'];
		$msg= $_POST['status'];
		$tym= $_POST['time'];

		$sql = "UPDATE status SET sstatus = '$msg', stime = '$tym' WHERE uid = $uid";

		if(mysqli_query($con,$sql))
		{
			echo "Status updated successfully.";
		}
		else
		{
			echo "Failed to update status.";
		}
	}
	else
	{
			echo "Failed to contact server";
	}
	mysqli_close($con);
?>		