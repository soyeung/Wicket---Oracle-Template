prompt creating table delegate_app_user

create table app_data.delegate_app_user
(
  aur_id           int                                    not null
, aur_created_by   varchar2(30 char) default user         not null
, aur_created_date timestamp         default systimestamp not null
, constraint pk_dlaur     primary key (aur_id)
, constraint fk_dlaur_aur foreign key (aur_id) references app_data.app_user (aur_id)
)
pctfree 0
/