prompt creating table standard_app_user

create table app_data.standard_app_user
(
  aur_id           int                                    not null
, lng_id           int                                    not null
, aur_created_by   varchar2(30 char) default user         not null
, aur_created_date timestamp         default systimestamp not null
, constraint pk_staur primary key (aur_id)
, constraint fk_staur_aur foreign key (aur_id) references app_data.app_user (aur_id)
, constraint fk_staur_lng foreign key (lng_id) references app_data.language (lng_id)
)
/

create index app_data.i_staur_lng_id on app_data.standard_app_user (lng_id)
/