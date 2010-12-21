prompt creating view v_standard_app_user

create or replace view app_data.v_standard_app_user
as
select 
       vaur.aur_id
     , vaur.aur_username
     , vaur.aur_profile
     , vaur.aur_password_expiry_date
     , vaur.aur_is_account_enabled
     , vaur.aur_is_tracing_enabled
     , vaur.aur_number_of_roles
     , vaur.aur_granted_roles
     , lng.lng_id
     , lng.lng_code
     , vaur.aur_created_by
     , vaur.aur_created_date
     , vaur.aur_updated_by
     , vaur.aur_updated_date
  from 
            app_data.v_app_user        vaur
       join app_data.standard_app_user saur on vaur.aur_id = saur.aur_id
       join app_data.language          lng  on saur.lng_id = lng.lng_id
  with
       read only
/