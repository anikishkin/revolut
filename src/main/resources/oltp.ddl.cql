CREATE TABLE accounts(
    acct_num varchar,
    balance int,
    currency varchar,
    PRIMARY KEY(acct_num));


CREATE TABLE wallets(
    id varchar,
    acct_num varchar,
    holder_name varchar,
    msisdn varchar,
    PRIMARY KEY(id));


CREATE TABLE translog(
    ref_num varchar,
    src varchar,
    dst varchar,
    amount int,
    currency varchar,
    PRIMARY KEY(ref_num));




INSERT INTO accounts(acct_num, balance, currency) values('135790246800001', 100000, '840');

INSERT INTO accounts(acct_num, balance, currency) values('135790246800002', 200000, '840');


INSERT INTO wallets(id, acct_num, holder_name, msisdn) values('123000000001', '135790246800001', 'Marcin Szymaniuk', '12125650001');

INSERT INTO wallets(id, acct_num, holder_name, msisdn) values('123000000002', '135790246800002', 'Jeremy Sevellec', '12125650002');