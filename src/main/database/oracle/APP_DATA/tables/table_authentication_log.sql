prompt creating table authentication_log

create sequence app_data.sq_lgn
/

create table app_data.authentication_log
(
  lgn_id           int                                      not null
, aur_id           int                                      not null
, lgn_time         timestamp           default systimestamp not null
, lgn_ip_address   varchar2(4000 char)                      not null
, lgn_http_session varchar2(500  char)                      not null
, constraint pk_lgn primary key (lgn_id)
, constraint fk_lgn_aur foreign key (aur_id) references app_data.app_user (aur_id)
)
pctfree 0
/

create unique index app_data.uk_aur_id_lgn_time on app_data.authentication_log (aur_id, lgn_time desc)
/