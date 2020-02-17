-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Anamakine: localhost
-- Üretim Zamanı: 07 Oca 2018, 12:19:41
-- Sunucu sürümü: 5.5.54-0ubuntu0.14.04.1
-- PHP Sürümü: 5.5.9-1ubuntu4.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Veritabanı: `tiklapanpa`
--
CREATE DATABASE IF NOT EXISTS `tiklapanpa` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;
USE `tiklapanpa`;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `SilinenKullanicilar`
--

DROP TABLE IF EXISTS `SilinenKullanicilar`;
CREATE TABLE IF NOT EXISTS `SilinenKullanicilar` (
  `kayit_no` int(11) NOT NULL AUTO_INCREMENT,
  `Silen` varchar(55) COLLATE utf8_bin NOT NULL,
  `kullanici` varchar(55) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`kayit_no`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=105 ;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `arkadaslar`
--

DROP TABLE IF EXISTS `arkadaslar`;
CREATE TABLE IF NOT EXISTS `arkadaslar` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `kullanici` varchar(50) COLLATE utf8_bin NOT NULL,
  `friend` varchar(50) COLLATE utf8_bin NOT NULL,
  `dil` varchar(5) COLLATE utf8_bin NOT NULL,
  `online` int(2) DEFAULT '0',
  `onay` int(11) NOT NULL DEFAULT '0',
  `tarih` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `room_id` int(11) DEFAULT NULL,
  `grup` int(1) NOT NULL DEFAULT '0',
  `sahibi` int(1) NOT NULL DEFAULT '0',
  `son_yazi_tarihi` datetime DEFAULT NULL,
  `sessiz` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`no`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=21443 ;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `bildirimler`
--

DROP TABLE IF EXISTS `bildirimler`;
CREATE TABLE IF NOT EXISTS `bildirimler` (
  `b_no` int(22) NOT NULL AUTO_INCREMENT,
  `eposta` varchar(50) COLLATE utf8_bin NOT NULL,
  `gondereneposta` varchar(50) COLLATE utf8_bin NOT NULL,
  `adi` varchar(50) COLLATE utf8_bin NOT NULL,
  `foto` varchar(50) COLLATE utf8_bin NOT NULL,
  `mesaj` varchar(100) COLLATE utf8_bin NOT NULL,
  `resim_no` varchar(25) COLLATE utf8_bin NOT NULL,
  `resimyol` varchar(150) COLLATE utf8_bin NOT NULL,
  `grup` varchar(10) COLLATE utf8_bin NOT NULL,
  `bildirim_tarihi` datetime NOT NULL,
  PRIMARY KEY (`b_no`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=33560 ;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `chat_rooms`
--

DROP TABLE IF EXISTS `chat_rooms`;
CREATE TABLE IF NOT EXISTS `chat_rooms` (
  `chat_room_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`chat_room_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10385 ;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `kullanicilar`
--

DROP TABLE IF EXISTS `kullanicilar`;
CREATE TABLE IF NOT EXISTS `kullanicilar` (
  `kayit_no` int(10) NOT NULL AUTO_INCREMENT,
  `adi` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `soyadi` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `eposta` varchar(30) COLLATE utf8_bin NOT NULL,
  `nick` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `sifre` varchar(20) COLLATE utf8_bin NOT NULL,
  `yas` varchar(5) COLLATE utf8_bin DEFAULT NULL,
  `cinsiyet` varchar(6) COLLATE utf8_bin NOT NULL,
  `foto` varchar(100) COLLATE utf8_bin DEFAULT 'varsayilan/uyu.jpeg',
  `dil` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `gcm_registration_id` text COLLATE utf8_bin,
  `tarih` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Puan` bigint(30) NOT NULL DEFAULT '1',
  `Harcama` int(50) NOT NULL DEFAULT '0',
  `fb` int(11) DEFAULT NULL,
  `bit` tinyint(1) NOT NULL DEFAULT '0',
  `arama_gorulme` tinyint(4) NOT NULL DEFAULT '1',
  `resimler_gorulme` tinyint(4) NOT NULL DEFAULT '1',
  `takipci_gorulme` tinyint(4) NOT NULL DEFAULT '1',
  `giris` tinyint(1) NOT NULL DEFAULT '1',
  `Ulke` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `Sehir` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `ilce` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `adres` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `salla` tinyint(1) NOT NULL DEFAULT '0',
  `latitude` varchar(100) COLLATE utf8_bin NOT NULL,
  `longitude` varchar(100) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`kayit_no`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=4143 ;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `messages`
--

DROP TABLE IF EXISTS `messages`;
CREATE TABLE IF NOT EXISTS `messages` (
  `message_id` int(11) NOT NULL AUTO_INCREMENT,
  `chat_room_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `image` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ses` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `okundu` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`message_id`),
  KEY `chat_room_id` (`chat_room_id`),
  KEY `user_id` (`user_id`),
  KEY `chat_room_id_2` (`chat_room_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=17622 ;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `olaylar`
--

DROP TABLE IF EXISTS `olaylar`;
CREATE TABLE IF NOT EXISTS `olaylar` (
  `olayid` int(10) NOT NULL AUTO_INCREMENT,
  `resim_no` int(10) NOT NULL,
  `kullanici` varchar(100) COLLATE utf8_bin NOT NULL,
  `oy` tinyint(4) NOT NULL,
  `yorum` varchar(140) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `dil` varchar(5) COLLATE utf8_bin DEFAULT NULL,
  `okundumu` int(2) NOT NULL DEFAULT '0',
  `tarih` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sessiz` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`olayid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=92195 ;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `ozelmesajlar`
--

DROP TABLE IF EXISTS `ozelmesajlar`;
CREATE TABLE IF NOT EXISTS `ozelmesajlar` (
  `mesajno` int(11) NOT NULL AUTO_INCREMENT,
  `kimden` varchar(50) COLLATE utf8_bin NOT NULL,
  `kime` varchar(50) COLLATE utf8_bin NOT NULL,
  `icerik` text COLLATE utf8_bin NOT NULL,
  `zaman` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `dil` varchar(5) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`mesajno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `resimler`
--

DROP TABLE IF EXISTS `resimler`;
CREATE TABLE IF NOT EXISTS `resimler` (
  `resim_no` int(20) NOT NULL AUTO_INCREMENT,
  `resim_adi` varchar(100) COLLATE utf8_bin NOT NULL,
  `resim_baslik` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `kullanici` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `yukari` int(20) DEFAULT '1',
  `asagi` int(20) DEFAULT '1',
  `notr` tinyint(4) DEFAULT '0',
  `yorumsayisi` int(11) NOT NULL,
  `kategori` int(5) DEFAULT NULL,
  `dil` varchar(5) COLLATE utf8_bin DEFAULT NULL,
  `tarih` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `gizlilik` tinyint(1) NOT NULL DEFAULT '0',
  `Ulke` varchar(50) COLLATE utf8_bin NOT NULL,
  `Sehir` varchar(50) COLLATE utf8_bin NOT NULL,
  `ilce` varchar(50) COLLATE utf8_bin NOT NULL,
  `adres` varchar(200) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`resim_no`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=2548 ;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `gcm_registration_id` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
