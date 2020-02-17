<?php
/**
 * @author Ravi Tamada
 * @link URL Tutorial link
 */

class Push{
    // push message title
    private $title;
    
    // push message payload
    private $data;
    
    // flag indicating background task on push received
    private $is_background;
    
    // flag to indicate the type of notification
    private $flag;
    
	
	private $imageUrl;

    private $sahibi;
private $grup;

	private $foto;

	
	function __construct() {
        
    }
    
    public function setTitle($title){
        $this->title = $title;
    }
     public function setImageUrl($imageUrl){
        $this->imageUrl = $imageUrl;
    }
    public function setData($data){
        $this->data = $data;
    }
    
    public function setIsBackground($is_background){
        $this->is_background = $is_background;
    }
    
    public function setFlag($flag){
        $this->flag = $flag;
    }
     public function setGrup($grup){
        $this->grup = $grup;
    }
     public function setSahibi($sahibi){
        $this->sahibi = $sahibi;
    }
      public function setFoto($foto){
        $this->foto = $foto;
    }
    public function getPush(){
        $res = array();
        $res['title'] = $this->title;
        $res['is_background'] = $this->is_background;
        $res['flag'] = $this->flag;
		$res['imageUrl'] = $this->imageUrl;
        $res['data'] = $this->data;
                $res['sahibi'] = $this->sahibi;
                $res['grup'] = $this->grup;
                $res['foto'] = $this->foto;

        return $res;
    }
}