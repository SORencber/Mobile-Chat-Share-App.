<?php
include "baglanti.php";

  
	//if(  isset($_POST['eposta']) &&  isset($_POST['gondereneposta']) && isset($_POST['gonderenisim']) && 
	                  //            isset($_POST['gonderenfoto']) &&isset($_POST['resimno']) && isset($_POST['gondereneposta']))
$eposta=$_GET['eposta'];
	mysqli_set_charset($link,'utf8'); 
	
	
	$data = array();
	
	$sorgu="SELECT * from bildirimler where eposta='$eposta' order by b_no DESC";

	
	
 $sonuc=mysqli_query($link, $sorgu);
 while($veriler=mysqli_fetch_assoc($sonuc)){
	
		$data[]=$veriler;
	 }
	$degistir="";
	$ara='\\';
	echo $cleandata = str_replace($ara,$degistir, json_encode($data, JSON_UNESCAPED_UNICODE));
	//json_encode($data,JSON_UNESCAPED_UNICODE);
	//echo json_encode(json_encode($data, JSON_UNESCAPED_UNICODE),JSON_UNESCAPED_SLASHES);


?>
