<?php
if($_SERVER['REQUEST_METHOD']=="GET")
{
	echo" GET ile geldin";
}else if($_SERVER['REQUEST_METHOD']=="POST"){
	echo" POST ile geldin";
}
?>