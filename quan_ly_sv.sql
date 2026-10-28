-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.32-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.21.0.7344
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for quan_ly_sinh_vien
CREATE DATABASE IF NOT EXISTS `quan_ly_sinh_vien` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `quan_ly_sinh_vien`;

-- Dumping structure for table quan_ly_sinh_vien.diem
CREATE TABLE IF NOT EXISTS `diem` (
  `ma_sv` varchar(15) NOT NULL,
  `ma_mh` varchar(15) NOT NULL,
  `diem` decimal(4,2) NOT NULL,
  PRIMARY KEY (`ma_sv`,`ma_mh`),
  KEY `fk_diem_mon_hoc` (`ma_mh`),
  CONSTRAINT `fk_diem_mon_hoc` FOREIGN KEY (`ma_mh`) REFERENCES `mon_hoc` (`ma_mh`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_diem_sinh_vien` FOREIGN KEY (`ma_sv`) REFERENCES `sinh_vien` (`ma_sv`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chk_diem` CHECK (`diem` >= 0 and `diem` <= 10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table quan_ly_sinh_vien.mon_hoc
CREATE TABLE IF NOT EXISTS `mon_hoc` (
  `ma_mh` varchar(15) NOT NULL,
  `ten_mh` varchar(100) NOT NULL,
  `so_tin_chi` int(11) NOT NULL,
  PRIMARY KEY (`ma_mh`),
  CONSTRAINT `chk_so_tin_chi` CHECK (`so_tin_chi` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table quan_ly_sinh_vien.sinh_vien
CREATE TABLE IF NOT EXISTS `sinh_vien` (
  `ma_sv` varchar(15) NOT NULL,
  `ho_ten` varchar(100) NOT NULL,
  `ngay_sinh` date DEFAULT NULL,
  `gioi_tinh` varchar(10) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `so_dien_thoai` varchar(15) DEFAULT NULL,
  `dia_chi` varchar(255) DEFAULT NULL,
  `lop` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ma_sv`),
  UNIQUE KEY `email` (`email`),
  CONSTRAINT `chk_gioi_tinh` CHECK (`gioi_tinh` is null or `gioi_tinh` in ('Nam','Nu','Khac'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
