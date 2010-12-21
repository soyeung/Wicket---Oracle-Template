prompt creating table message_log

-- ---------------------------------------------------------------------
-- definition of message_log table
-- ---------------------------------------------------------------------

create sequence app_data.sq_msl
/

create table app_data.message_log
(
  msl_id                int                 not null
, msl_user              varchar2(50   char) not null
, msl_timestamp         timestamp           not null
, msl_calling_procedure varchar2(100  char) not null
, msl_message           varchar2(4000 char)
)
pctfree 0
/

alter table app_data.message_log add constraint pk_msl primary key (msl_id)
/

alter table app_data.message_log modify msl_calling_procedure default 'not specified'
/

alter table app_data.message_log modify msl_user default user
/

alter table app_data.message_log modify msl_timestamp default systimestamp
/