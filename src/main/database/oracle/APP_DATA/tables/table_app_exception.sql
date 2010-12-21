prompt creating table app_exception

create table app_data.app_exception
(
  aex_id            int                                      not null
, aex_code          varchar2(50   char)                      not null
, aex_message       varchar2(4000 char)                      not null
, aex_created_by    varchar2(50   char) default user         not null
, aex_created_date  timestamp           default systimestamp not null
, constraint pk_aex primary key (aex_id)
, constraint ck_aex_exception_id check (aex_id between -20999 and -20000)
)
pctfree 0
/

create unique index app_data.uk_aex_code on app_data.app_exception (app_utility.pk_string_utility.fn_reduce_string(aex_code))
/