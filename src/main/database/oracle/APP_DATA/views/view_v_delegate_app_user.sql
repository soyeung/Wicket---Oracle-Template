prompt creating view v_delegate_app_user

create or replace view app_data.v_delegate_app_user
as
select 
       vaur.aur_id
     , vaur.aur_username
     , vaur.aur_profile
     , vaur.aur_is_account_enabled
     , vaur.aur_is_tracing_enabled
     , vaur.aur_created_date
     , vaur.aur_updated_date
  from 
            app_data.v_app_user        vaur
       join app_data.delegate_app_user daur on vaur.aur_id = daur.aur_id
  with
       read only
/