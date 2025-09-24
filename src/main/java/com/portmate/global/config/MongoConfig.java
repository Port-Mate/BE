package com.portmate.global.config;

import com.portmate.global.util.DateToLocalDateTimeKstConverter;
import com.portmate.global.util.LocalDateTimeToDateKstConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.Arrays;


@Configuration
@EnableMongoAuditing
public class MongoConfig {

    @Bean
    public MongoCustomConversions customConversions(LocalDateTimeToDateKstConverter localDateTimeToDateKstConverter,
                                                    DateToLocalDateTimeKstConverter dateToLocalDateTimeKstConverter){
        return new MongoCustomConversions(
                Arrays.asList(
                        localDateTimeToDateKstConverter,
                        dateToLocalDateTimeKstConverter
                )
        );
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(
            MongoDatabaseFactory factory, MongoMappingContext context, MongoCustomConversions conversions) {
        MappingMongoConverter converter = new MappingMongoConverter(factory, context);
        converter.setCustomConversions(conversions);
        converter.setMapKeyDotReplacement("_"); // 점(.)을 밑줄(_)로 자동 변환
        return converter;
    }

}