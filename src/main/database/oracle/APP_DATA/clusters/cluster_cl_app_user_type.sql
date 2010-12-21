prompt creating cluster cl_app_user_type

create cluster app_data.cl_app_user_type
(
  uty_id int
)
size 60
/

create index app_data.cli_app_user_type on cluster app_data.cl_app_user_type
/