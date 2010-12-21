prompt creating table app_user_type_role

create table app_data.app_user_type_role
(
  uty_id           int                                    not null
, dbrl_id          int                                    not null
, utr_created_by   varchar2(30 char) default user         not null
, utr_created_date timestamp         default systimestamp not null
, constraint pk_utr primary key (uty_id, dbrl_id)
, constraint fk_utr_uty  foreign key (uty_id)  references app_data.app_user_type (uty_id)
, constraint fk_utr_dbrl foreign key (dbrl_id) references app_data.db_role       (dbrl_id)
)
organization index
/

create index app_data.i_utr_dbrl on app_data.app_user_type_role (dbrl_id)
/