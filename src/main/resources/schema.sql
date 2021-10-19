drop table payments;
drop table charges;
drop table people;

create table if not exists people (
    id UUID default RANDOM_UUID() not null primary key,
    name varchar(100),
    disable boolean
);

create table if not exists payments (
    person UUID not null,
    amount number(15, 2),
    date date,
    primary key (person, date),
    foreign key (person) references people(id)
);

create table if not exists charges (
    person UUID not null,
    amount number(15, 2),
    date date,
    primary key (person, date),
    foreign key (person) references people(id)
);

