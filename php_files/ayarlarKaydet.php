<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";

if(  isset($_POST['txtEposta']) )
{
	
	// $keyword=$_GET['keyword'];
	 $eposta=$_POST['txtEposta'];
	 $adi=$_POST['txtAdi'];
	 $soyadi=$_POST['txtSoyadi'];
	 $arama_gorulme=$_POST['txtArama'];
	 $resimler_gorulme=$_POST['txtResimler'];
	 $takipci_gorulme=$_POST['txtTakipciler'];
	
     //$keyword=strtolower($keyword);
	mysqli_set_charset($link,'utf8'); 
	$data = array();
	
	$sorgu="UPDATE kullanicilar SET adi='$adi',soyadi='$soyadi',arama_gorulme='$arama_gorulme',resimler_gorulme='$resimler_gorulme',
	           takipci_gorulme='$takipci_gorulme' where eposta='$eposta'";
	
		
	
	
	
 $sonuc=mysqli_query($link, $sorgu);

//mysqli_close($link);
}
?>