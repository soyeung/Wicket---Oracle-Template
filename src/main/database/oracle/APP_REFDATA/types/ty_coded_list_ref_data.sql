create type app_refdata.ty_coded_list_ref_data as object
(
    lrd_id              int
,   lrd_code            varchar2(15  char)
,   lrd_name            varchar2(100 char)
,   lrd_is_user_visible varchar2(1   char)
,   lrd_order           int
,   lrd_updated_date    timestamp
)
/