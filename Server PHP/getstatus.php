<?php

	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		
		
		require_once('Auth.php');
		
                $uid= $_POST['uid'];
		
		$sql = "SELECT s.sstatus,s.stime,u.joindate,u.lastseen FROM status s,users u WHERE s.uid=$uid AND s.uid=u.id";
		$result = mysqli_query($con,$sql);
		
		$statusarray = array();
		
		$row = mysqli_fetch_assoc($result);
    	        array_push($statusarray,array(
									 "status"=>$row['sstatus'],
									 "time"=>$row['stime'],
									 "joindate"=>$row['joindate'],
									 "lastseen"=>$row['lastseen']
									 									 
							    )
					 );

		if(isset($row))
			echo json_encode(array("statusdata"=>$statusarray));	
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