package com.health.mapper;

import com.health.entity.HealthRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface HealthRecordMapper {

    @Select("SELECT * FROM health_record WHERE user_id=#{userId} AND type=#{type} AND record_date=#{recordDate} LIMIT 1")
    HealthRecord selectOneByUserIdAndTypeAndDate(@Param("userId") Long userId,
                                                 @Param("type") String type,
                                                 @Param("recordDate") java.time.LocalDate recordDate);

    @Insert("insert into health_record(user_id,type,value,unit,record_date,remark) " +
            "values(#{userId},#{type},#{value},#{unit},#{recordDate},#{remark})")
    int insert(HealthRecord record);

    @Update("update health_record set value=#{value}, unit=#{unit}, remark=#{remark} " +
            "where user_id=#{userId} and type=#{type} and record_date=#{recordDate}")
    int updateByUserIdAndTypeAndDate(HealthRecord record);

    @Select("SELECT record_date, value FROM health_record " +
            "WHERE user_id=#{userId} AND type=#{type} ORDER BY record_date")
    List<Map<String,Object>> selectTrend(@Param("userId") Long userId,
                                         @Param("type") String type);

    @Select("SELECT * FROM health_record WHERE user_id=#{userId} ORDER BY record_date DESC")
    List<HealthRecord> selectByUserId(@Param("userId") Long userId);
}