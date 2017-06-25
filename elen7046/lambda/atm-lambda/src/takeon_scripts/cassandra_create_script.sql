
CREATE KEYSPACE asset_tracking_management
  WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE asset_tracking_management.coordinates_batch
(deviceid varchar,
publishedtime varchar,
latitude double,
longitude double,
PRIMARY KEY (deviceid, publishedtime));


CREATE TABLE asset_tracking_management.coordinates_speed
(deviceid varchar,
publishedtime varchar,
latitude double,
longitude double,
PRIMARY KEY (deviceid, publishedtime));

CREATE TABLE asset_tracking_management.coordinates_daily
(deviceid varchar,
publishedtime varchar,
latitude double,
longitude double,
date varchar,
PRIMARY KEY (deviceid, publishedtime));

CREATE TABLE asset_tracking_management.coordinates_day_of_week
(deviceid varchar,
publishedtime varchar,
latitude double,
longitude double,
date varchar,
dayOfWeek varchar,
PRIMARY KEY (deviceid, publishedtime));

CREATE TABLE asset_tracking_management.coordinates_monthly
(deviceid varchar,
publishedtime varchar,
latitude double,
longitude double,
date varchar,
month varchar,
PRIMARY KEY (deviceid, publishedtime, month));

CREATE TABLE asset_tracking_management.coordinates_hourly
(deviceid varchar,
publishedtime varchar,
latitude double,
longitude double,
date varchar,
dayOfWeek varchar,
hour varchar,
PRIMARY KEY (deviceid, publishedtime,date));