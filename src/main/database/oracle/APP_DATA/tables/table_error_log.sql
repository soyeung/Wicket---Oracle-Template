prompt creating table error_log

-- ---------------------------------------------------------------------
-- definition of error_log table
-- ---------------------------------------------------------------------

create sequence app_data.sq_erl
/

create table app_data.error_log
(
  erl_id                int                 not null
, erl_user              varchar2(50   char) not null
, erl_sqlcode           varchar2(50   char)
, erl_sqlerrm           varchar2(250  char)
, erl_timestamp         timestamp           not null
, erl_calling_procedure varchar2(100  char) not null
, erl_error_backtrace   varchar2(4000 char)
, erl_message           varchar2(4000 char)
)
pctfree 0
/

alter table app_data.error_log add constraint pk_erl primary key (erl_id)
/

alter table app_data.error_log modify erl_user default user
/

alter table app_data.error_log modify erl_timestamp default systimestamp
/