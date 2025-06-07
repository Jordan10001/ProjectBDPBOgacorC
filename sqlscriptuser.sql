-- Insert a user for Admin role
INSERT INTO Users (user_id, username, password, NIS_NIP, nama, gender, alamat, email, nomer_hp, Role_role_id)
VALUES ('A250001', 'aaron25', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'NIP001', 'Aaron Jordan', 'L', 'Jl. Admin No. 1', 'admin@example.com', '081234567890', 'A');

-- Insert a user for Kepala Sekolah role
INSERT INTO Users (user_id, username, password, NIS_NIP, nama, gender, alamat, email, nomer_hp, Role_role_id)
VALUES ('K250001', 'kepala', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'NIP002', 'Kepala Sekolah Name', 'P', 'Jl. Kepala No. 2', 'kepala@example.com', '081234567891', 'K');

-- Insert a user for Guru role
INSERT INTO Users (user_id, username, password, NIS_NIP, nama, gender, alamat, email, nomer_hp, Role_role_id)
VALUES ('G250001', 'guru', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'NIP003', 'Guru Name', 'L', 'Jl. Guru No. 3', 'guru@example.com', '081234567892', 'G');

-- Insert a user for Wali Kelas role
INSERT INTO Users (user_id, username, password, NIS_NIP, nama, gender, alamat, email, nomer_hp, Role_role_id)
VALUES ('W250001', 'wali', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'NIP004', 'Wali Kelas Name', 'P', 'Jl. Wali No. 4', 'wali@example.com', '081234567893', 'W');

-- Insert a user for Siswa role
INSERT INTO Users (user_id, username, password, NIS_NIP, nama, gender, alamat, email, nomer_hp, Role_role_id)
VALUES ('S250001', 'siswa', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'NIS001', 'Siswa Name', 'L', 'Jl. Siswa No. 5', 'siswa@example.com', '081234567894', 'S');

--ROLE
INSERT INTO Role (role_id, role_name) VALUES ('A', 'Admin');
INSERT INTO Role (role_id, role_name) VALUES ('K', 'Kepala Sekolah');
INSERT INTO Role (role_id, role_name) VALUES ('G', 'Guru');
INSERT INTO Role (role_id, role_name) VALUES ('W', 'Wali Kelas');
INSERT INTO Role (role_id, role_name) VALUES ('S', 'Siswa');

--SEMESTER
INSERT INTO Semester (tahun_ajaran, semester, tahun) VALUES ('2024/2025', 'Ganjil', '2024-07-01 00:00:00');

--MATPEL
INSERT INTO Matpel (nama_mapel, category) VALUES ('Matematika', 'Umum');

--KELAS
INSERT INTO Kelas (nama_kelas, keterangan, Users_user_id, Semester_semester_id) VALUES ('Kelas 10A', 'Siswa SMA Kelas 10 A', 'USR002', 1);

--JADWAL
INSERT INTO Jadwal (hari, jam_mulai, jam_selsai, Kelas_Users_user_id, Matpel_mapel_id, Kelas_kelas_id) VALUES
('Senin', '08:00', '09:30', 'USR002', 1, 1);

--ABSENSI
INSERT INTO Absensi (tanggal, status, Users_user_id, Jadwal_jadwal_id) VALUES ('2024-07-08 08:00:00', 'Hadir', 'USR003', 1);

--DETAIL PENGAJAR
INSERT INTO Detail_Pengajar (Users_user_id, Kelas_Users_user_id, Matpel_mapel_id, Kelas_kelas_id) VALUES
('USR002', 'USR002', 1, 1);

--FEEDBACK
INSERT INTO Feedback (feedback, Users_user_id) VALUES ('Aplikasi sangat membantu.', 'USR003');

--MATERI
INSERT INTO Materi (nama_materi, Kelas_Users_user_id, Matpel_mapel_id, Kelas_kelas_id) VALUES
('Pengenalan Aljabar', 'USR002', 1, 1);

--PENGUMUMAN
INSERT INTO Pengumuman (pengumuman, Users_user_id) VALUES ('Libur Idul Adha.', 'USR001');

--RAPOR
INSERT INTO Rapor (Users_user_id, Semester_semester_id) VALUES ('USR003', 1);

-- NILAI
INSERT INTO Nilai (nilai, jenis_nilai, Matpel_mapel_id, Rapor_rapor_id) VALUES (85, 'UTS', 1, 1);

--TUGAS
INSERT INTO Tugas (keterangan, deadline, tanggal_direlease, Kelas_Users_user_id, Matpel_mapel_id, Kelas_kelas_id) VALUES
('Kerjakan latihan soal', '2024-07-15 23:59:59', '2024-07-08 08:00:00', 'USR002', 1, 1);

--UJIAN
INSERT INTO Ujian (jenis_ujian, tanggal, Kelas_Users_user_id, Matpel_mapel_id, Kelas_kelas_id) VALUES
('UTS', '2024-09-01 08:00:00', 'USR002', 1, 1);

--USER LOG
INSERT INTO User_Log (jenis, keterangan, waktu, Users_user_id) VALUES
('Login', 'Pengguna admin1 berhasil login', '2025-06-07 18:00:00', 'USR001');



