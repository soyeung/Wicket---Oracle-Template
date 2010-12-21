prompt creating table language

create sequence app_data.sq_lng
/

create table app_data.language
(
  lng_id              int                                    not null
, lng_code            varchar2(2  char)                      not null
, lng_name            varchar2(50 char)                      not null
, lng_is_user_visible varchar2(1  char) default 'Y'          not null
, lng_order           int                                    not null
, lng_created_by      varchar2(30 char) default user         not null
, lng_created_date    timestamp         default systimestamp not null
, lng_updated_by      varchar2(30 char) default user         not null
, lng_updated_date    timestamp         default systimestamp not null
, constraint pk_lng primary key (lng_id)
, constraint uk_lng_code unique (lng_code)
, constraint ck_lng_is_user_visible check (lng_is_user_visible in ('Y', 'N'))
)
/

create unique index app_data.uk_lng_name on app_data.language (app_utility.pk_string_utility.fn_reduce_string(lng_name))
/

insert into app_data.language (lng_id, lng_code, lng_name, lng_is_user_visible, lng_order) values (app_data.sq_lng.nextval, 'en', 'English', 'Y', 1)
/

insert into app_data.language (lng_id, lng_code, lng_name, lng_is_user_visible, lng_order) values (app_data.sq_lng.nextval, 'pt', 'Portuguese', 'Y', 2)
/

commit
/