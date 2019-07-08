create table Maschine(
	mnr int primary key, 
	mname varchar(10)
);

create table Abteilung( 
	abtnr char(3) primary key, 
	aname varchar(10)
);

create table Personal(  
	pnr int primary key, 
	pname varchar(10) not null, 
	vorname varchar(10), 
	abt char(3) references Abteilung(abtnr), 
	lohn int default 36000
);

create table ALeitung( 
	pnr int references Personal(pnr) on delete cascade,
	abtnr char(3) primary key references Abteilung(abtnr)
);

create table PMzuteilung(
	pnr int references Personal(pnr) on delete cascade, 
	mnr int references Maschine(mnr) on delete cascade, 
 	note int, 
 	constraint pk primary key (pnr,mnr)
);


insert into Abteilung values 
	('B10', 'Spielzeug'),
	('A63', 'Computer'),
	('A64', 'Suppen');


insert into Maschine values 
	(84,  'Presse'), 
	(93 , 'Füllanlage'),
	(101, 'Säge');


insert into Personal values 
	( 67, 'Meier', '  Helmut', 'B10', 65000), 
	( 73, 'Müller',  'Margot', 'B10', 51000),
	(114, 'Bayer',   'Martin', 'A63', 60000),
	( 51, 'Daum',    'Birgit', 'A64', 72000),
	( 69, 'Störmer', 'Willi',  'A64', 60000),
	(333, 'Haar',    'Hans',   'A63', 75000),
	(701, 'Reiner',  'Willi',  'A64', 42500),
	( 82, 'Just',   'Michael', 'A64', 65000);


insert into ALeitung values 
	(67,  'B10'), 
	(333, 'A63'), 
	(333, 'A64');


insert into PMzuteilung values
	(67, 84, 3),
	(67, 93, 2),
	(67, 101, 3),
	(73, 84, 5),
	(114, 93, 5),
	(114, 101, 3),
	(51, 93, 2),
	(69, 101, 2),
	(333, 84, 3),
	(701, 84, 2),
	(701, 101, 2),
	(82, 101, 2);

-- drop table pmzuteilung;
-- drop table aleitung;
-- drop table personal;
-- drop table maschine;
-- drop table abteilung; 
