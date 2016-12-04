<?php
	
	require "Auth.php";
	$uid = $_POST["uid"];
	$message = $_POST["message"];
	$title = $_POST["title"];

	$path_to_fcm = 'https://fcm.googleapis.com/fcm/send';
	$server_key = "AIzaSyAEBZVQUGCxUigI6YpZRw6z0yjSa9fR58g";
	$count = 0;
	$sql = "select token from users where id in $uid";

	$results = mysqli_query($con,$sql);

	if($results->num_rows > 0)
	{
		while($row = mysqli_fetch_assoc($results))
		{
			$key = $row['token'];

			$headers = array(
					'Authorization:key='.$server_key,
					'Content-Type:application/json'
				);
			
			$fields = array('to' =>$key ,
					'notification' => array('title'=>$title,'body'=>$message)
				);

			$payload = json_encode($fields);

			$curl_session=curl_init(); 
			curl_setopt($curl_session,CURLOPT_URL,$path_to_fcm);
			curl_setopt($curl_session,CURLOPT_POST,true);
			curl_setopt($curl_session,CURLOPT_HTTPHEADER,$headers);
			curl_setopt($curl_session,CURLOPT_RETURNTRANSFER,true);
			curl_setopt($curl_session,CURLOPT_SSL_VERIFYPEER,false);
			curl_setopt($curl_session,CURLOPT_IPRESOLVE,CURL_IPRESOLVE_V4);
			curl_setopt($curl_session,CURLOPT_POSTFIELDS,$payload);
			$result=curl_exec($curl_session); 
			curl_close($curl_session);
			echo $result;
			$count=$count+1;
		}

		echo "Broadcast message sent to $count people.";
	}
	else
	{
		//no tokens!!
		echo "Message sending failed.";
	}

	mysqli_close($con);
?>		