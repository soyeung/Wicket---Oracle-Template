create type app_refdata.ty_subdiv_ref_data as object
(
  lrd_id           int
, lrd_name         varchar2(100 char)
, lrd_is_included  varchar2(1   char)
)
/