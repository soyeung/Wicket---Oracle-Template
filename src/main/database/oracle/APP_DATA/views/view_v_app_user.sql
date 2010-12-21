prompt creating view v_app_user

create or replace view app_data.v_app_user
as
select
       aur.aur_id                                     aur_id
     , aur.aur_username                               aur_username
     , case 
         upper(dba_aur.account_status)
       when 
            'OPEN'
       then 
            'Y'
       else
            'N'
       end                                            aur_is_account_enabled
     , case
         when
           dba_etr.primary_id is null
         then
           'N'
         else
           'Y'
       end                                            aur_is_tracing_enabled
     , dba_aur.profile                                aur_profile
     , dba_aur.expiry_date                            aur_password_expiry_date
     , aur.aur_created_by                             aur_created_by
     , aur.aur_created_date                           aur_created_date
     , aur.aur_updated_by                             aur_updated_by
     , aur.aur_updated_date                           aur_updated_date
     , nvl
       (
         (
           select 
                  count(*)
             from
                  sys.dba_role_privs dba_rp
            where
                  aur.aur_username = dba_rp.grantee
         )
       , 0
       )                                              aur_number_of_roles
     , cast
       (
         multiset
         (
           select
                  dba_rp.granted_role
             from
                  sys.dba_role_privs dba_rp
            where
                  aur.aur_username = dba_rp.grantee
         )
         as app_utility.tty_string
       )                                              aur_granted_roles
  from
                 app_data.app_user      aur
            join sys.dba_users          dba_aur on aur.aur_username = dba_aur.username
       left join sys.dba_enabled_traces dba_etr on aur.aur_username = dba_etr.primary_id
  with 
       read only
/