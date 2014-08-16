--
-- Turn off autocommit and start a transaction so that we can use the temp tables
--

SET autocommit = 0;

START TRANSACTION;

--
-- Insert user information into the temporary tables. To add users to the HSQL database, edit things here.
-- 

INSERT INTO users_TEMP (username, password, enabled) VALUES
  ('admin','password',true),
  ('user','password',true);


INSERT INTO authorities_TEMP (username, authority) VALUES
  ('admin','ROLE_ADMIN'),
  ('admin','ROLE_USER'),
  ('user','ROLE_USER');
    
-- By default, the username column here has to match the username column in the users table, above
INSERT INTO user_info_TEMP (sub, preferred_username, name, email, email_verified) VALUES
  ('90342.ASDFJWFA','admin','Demo Admin','admin@example.com', true),
  ('01921.FLANRJQW','user','Demo User','user@example.com', true);
    
-- By default, the username column here has to match the username column in the users table, above
INSERT INTO taxe_user_info_TEMP (sub, preferred_username, name, email, email_verified) VALUES
  ('90342.ASDFJWFA','admin','Demo Admin','admin@example.com', true),
  ('01921.FLANRJQW','user','Demo User','user@example.com', true);

 
--
-- Merge the temporary users safely into the database. This is a two-step process to keep users from being created on every startup with a persistent store.
--

INSERT INTO users (username, password, enabled)
  SELECT username, password, enabled FROM users_TEMP as vals
  ON DUPLICATE KEY UPDATE
    users.username = vals.username, 
    users.password = vals.password, 
    users.enabled = vals.enabled;

INSERT INTO authorities (username, authority)
  SELECT username, authority FROM authorities_TEMP AS vals
  ON DUPLICATE KEY UPDATE
    authorities.username = vals.username, 
    authorities.authority = vals.authority;

INSERT INTO user_info (sub, preferred_username, name, email, email_verified)
  SELECT sub, preferred_username, name, email, email_verified FROM user_info_TEMP AS vals
  ON DUPLICATE KEY UPDATE
    user_info.sub = vals.sub, 
    user_info.preferred_username = vals.preferred_username, 
    user_info.name = vals.name, 
    user_info.email = vals.email, 
    user_info.email_verified = vals.email_verified;

INSERT INTO taxe_user_info (sub, preferred_username, name, email, email_verified)
  SELECT sub, preferred_username, name, email, email_verified FROM user_info_TEMP AS vals
  ON DUPLICATE KEY UPDATE
    taxe_user_info.sub = vals.sub, 
    taxe_user_info.preferred_username = vals.preferred_username, 
    taxe_user_info.name = vals.name, 
    taxe_user_info.email = vals.email, 
    taxe_user_info.email_verified = vals.email_verified;

    
-- 
-- Close the transaction and turn autocommit back on
-- 
    
COMMIT;

SET autocommit = 1;

