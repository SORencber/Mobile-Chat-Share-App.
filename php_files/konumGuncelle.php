<?PHP
include "baglanti.php";
mysqli_set_charset($link,'utf8mb4'); 


if(isset($_POST['txtKullanici'])){
	
	   $txteposta = $_POST['txtKullanici'];
	    $txtUlke = $_POST['txtUlke'];
	   	   $txtSehir = $_POST['txtSehir'];
	   	   $txtIlce = $_POST['txtIlce'];
	   	   $txtadres = $_POST['txtadres'];
		   	   	   $txtlatitude = $_POST['txtlatitude'];
	   	   $txtlongitude = $_POST['txtlongitude'];

    guncelle($_POST['txtKullanici'],$txtUlke,$txtSehir,$txtIlce,$txtadres,$txtlatitude,$txtlongitude);
    exit;
}
else{
    echo "image_not_in";
    exit;
}
function guncelle($txteposta,$txtUlke,$txtSehir,$txtIlce,$txtadres){
    //create unique image file name based on micro time and date
  
	  $sorgu="update kullanicilar SET latitude='$txtlatitude', longitude='$txtlongitude',Ulke='$txtUlke',Sehir='$txtSehir',ilce='$txtIlce',adres='$txtadres' Where eposta='$txteposta'  "; 	
 $sonuc=mysqli_query($link, $sorgu);
	
   
        echo "1";
   
    } 
mysqli_close($link);	

