create type app_refdata.ty_db_role as object
(
    dbrl_code            varchar2( 30 char )
,   dbrl_name            varchar2( 250 char )
,   dbrl_is_user_visible varchar2( 1 char )
,   dbrl_order           int
,   dbrlc_id             int
,   dbrl_updated_date    timestamp
)
/