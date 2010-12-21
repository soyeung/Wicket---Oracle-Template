prompt creating table app_user

create sequence app_data.sq_aur
/

create table app_data.app_user
(
  aur_id           int                                    not null
, aur_username     varchar2(30 char)                      not null
, aur_created_by   varchar2(30 char) default user         not null
, aur_created_date timestamp         default systimestamp not null
, aur_updated_by   varchar2(30 char) default user         not null
, aur_updated_date timestamp         default systimestamp not null
, constraint pk_aur          primary key (aur_id)
, constraint ck_aur_username check (upper(aur_username) not in ('system', 'sys'))
)
pctfree 0
/

create unique index app_data.uk_aur_username on app_data.app_user ( upper ( aur_username ) )
/
