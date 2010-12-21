prompt creating table app_data.restricted_sqlplus_function

create table app_data.restricted_sqlplus_function
(
  rsf_function     varchar2(100 char)                     not null
, rsf_created_by   varchar2(30 char) default user         not null
, rsf_created_date timestamp         default systimestamp not null
, constraint pk_rsf primary key(rsf_function)
)
pctfree 0
/

prompt populating table app_data.restricted_sqlplus_function

begin

  --
  -- Restrict SQL*Plus commands
  --

  insert into app_data.restricted_sqlplus_function (rsf_function) values ('COPY');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('HOST');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('SET');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('EDIT');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('PASSWORD');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('SPOOL');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('EXECUTE');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('QUIT');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('START');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('EXIT');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('RUN');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('GET');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('SAVE');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('BEGIN');

  --
  -- Restrict SQL commands within SQL*PLUS
  --

  insert into app_data.restricted_sqlplus_function (rsf_function) values ('ALTER');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('GRANT');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('SET CONSTRAINTS');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('ANALYZE');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('INSERT');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('SET ROLE');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('AUDIT');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('LOCK');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('SET TRANSACTION');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('CONNECT');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('NOAUDIT');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('TRUNCATE');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('CREATE');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('RENAME');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('UPDATE');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('DELETE');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('REVOKE');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('DROP');
  insert into app_data.restricted_sqlplus_function (rsf_function) values ('SELECT');

  --
  -- Restrict PL/SQL commands from SQL*PLUS
  --

  insert into app_data.restricted_sqlplus_function (rsf_function) values ('DECLARE');

  commit;

end;
/