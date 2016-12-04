<?php
	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		$uid = $_POST['id'];
		$image = $_POST['img'];
		

		require_once('Auth.php');
		
		$name = time().'_'.$uid.".png";
		$path = "profileimage/".$name;
		
		
		if(file_put_contents($path, base64_decode($image))!=false)
		{

			$sql = "UPDATE users SET profpic='$name' WHERE id=$uid";

			if(mysqli_query($con,$sql))
			{
				echo "Profile picture updated successfully.\nSwipe down to refresh";

			}
			else
			{
				echo "Uploading Failed";

			}
			
		}
		else
		{
			echo "Failed to decode image. Bad image format.";
		}
	}
	else
	{
		echo 'error';
	}
	mysqli_close($con);
?>