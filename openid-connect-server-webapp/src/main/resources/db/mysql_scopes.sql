--
-- Turn off autocommit and start a transaction so that we can use the temp tables
--

SET autocommit = 0;

START TRANSACTION;

--
-- Insert scope information into the temporary tables.
-- 

INSERT INTO system_scope_TEMP (scope, description, icon, allow_dyn_reg, default_scope, structured, structured_param_description) VALUES
  ('openid', 'log in using your identity', 'user', true, true, false, null),
  ('profile', 'basic profile information', 'list-alt', true, true, false, null),
  ('email', 'email address', 'envelope', true, true, false, null),
  ('address', 'physical address', 'home', true, true, false, null),
  ('phone', 'telephone number', 'bell', true, true, false, null),
  ('offline_access', 'offline access', 'time', true, true, false, null);
  
--
-- Merge the temporary scopes safely into the database. This is a two-step process to keep scopes from being created on every startup with a persistent store.
--

INSERT INTO system_scope (scope, description, icon, allow_dyn_reg, default_scope, structured, structured_param_description)
  SELECT scope, description, icon, allow_dyn_reg, default_scope, structured, structured_param_description FROM system_scope_TEMP AS vals
  ON DUPLICATE KEY UPDATE
    system_scope.scope = vals.scope, 
    system_scope.description = vals.description, 
    system_scope.icon = vals.icon, 
    system_scope.allow_dyn_reg = vals.allow_dyn_reg, 
    system_scope.default_scope = vals.default_scope, 
    system_scope.structured = vals.structured, 
    system_scope.structured_param_description = vals.structured_param_description;

COMMIT;

SET autocommit = 1;