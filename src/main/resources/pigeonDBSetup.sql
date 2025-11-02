create database if not exists pigeonDB;

use pigeonDB;

create table if not exists user(
id int primary key auto_increment,
login varchar(20),
email varchar(50),
hashedPass varchar(50),
recoveryPass varchar(12),
dateCreated dateTime,

isAdmin bool,
isBanned bool
);

create table if not exists mails(
id int primary key auto_increment,
senderID int,
receiverID int,
emailText mediumtext,
attachment mediumblob,
sendTime datetime,
subject varchar(75),
Foreign key(senderID) references user(id),
Foreign key(reeceiverID) references user(id)
);