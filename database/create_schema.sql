drop table if exists synergy.bd_authority;
CREATE TABLE synergy.bd_authority
(
  pk_authority INTEGER PRIMARY KEY NOT NULL auto_increment,
  fk_role INTEGER NOT NULL,
  fk_function INTEGER NOT NULL,
  ts TEXT,
  dr INTEGER
);


drop table if exists synergy.bd_user;
CREATE TABLE synergy.bd_user (
  pk_user integer primary key auto_increment,
  user_name varchar(200) unique,
  password varchar(200),
  status varchar(200),
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  dr integer not null DEFAULT 0
);

drop table if exists synergy.bd_log;
CREATE TABLE synergy.bd_log
(
  id INTEGER PRIMARY KEY,
  user_id INTEGER,
  level varchar(10),
  msg TEXT,
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  dr integer not null DEFAULT 0
);
CREATE UNIQUE INDEX i_log_user_id ON synergy.bd_log ( user_id );


DROP TABLE IF EXISTS synergy.bd_prompt;
CREATE TABLE synergy.bd_prompt (
  pk_prompt integer primary key auto_increment,
  level integer DEFAULT 0,
  module varchar(200),
  detail_en varchar(200),
  detail_zh varchar(200),
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  dr integer not null DEFAULT 0
);

DROP TABLE IF EXISTS synergy.bd_user_role;
CREATE TABLE synergy.bd_user_role (
  pk_user_role integer primary key auto_increment,
  fk_user integer,
  fk_role integer,
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  dr integer not null DEFAULT 0
);

DROP TABLE IF EXISTS synergy.bd_role;
CREATE TABLE synergy.bd_role (
  pk_role integer primary key auto_increment,
  name varchar(200),
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  dr integer not null DEFAULT 0
);

DROP TABLE IF EXISTS synergy.bd_template;
CREATE TABLE synergy.bd_template (
  pk_template integer primary key auto_increment,
  fk_function integer,
  fk_metadata integer,
  type integer,
  module varchar(200),
  user varchar(200),
  description varchar(200),
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  dr integer not null DEFAULT 0
);

INSERT INTO bd_template (pk_template, module) VALUES (100, 'common_form');
INSERT INTO bd_template (pk_template, fk_function, module) VALUES (200, 1,'login');


DROP TABLE IF EXISTS synergy.bd_template_field;
CREATE TABLE synergy.bd_template_field (
  pk_template_field integer primary key auto_increment,
  fk_template integer not null,
  fk_metadata_detail integer,
  name varchar(200),
  type varchar(200),
  field_index integer,
  default_value varchar(200),
  text_en varchar(200),
  text_zh varchar(200),
  description varchar(200),
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  dr integer not null DEFAULT 0
);

INSERT INTO bd_template_field (pk_template_field, fk_template, name,  text_en, text_zh) VALUES (101, 100, 'formFieldNotEmpty',  'Field can not be empty', '字段不能为空');
INSERT INTO bd_template_field (pk_template_field, fk_template, name,  text_en, text_zh) VALUES (201, 200, 'userName', 'User Name', '用户名');
INSERT INTO bd_template_field (pk_template_field, fk_template, name,  text_en, text_zh) VALUES (202, 200, 'password', 'Password', '密码');
INSERT INTO bd_template_field (pk_template_field, fk_template, name,  text_en, text_zh) VALUES (203, 200, 'title', 'Login', '登录');

DROP TABLE IF EXISTS synergy.bd_function;
CREATE TABLE synergy.bd_function (
  pk_function integer primary key auto_increment,
  function_no varchar(200),
  name_en varchar(200),
  name_zh varchar(200),
  parent integer,
  display boolean,
  node_index integer,
  description_en varchar(200),
  description_zh varchar(200),
  file varchar(200),
  url varchar(200),
  leaf boolean,
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  dr integer not null DEFAULT 0
);

DROP TABLE IF EXISTS synergy.bd_operation_group;
CREATE TABLE synergy.bd_operation_group (
  pk_operation_group integer primary key auto_increment,
  fk_function integer not null,
  url varchar(200) not null,
  group_name_en varchar(200),
  group_name_zh varchar(200),
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  dr integer not null DEFAULT 0
);

DROP TABLE IF EXISTS synergy.bd_operation;
CREATE TABLE synergy.bd_operation (
  pk_operation integer primary key auto_increment,
  fk_operation_group integer not null,
  name_en varchar(200),
  name_zh varchar(200),
  description_en varchar(200),
  description_zh varchar(200),
  xtype varchar(200),
  width integer,
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  dr integer not null DEFAULT 0
);

DROP TABLE IF EXISTS synergy.bd_metadata;
CREATE TABLE synergy.bd_metadata (
  pk_metadata integer primary key auto_increment,
  name varchar(200),
  entity_type integer,
  display_name_en varchar(200),
  display_name_zh varchar(200),
  primary_table varchar(200),
  primary_pk varchar(200),
  json_entity varchar(200),
  description_en varchar(200),
  description_zh varchar(200),
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  dr integer not null DEFAULT 0
);

DROP TABLE IF EXISTS synergy.bd_metadata_detail;
CREATE TABLE synergy.bd_metadata_detail (
  pk_metadata_detail integer primary key auto_increment,
  fk_metadata integer not null,
  name varchar(200),
  json_name varchar(200),
  language varchar(10),
  type integer,
  data_type varchar(200),
  field_name varchar(200),
  display_level integer default 1,
  description_en varchar(200),
  description_zh varchar(200),
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  dr integer not null DEFAULT 0
);