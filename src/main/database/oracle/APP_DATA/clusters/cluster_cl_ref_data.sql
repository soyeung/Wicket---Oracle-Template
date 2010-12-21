prompt creating b*tree index cluster cl_ref_data

create cluster app_data.cl_ref_data
(
  rds_id int
)
size 400
pctfree 0
/

create index app_data.cli_ref_data on cluster app_data.cl_ref_data
/