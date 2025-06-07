-- Insert a user for Admin role
INSERT INTO "User" (user_id, username, password, NIS_NIP, nama, gender, alamat, email, nomer_hp, Role_role_id)
VALUES ('A250001', 'aaron25', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'NIP001', 'Aaron Jordan', 'L', 'Jl. Admin No. 1', 'admin@example.com', '081234567890', 'A');

-- Insert a user for Kepala Sekolah role
INSERT INTO "User" (user_id, username, password, NIS_NIP, nama, gender, alamat, email, nomer_hp, Role_role_id)
VALUES ('K250001', 'kepala', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'NIP002', 'Kepala Sekolah Name', 'P', 'Jl. Kepala No. 2', 'kepala@example.com', '081234567891', 'K');

-- Insert a user for Guru role
INSERT INTO "User" (user_id, username, password, NIS_NIP, nama, gender, alamat, email, nomer_hp, Role_role_id)
VALUES ('G250001', 'guru', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'NIP003', 'Guru Name', 'L', 'Jl. Guru No. 3', 'guru@example.com', '081234567892', 'G');

-- Insert a user for Wali Kelas role
INSERT INTO "User" (user_id, username, password, NIS_NIP, nama, gender, alamat, email, nomer_hp, Role_role_id)
VALUES ('W250001', 'wali', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'NIP004', 'Wali Kelas Name', 'P', 'Jl. Wali No. 4', 'wali@example.com', '081234567893', 'W');

-- Insert a user for Siswa role
INSERT INTO "User" (user_id, username, password, NIS_NIP, nama, gender, alamat, email, nomer_hp, Role_role_id)
VALUES ('S250001', 'siswa', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'NIS001', 'Siswa Name', 'L', 'Jl. Siswa No. 5', 'siswa@example.com', '081234567894', 'S');


INSERT INTO Role (role_id, role_name) VALUES ('A', 'Admin');
INSERT INTO Role (role_id, role_name) VALUES ('K', 'Kepala Sekolah');
INSERT INTO Role (role_id, role_name) VALUES ('G', 'Guru');
INSERT INTO Role (role_id, role_name) VALUES ('W', 'Wali Kelas');
INSERT INTO Role (role_id, role_name) VALUES ('S', 'Siswa');