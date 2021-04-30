create table cron
(
    cron_id varchar(30) null,
    cron    varchar(30) null
);
insert into cron('cron') values('* 0/30 * * * ?');
