ALTER TABLE health_record
  ADD COLUMN systolic DOUBLE NULL COMMENT '收缩压',
  ADD COLUMN diastolic DOUBLE NULL COMMENT '舒张压';
