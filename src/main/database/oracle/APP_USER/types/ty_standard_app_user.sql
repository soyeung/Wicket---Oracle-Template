create type app_user.ty_standard_app_user as object
(
  aur_id                 int
, aur_username           varchar2(30  char)
, aur_is_account_enabled varchar2(1   char)
, aur_is_tracing_enabled varchar2(1   char)
, lng_id                 int
, aur_created_date       date
, aur_updated_date       timestamp
)
/

sho err