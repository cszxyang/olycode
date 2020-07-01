### olycode todo

- abtain AST through Java compiler API
- diy JVM in c++
- JNI
- design code editor with CodeMirror

table

```mysql
CREATE TABLE request_record (
`id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
`ip` varchar(50) NOT NULL DEFAULT '' COMMENT 'ip',
`request_url` varchar(50) NOT NULL DEFAULT '' COMMENT 'request_url',
`visit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'visit_time',
PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4  COMMENT='request_record';

select count(*) from (select distinct ip from request_record) as t
```
