package com.multi.culture_link.config;

import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.CulturalPropertiesKeywordDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.KeywordDTO;
import com.multi.culture_link.admin.culturalProperties.model.dto.StringListTypeHandler;
import com.multi.culture_link.common.region.model.dto.RegionDTO;
import com.multi.culture_link.common.time.model.dto.TimeDTO;
import com.multi.culture_link.culturalProperties.model.dto.CulturalPropertiesInterestDTO;
import com.multi.culture_link.culturalProperties.model.dto.CulturalPropertiesReviewDTO;
import com.multi.culture_link.festival.model.dto.*;
import com.multi.culture_link.users.model.dto.RoleDTO;
import com.multi.culture_link.users.model.dto.UserDTO;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.multi.culture_link", annotationClass = Mapper.class)
public class MyBatisConfiguration {

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {

        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {

        // XML 파일 경로 설정
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:mappers/**/*.xml");
        factoryBean.setMapperLocations(resources);


        // 테이블 속성들 _로 되어있는 것들과 dto의 필드에 camelCase로 되어있는 것들을 자동 연결 ex: user_id // userId
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();

        // 모든 mapper 관련된 log 출력됨
//		configuration.setLogImpl(StdOutImpl.class);

        configuration.setMapUnderscoreToCamelCase(true);

        // null 값 처리
        configuration.setJdbcTypeForNull(JdbcType.NULL);


        // DTO 별명 설정
        configuration.getTypeAliasRegistry().registerAlias("vwUserRoleDTO", VWUserRoleDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("roleDTO", RoleDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("userDTO", UserDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("festivalDTO", FestivalDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("pageDTO", PageDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("regionDTO", RegionDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("timeDTO", TimeDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("festivalKeywordDTO", FestivalKeywordDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("festivalContentReviewNaverKeywordMapping", FestivalContentReviewNaverKeywordMapDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("userFestivalLoveHateDTO", UserFestivalLoveHateMapDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("vwUserReviewDTO", VWUserReviewDataDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("naverArticleDTO", NaverArticleDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("naverBlogDTO", NaverBlogDTO.class);

        configuration.getTypeAliasRegistry().registerAlias("culturalPropertiesDTO", CulturalPropertiesDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("CulturalPropertiesInterestDTO", CulturalPropertiesInterestDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("CulturalPropertiesReviewDTO", CulturalPropertiesReviewDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("CulturalPropertiesKeywordDTO", CulturalPropertiesKeywordDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("KeywordDTO", KeywordDTO.class);


        // TypeHandler 등록
        configuration.getTypeHandlerRegistry().register(StringListTypeHandler.class);


        factoryBean.setConfiguration(configuration);

        return factoryBean.getObject();


    }

}
