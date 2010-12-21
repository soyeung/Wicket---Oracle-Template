prompt creating table proxy_app_user

create table app_data.proxy_app_user
(
  aur_id           int                                    not null
, aur_created_by   varchar2(30 char) default user         not null
, aur_created_date timestamp         default systimestamp not null
, constraint pk_paur primary key (aur_id)
, constraint fk_paur_aur foreign key (aur_id) references app_data.app_user (aur_id)
)
pctfree 0
/

create unique index app_data.uk_paur on app_data.proxy_app_user ('single_proxy_user')
/