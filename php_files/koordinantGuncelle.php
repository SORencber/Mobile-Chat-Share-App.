<?PHP
include "baglanti.php";
mysqli_set_charset($link,'utf8mb4'); 


if(isset($_POST['txtKullanici'])){
	
	       $txteposta = $_POST['txtKullanici'];
	       $txtlatitude = $_POST['txtlatitude'];
	   	   $txtlongitude = $_POST['txtlongitude'];


  
	  $sorgu="update kullanicilar SET latitude='$txtlatitude', longitude='$txtlongitude' Where eposta='$txteposta'  "; 	
 $sonuc=mysqli_query($link, $sorgu);
	
   
        echo "1";
   
    
mysqli_close($link);	

}