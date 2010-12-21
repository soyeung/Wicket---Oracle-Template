prompt creating cluster cl_app_user_profile

create cluster app_data.cl_app_user_profile
(
  aurp_id int
)
size 150
/

create index app_data.cli_app_user_profile on cluster app_data.cl_app_user_profile
/