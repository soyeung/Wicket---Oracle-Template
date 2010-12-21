create type app_user.ty_db_role as object
(
  dbrl_id                  int
, dbrl_is_assigned_to_user varchar2(1 char) -- ('Y' or 'N')
)
/

sho err